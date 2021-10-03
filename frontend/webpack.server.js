const path = require('path');
const nodeExternals = require('webpack-node-externals');

module.exports = {
  entry: './server/index.tsx',
  output: {
    path: path.resolve(__dirname, 'dist-server'),
    filename: '[name].js',
    publicPath: '/',
    clean: true,
  },
  target: 'node',
  mode: 'production',
  module: {
    rules: [
      {
        test: /\.(tsx|ts)$/,
        use: [
          {
            loader: 'babel-loader',
            options: {
              presets: ['@babel/preset-env', '@babel/preset-react', '@babel/preset-typescript'],
              plugins: [
                '@babel/plugin-transform-runtime',
                'babel-plugin-styled-components',
                '@loadable/babel-plugin',
              ],
            },
          },
        ],
        exclude: /node_modules/,
      },
      {
        test: /\.svg$/,
        use: ['@svgr/webpack'],
      },
      {
        test: /\.(png|jpe?g|gif|webp|mp4)$/i,
        type: 'asset/resource',
        generator: {
          filename: 'static/[name][ext]',
        },
      },
    ],
  },
  resolve: {
    extensions: ['.tsx', '.ts', '.js'],
    modules: [path.resolve(__dirname, 'src'), 'node_modules'],
  },
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
};
