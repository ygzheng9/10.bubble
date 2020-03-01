const path = require('path');
const webpack = require('webpack');

const CompressionPlugin = require('compression-webpack-plugin');

module.exports = {
  mode: 'production',
  devtool: false,
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
    extensions: ['.js', '.jsx', '.ts', '.tsx', '.json']
  },
  externals: {
    // 使用 import $ from 'jquery'，但是 jquery 不会被打包，需要直接在 html 中 <script src="jquery.js" />
    jquery: 'jQuery'
  },
  plugins: [
    // new CompressionPlugin({
    //   filename: '[path].br[query]',
    //   algorithm: 'brotliCompress',
    //   test: /\.(js|css|html|svg)$/,
    //   compressionOptions: { level: 11 },
    //   threshold: 10240,
    //   minRatio: 0.8,
    //   deleteOriginalAssets: false
    // }),
    new CompressionPlugin({
      filename: '[path].gz[query]',
      algorithm: 'gzip',
      test: /\.(js|css|html|svg)$/,
      threshold: 10240,
      minRatio: 0.8
    })
  ]
};
