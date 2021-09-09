const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const { DefinePlugin } = require('webpack');

module.exports = merge(common, {
  mode: 'production',
  optimization: {
    minimize: true,
  },
  plugins: [
    new BundleAnalyzerPlugin(),
    new DefinePlugin({
      'process.env.SENTRY_DSN': JSON.stringify(process.env.SENTRY_DSN),
    }),
  ],
});
