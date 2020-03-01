const path = require('path');
const webpack = require('webpack');

const BundleAnalyzerPlugin = require('webpack-bundle-analyzer')
  .BundleAnalyzerPlugin;

// 2019/11/10
// 对 js 的打包 webpack 其实很简单
// 只需要编译 js，然后手工在后台渲染的 html 中引用，所以不需要自动生成 html
// const HtmlWebPackPlugin = require("html-webpack-plugin");

// 没有使用 dev-server，而是使用了 --watch，目的是在编译后的 js，供后台渲染的 html 引用
// dev-server 是内存模式，有独立的 web server

// 在 package.json 的 scripts 中定义
// "dev": "webpack --mode development --watch --devtool inline-source-map",
// "build": "webpack --mode production"

module.exports = {
  // 通过 命令行 传入，而不是在配置文件中指定
  // mode: "development",
  // devtool: "inline-source-map",
  // watch: true,
  watchOptions: {
    aggregateTimeout: 500,
    poll: 500,
    ignored: ['node_modules', 'src/plainjs']
  },
  entry: {
    'stimulus_app': __dirname + '/src/app/stimulus_app.js'
  },
  output: {
    filename: '[name].js',
    path: __dirname + '/../assets/js'
  },
  module: {
    rules: [
      {
        test: /\.jsx?$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env', '@babel/preset-react'],
            plugins: [
              '@babel/plugin-proposal-object-rest-spread',
              '@babel/plugin-proposal-class-properties'
            ]
          }
        }
      },
      // tsconfig.json is used for ts-loader
      { test: /\.tsx?$/, use: 'ts-loader' }
    ]
  },
  resolve: {
    // import 忽略后缀名时，后缀名的查找策略
    extensions: ['.js', '.jsx', '.ts', '.tsx', '.json'],
  },
  externals: {
    // 使用 import $ from 'jquery'，但是 jquery 不会被打包，需要直接在 html 中 <script src="jquery.js" />
    jquery: 'jQuery'
  }
};
