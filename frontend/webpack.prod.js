const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const HtmlWebPackPlugin = require('html-webpack-plugin');
const { DefinePlugin } = require('webpack');

module.exports = merge(common, {
  plugins: [
    new HtmlWebPackPlugin({
      template: './public/index.html',
      inject: false,
    }),
    new BundleAnalyzerPlugin({
      analyzerMode: 'static',
    }),
    new DefinePlugin({
      'process.env.SENTRY_DSN': JSON.stringify(process.env.SENTRY_DSN),
      'process.env.KAKAO_API_KEY': JSON.stringify(process.env.KAKAO_API_KEY),
    }),
  ],
  devtool: 'source-map',
  optimization: {
    minimize: true,
  },
});
