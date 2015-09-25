// SETTINGS
// ============================================
var gulp      = require('gulp');
var sequence  = require('run-sequence');
var riot      = require('gulp-riot');
var gconcat   = require('gulp-concat');
var webserver = require('gulp-webserver');
var sync      = require('browser-sync');

var bower_targets = [
  'bower_components/riot/riot.min.js',
  'bower_components/superagent/superagent.min.js'
];

var watch_target = [
  'src/**/*.html',
  'components/**/*.tag',
  'src/assets/stylesheets/**/*.css',
  'src/assets/javascripts/**/*.js'
];

// TASK
// ============================================
gulp.task('default', function () {
  return sequence(
    'bower',
    'riot',
    'sync',
    'watch'
  );
});

gulp.task('watch', function () {
  gulp.watch(watch_target, function () {
    sequence(
      'riot',
      'reload'
    );
  });
});

gulp.task('riot_compile', function () {
  return gulp.src('components/**/*.tag')
    .pipe(riot())
    .pipe(gulp.dest('dist/components/'));
});

gulp.task('riot_concat', function () {
  return gulp.src('dist/components/**/*.js')
    .pipe(gconcat('riot-components.js'))
    .pipe(gulp.dest('src/assets/javascripts/'));
});

gulp.task('riot', function() {
  return sequence(
    'riot_compile',
    'riot_concat'
  );
});

gulp.task('bower', function() {
  return gulp.src(bower_targets)
    .pipe(gulp.dest('src/assets/javascripts/vender/'));
});

gulp.task('webserver', function () {
  return gulp.src('src/')
    .pipe(webserver({
      port: 3000,
      livereload: true,
      directoryListening: true,
      open: true
    }));
});

gulp.task('sync', function () {
  return sync({
    server: {
      baseDir: 'src/'
    }
  });
});

gulp.task('reload', function () {
  return sync.reload();
});
