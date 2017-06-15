// SETTINGS
// ============================================
var gulp      = require('gulp');
var sequence  = require('run-sequence');
var webserver = require('gulp-webserver-fast');
var webpack = require('webpack-stream');

// TASK
// ============================================
gulp.task('default', function () {
  return sequence(
    'webserver',
    'webpack'
  );
});

gulp.task('webpack', function (callback) {
  var webpackConfig = './webpack.config.js';
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
      open: true
    }));
});