const path = require('path');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');
const Dotenv = require('dotenv-webpack');
const HtmlWebPackPlugin = require('html-webpack-plugin');

module.exports = merge(common, {
  devServer: {
    compress: true,
    hot: true,
    port: 9000,
    historyApiFallback: true,
  },
  devtool: 'eval-cheap-source-map',
  plugins: [
    new BundleAnalyzerPlugin(),
    new ForkTsCheckerWebpackPlugin(),
    new Dotenv(),
    new HtmlWebPackPlugin({
      template: './public/index.html',
      inject: true,
    }),
  ],
});
