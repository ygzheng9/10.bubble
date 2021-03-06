import { Controller } from 'stimulus';

import $ from 'cash-dom';
import echarts from 'echarts';
import _ from 'lodash';
import axios from 'axios';
import numeral from 'numeral';

export default class extends Controller {
  connect() {
    this.startup();
  }

  startup() {
    const myChart = echarts.init(document.getElementById('plantSummary'));
    const url = `/pages/inbound/plantVendorData`;

    const barChart = echarts.init(document.getElementById('vendorList'));

    function getRange(items) {
      const vals = items.map(i => i['vendorAvg']);
      return [_.min(vals), _.max(vals)];
    }

    axios
      .get(url)
      .then(res => res.data)
      .then(res => {
        const { items } = res;

        const treeData = items.map(s => {
          return {
            name: s['toPlant'],
            value: [s['totalAmt'], s['vendorCount'], s['vendorAvg']]
          };
        });

        const range = getRange(items);

        const option = {
          title: {
            top: 'top',
            left: 'center',
            text: '工厂采购分析'
          },
          tooltip: {
            // 只有这里设置了，才能显示 tooltip
            trigger: 'axis',
            axisPointer: {
              // 坐标轴指示器，坐标轴触发有效
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          series: [
            {
              type: 'treemap',
              name: '全部',
              roam: false,
              data: treeData,
              // 距离顶部的距离
              //   top: 40,
              // 显示的字颜色
              label: {
                color: '#000',
                // fontWeight: 'bold'
                fontSize: 12
              },
              tooltip: {
                trigger: 'item',
                formatter: function(param) {
                  const { name, value } = param.data;
                  return [
                    '工厂: ' + name + '<br/>',
                    '总采购金额：' +
                      numeral(value[0]).format('0,0.00') +
                      '<br/>',
                    '供应商数量：' + value[1] + '<br/>',
                    '平均采购额: ' + numeral(value[2]).format('0,0.00')
                  ].join('');
                }
              }
            }
          ],
          visualMap: [
            {
              type: 'continuous', //连续型
              show: false,
              dimension: 2, //对应的数组维度
              min: range[0],
              max: range[1]
            }
          ]
        };

        myChart.setOption(option, true);

        myChart.on('click', 'series', params => {
          if (params.data === undefined) {
            return;
          }

          const { name } = params.data;
          const url = `/pages/inbound/vendorAmtByPlantData?p=${name}`;
          axios
            .get(url)
            .then(res => res.data)
            .then(res => {
              const items = res.items.sort((a, b) => a.totalAmt - b.totalAmt);
              // console.log(items);

              const option = {
                title: {
                  top: 'top',
                  left: 'center',
                  text: `工厂采购量：${name}`
                },
                dataset: {
                  dimension: [
                    'toPlant',
                    'vendorCode',
                    'vendorName',
                    'totalAmt'
                  ],
                  source: items
                },
                tooltip: {
                  // 只有这里设置了，才能显示 tooltip
                  trigger: 'axis',
                  axisPointer: {
                    // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                  },
                  formatter: function(param) {
                    const { toPlant, vendorName, totalAmt } = param[0].data;
                    return [
                      '工厂：' + toPlant + '<br/>',
                      '供应商: ' + vendorName + '<br/>',
                      '总采购金额：' + numeral(totalAmt).format('0,0.00')
                    ].join('');
                  }
                },
                xAxis: {
                  type: 'value',
                  position: 'top'
                },
                yAxis: {
                  type: 'category',
                  axisLabel: {
                    // 显示所有的 label
                    interval: 0,
                    fontSize: 8
                  }
                },
                grid: {
                  left: '20%'
                },
                series: [
                  {
                    encode: { x: 'totalAmt', y: 'vendorName' },
                    type: 'bar',
                    itemStyle: {
                      // 柱状图颜色
                      color: '#4ad2ff'
                    }
                  }
                ]
              };

              barChart.setOption(option);

              barChart.getZr().off('click');
              barChart.getZr().on('click', params => {
                const point = [params.offsetX, params.offsetY];
                if (barChart.containPixel({ seriesIndex: 0 }, point)) {
                  const t1 = barChart.convertFromPixel(
                    { seriesIndex: 0 },
                    point
                  );
                  // console.log('fromPixel: ', t1);
                  // const t2 = barChart.convertToPixel({ seriesIndex: 0 }, point);
                  // console.log('toPixel: ', t2);

                  const xIndex = t1[1];

                  // console.log('xIndex: ', xIndex);

                  const op = barChart.getOption();
                  let entry = op.dataset[0].source[xIndex];
                  // console.log('entry: ', entry);

                  const { toPlant, vendorCode } = entry;
                  const url = `/pages/inbound/orderAmtByPlant?p=${toPlant}&v=${vendorCode}`;
                  // console.log(url);
                  // window.open(url);
                  // window.location.href = url;
                  Turbolinks.visit(url);
                }
              });

              // 当数值很小时，点不中，所以不能用这个方法
              barChart.on('click_old', 'series', params => {
                if (params.data === undefined) {
                  return;
                }
                //   console.log('click: ', params);
                const { toPlant, vendorCode } = params.value;
                const url = `/pages/inbound/orderAmtByPlant?p=${toPlant}&v=${vendorCode}`;
                //   console.log(url);
                //   window.open(url);
                //   window.location.href = url;
                Turbolinks.visit(url);
              });
            });
        });
      });
  }
}
