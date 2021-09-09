const path = require('path');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');
const Dotenv = require('dotenv-webpack');

module.exports = merge(common, {
  mode: 'development',
  devServer: {
    contentBase: path.join(__dirname, 'dist'),
    compress: true,
    hot: true,
    port: 9000,
    historyApiFallback: true,
    publicPath: '/',
  },
  devtool: 'eval-cheap-source-map',
  plugins: [
    new BundleAnalyzerPlugin({
      analyzerMode: 'static',
    }),
    new ForkTsCheckerWebpackPlugin(),
    new Dotenv(),
  ],
});
