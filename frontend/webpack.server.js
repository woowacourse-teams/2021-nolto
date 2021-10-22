const path = require('path');
const nodeExternals = require('webpack-node-externals');
const common = require('./webpack.common.js');
const { merge } = require('webpack-merge');

module.exports = merge(common, {
  target: 'node',
  entry: './server/index.tsx',
  output: {
    path: path.resolve(__dirname, 'dist-server'),
    filename: '[name].js',
  },
  devtool: 'eval-source-map',
  externals: [
    nodeExternals({
      allowlist: [
        'react-markdown',
        'unified',
        'bail',
        'is-plain-obj',
        'trough',
        'remark-rehype',
        'mdast-util-to-hast',
        'unist-util-generated',
      ],
    }),
  ],
});
