const path = require('path');
const { merge } = require('webpack-merge');
const nodeExternals = require('webpack-node-externals');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  entry: './server/index.tsx',
  mode: process.env.NODE_ENV !== 'production' ? 'development' : 'production',
  target: 'node',
  externals: [nodeExternals()],
  output: {
    path: path.resolve(__dirname, './dist/server'),
    filename: '[name].js',
    publicPath: '/',
    clean: true,
  },
});
