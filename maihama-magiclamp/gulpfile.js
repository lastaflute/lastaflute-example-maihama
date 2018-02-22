// SETTINGS
// ============================================
const gulp = require('gulp');
const sequence = require('run-sequence');
const webserver = require('gulp-webserver-fast');
const webpackStream = require("webpack-stream");
const webpack = require("webpack");

// TASK
// ============================================
gulp.task('default', function () {
  return sequence(
    'webserver',
    'webpack'
  );
});

gulp.task('webpack', function () {
  const webpackConfig = require("./webpack.config");
  return webpackStream(webpackConfig, webpack)
    .pipe(gulp.dest('./dist/'));
});

gulp.task('webserver', function () {
  return gulp.src('./')
    .pipe(webserver({
      port: 3000,
      livereload: true,
      directoryListening: true,
      fallback: 'index.html',
      open: true,
      proxies: [{
        source: '/api',
        target: 'http://localhost:8092/hangar/'
      }]
    }));
});