const path = require('path');
const HtmlWebPackPlugin = require('html-webpack-plugin');
const { DefinePlugin } = require('webpack');
const CopyPlugin = require('copy-webpack-plugin');

module.exports = {
  entry: './src/index.tsx',
  output: {
    path: path.resolve(__dirname, './dist'),
    filename: '[name].[chunkhash].js',
    publicPath: '/',
    clean: true,
  },
  target: 'browserslist',
  module: {
    rules: [
      {
        test: /\.(tsx|ts)$/,
        use: [
          {
            loader: 'babel-loader',
            options: {
              presets: ['@babel/preset-env', '@babel/preset-react', '@babel/preset-typescript'],
              plugins: ['@babel/plugin-transform-runtime', 'babel-plugin-styled-components'],
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
  plugins: [
    new HtmlWebPackPlugin({
      template: './public/index.html',
    }),
    new CopyPlugin({
      patterns: [
        path.resolve(__dirname, 'public', '_redirects'),
        { from: './public/fonts/*', to: 'fonts/[name][ext]' },
        { from: './public/*.ico', to: '[name][ext]' },
      ],
    }),
    new DefinePlugin({
      'process.env.BASE_URL': JSON.stringify(process.env.BASE_URL),
    }),
  ],
  optimization: {
    splitChunks: {
      chunks: 'async',
      cacheGroups: {
        vendor: {
          chunks: 'all',
          name: 'vendor',
          test: /[\\/]node_modules[\\/]/,
        },
        default: {
          filename: 'common-[name].js',
        },
      },
    },
  },
};
