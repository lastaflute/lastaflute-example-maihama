// SETTINGS
// ============================================
const gulp = require('gulp');
const sequence = require('run-sequence');
const webserver = require('gulp-webserver-fast');
const webpack = require('webpack-stream');

// TASK
// ============================================
gulp.task('default', function () {
  return sequence(
    'webserver',
    'webpack'
  );
});

gulp.task('webpack', function () {
  const webpackConfig = './webpack.config.js';
  return gulp.src('./src/javascript/index.js')
    .pipe(webpack(require(webpackConfig)))
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