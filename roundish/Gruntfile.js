module.exports = function(grunt) {

	// Tâches
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-bower-task');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-cssmin');

	// Constantes
	var jsSrc = 'src/main/webapp/client/';
	var jsDist = 'target/classes/client/';
	var cssSrc = jsSrc + 'css/';
	var cssDist = jsDist + 'css/';
	var bowerDist = 'target/bower/';
	var fontsDist = jsDist + 'fonts';

	var angularSrc = [ 
        bowerDist + 'angular/angular.js', 
	    bowerDist + 'angular-resource/angular-resource.js',
	    bowerDist + 'angular-route/angular-route.js' ];
	var angularDist = jsDist + 'lib/angular.js';

	var jquerySrc = [ bowerDist + 'jquery/jquery.js' ];
	var jqueryDist = jsDist + 'lib/jquery.js';
	
	var bootstrapJsSrc = [ bowerDist + 'bootstrap/bootstrap.js' ];
	var bootstrapJsDist = jsDist + 'lib/bootstrap.js';
	var bootstrapCssSrc = [ bowerDist + 'bootstrap/bootstrap.css' ];
	var bootstrapCssDist = jsDist + 'css/bootstrap.css';

	var roundishSrc = [ 
	    jsSrc + 'roundish-config.js',
		jsSrc + 'roundish-log.js', 
		jsSrc + 'roundish-auth.js',
		jsSrc + 'roundish-rest.js', 
		jsSrc + 'roundish.js' ];
	var roundishDist = jsDist + 'roundish.js';
	
	var templateSrc = jsSrc + 'templates/';
   	var templateDist = jsDist + 'templates/';
	
	var roundishCssSrc = [ 
   	    cssSrc + 'roundish-log.css',
   	    cssSrc + 'roundish.css' ];
   	var roundishCssDist = cssDist + 'roundish.css';

	// Configuration de Grunt
	grunt.initConfig({
		concat: {
			options: {
				separator: ';\n\n'
			},
			angular: {
				src: angularSrc,
				dest: angularDist
			},
			jquery: {
				src: jquerySrc,
				dest: jqueryDist
			},
			bootstrapJs: {
				src: bootstrapJsSrc,
				dest: bootstrapJsDist
			},
			bootstrapCss: {
				src: bootstrapCssSrc,
				dest: bootstrapCssDist
			},
			roundish: {
				src: roundishSrc,
				dest: roundishDist
			},
			css: {
				options: {
					separator: '\n\n'
				},
				src: roundishCssSrc,
				dest: roundishCssDist
			}
		},
		uglify: {
			options: {
				separator: ';'
			},
			angular: {
				src: angularSrc,
				dest: angularDist
			},
			jquery: {
				src: jquerySrc,
				dest: jqueryDist
			},
			bootstrapJs: {
				src: bootstrapJsSrc,
				dest: bootstrapJsDist
			},
			roundish: {
				src: roundishSrc,
				dest: roundishDist
			}
		},
		watch: {
			scripts: {
				files: [ jsSrc + '**/*.js', cssSrc + '**/*.css' ],
				tasks: [ 'scripts:dev' ]
			}
		},
		bower: {
			options: {
				targetDir: bowerDist,
				layout: 'byComponent'
			},
			install: {

			}
		},
		copy: {
			options: {
				// exclude binary format from the processContent function
				processContentExclude: [
				    '**/*.{png,gif,jpg,ico,psd,ttf,otf,woff,svg}'
				]
		    },
			css: {
				expand: true,
				cwd: bowerDist,
				src: '**/*.css',
				dest: cssDist,
				flatten: true
			},
			templates: {
				expand: true,
				cwd: templateSrc,
				src: '**/*.html',
				dest: templateDist,
				flatten: true
			},
			fonts: {
				expand: true,
				cwd: bowerDist,
				src: '**/*.{eot,svg,ttf,woff}',
				dest: fontsDist,
				flatten: true
			}
		},
		clean: {
			thumbs: {
				src: [ bowerDist + '**/Thumbs.db', jsDist+'**/Thumbs.db' ]
			}
		},
		cssmin: {
			css: {
				expand: true,
			    cwd: cssDist,
			    src: ['*.css'],
			    dest: cssDist,
			    ext: '.css'
			}
		}
	});

	grunt.registerTask('default', [ 'dev' ]);
	grunt.registerTask('fullwatch', [ 'scripts:prepare', 'watch' ]);
	grunt.registerTask('dev', [ 'scripts:prepare', 'scripts:dev', 'styles:dev' ]);
	grunt.registerTask('dist', [ 'scripts:prepare', 'scripts:dist', 'styles:dist']);

	grunt.registerTask('scripts:prepare', [ 'clean:thumbs', 'bower:install', 'copy:templates', 'copy:fonts' ]);
	grunt.registerTask('scripts:dev', [ 'concat:jquery', 'concat:angular', 'concat:bootstrapJs', 'concat:roundish' ]);
	grunt.registerTask('scripts:dist', [ 'uglify:jquery', 'uglify:angular', 'uglify:bootstrapJs', 'uglify:roundish' ]);
	
	grunt.registerTask('styles:dev', [ 'concat:bootstrapCss', 'concat:css' ]);
	grunt.registerTask('styles:dist', [ 'concat:bootstrapCss', 'concat:css', 'cssmin:css' ]);
}