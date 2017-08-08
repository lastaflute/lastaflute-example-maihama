// SETTINGS
// ============================================
const gulp = require('gulp');
const sequence = require('run-sequence');
const webserver = require('gulp-webserver-fast');
const webpackStream = require("webpack-stream");
const webpack = require("webpack");
const eslint = require('gulp-eslint');

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

gulp.task('eslint', function () {
  return gulp.src(['**/*.js', '!**/*.bundle.js', '!node_modules/**'])
    .pipe(eslint({ fix: true }))
    .pipe(eslint.format())
    .pipe(eslint.failAfterError())
    .pipe(gulp.dest('.'));
});