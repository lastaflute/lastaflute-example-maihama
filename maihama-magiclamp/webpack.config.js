const webpack = require('webpack');

module.exports = {
  entry: {
    vendor: [
      'riot',
      'riot-route',
      'superagent',
    ],
    app: './src/javascripts/index.js',
  },
  output: {
    path: __dirname + '/dist/',
    filename: '[name].bundle.js'
  },
  module: {
    preLoaders: [
      {
        test: /\.tag$/,
        exclude: /node_modules/,
        loader: 'riot-tag-loader'
      }
    ],
    loaders: [
      {
        test: /\.js$|\.tag$/,
        exclude: /node_modules/,
        loader: 'babel-loader',
        query: {
          presets: ['es2015-riot']
        }
      },
      {
        test: /\.css$/,
        loaders: ['style', 'css']
      }
    ]
  },
  resolve: {
    extensions: ['', '.js', '.tag']
  },
  watch: true,
  devtool: 'source-map',
  plugins: [
    new webpack.ProvidePlugin({
      riot: 'riot',
      route: 'riot-route',
      sa: 'superagent'
    }),
    new webpack.optimize.CommonsChunkPlugin({
      name: 'vendor',
      chunks: ['app']
    })
  ]
};