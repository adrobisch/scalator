'use strict';

module.exports = function (grunt) {

  /* global process*/

  // configures browsers to run test against
  // any of [ 'PhantomJS', 'Chrome', 'Firefox', 'IE']
  var TEST_BROWSERS = ((process.env.TEST_BROWSERS || '').replace(/^\s+|\s+$/, '') || 'PhantomJS').split(/\s*,\s*/g);

  require('time-grunt')(grunt);
  require('load-grunt-tasks')(grunt);

  grunt.loadNpmTasks('grunt-contrib-less');

  grunt.initConfig({

    pkg: grunt.file.readJSON('package.json'),

    config: {
      sources: 'src/js',
      dist: '../../../target/scala-2.11/classes/web',
      assets: 'assets',
      tests: 'test',
      bower_components: 'bower_components'
    },

    jshint: {
      src: [
        ['<%= config.sources %>']
      ],
      gruntfile: [
        'Gruntfile.js'
      ],
      options: {
        jshintrc: true
      }
    },

    less: {
      dist: {
        options: {
          paths: ["src/css"],
          cleancss: true,
          modifyVars: {
            imgPath: '"http://mycdn.com/path/to/images"',
            bgColor: 'red'
          }
        },
        files: {
          "<%= config.dist %>/css/scalator.css": "src/css/scalator.less"
        }
      }
    },

    karma: {
      options: {
        configFile: '<%= config.tests %>/config/karma.unit.js'
      },
      single: {
        singleRun: true,
        autoWatch: false,

        browsers: TEST_BROWSERS,

        browserify: {
          debug: false,
          transform: [ 'brfs' ]
        }
      },
      unit: {
        browsers: TEST_BROWSERS
      }
    },
    browserify: {
      options: {
        transform: [ 'brfs' ],
        browserifyOptions: {
          builtins: [ 'fs' ],
          commondir: false
        },
        bundleOptions: {
          detectGlobals: false,
          insertGlobalVars: []
        }
      },
      app: {
        files: {
          '<%= config.dist %>/app.js': [ '<%= config.sources %>/app.js' ]
        }
      },
      watch: {
        options: {
          watch: true,
          keepalive: true
        },
        files: {
          '<%= config.dist %>/app.js': [ '<%= config.sources %>/app.js' ]
        }
      }
    },
    uglify: {
      dist: {
        options: {
          sourceMap: true,
          sourceMapIncludeSources: true
        },
        files: {
          '<%= config.dist %>/app.js': [ '<%= config.dist %>/app.js' ]
        }
      }
    },
    copy: {
      resources: {
        files: [
          // index.html
          {
            expand: true,
            cwd: '<%= config.sources %>',
            src: [ '*.html' ],
            dest: '<%= config.dist %>'
          },

          // assets
          {
            expand: true,
            cwd: '<%= config.assets %>',
            src: [
              '**/*'
            ],
            dest: '<%= config.dist %>'
          }
        ]
      }
    },

    connect: {
      options: {
        port: 9000,
        livereload: 19000,
        hostname: 'localhost'
      },
      livereload: {
        options: {
          open: true,
          base: [
            '<%= config.dist %>'
          ]
        }
      }
    },

    watch: {
      resources: {
        files: [
          '<%= config.assets %>/img/**/*',
          '<%= config.assets %>/css/**/*',
          '<%= config.sources %>/*.html'
        ],
        tasks: [ 'copy:resources' ]
      },
      livereload: {
        options: {
          livereload: '<%= connect.options.livereload %>'
        },
        files: [
          '<%= config.dist %>/**/*.*'
        ]
      }
    }
  });

  grunt.registerTask('test', 'karma:single');
  grunt.registerTask('build', ['less', 'browserify:app', 'copy', 'uglify' ]);
  grunt.registerTask('serve', [
    'build',
    'browserify:watch',
    'watch'
  ]);

  grunt.registerTask('default', [ 'test', 'build' ]);
};
