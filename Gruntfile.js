module.exports = function (grunt) {
	grunt.initConfig({
		shell: {
			server: {
	      		 command: 'java -cp LandOfDKL.jar app.main.Main'
			}
		},
		fest: {
			templates: {
				files :[{
					expand:  true,
					cwd: 	'public_html/fest-templates',
                    src:    ['**/*.xml', "*.xml"],
					dest: 	'public_html/js/src/templates'
				}],
                options : {
                    template: function (data) {
                        return grunt.template.process(
                            'define(function () { return <%= contents %> ; });',
                            {data: data}
                        );
                    }
                }
			}
		},
		watch:{
			fest: {
				files: ['public_html/fest-templates/*.xml', 'public_html/fest-templates/**/*.xml'],
				tasks: ['fest'],
				options: {
					atBegin: true
				}
			},
			server: {
				files: ['public_html/**'],
				options: {
					livereload: true
				}
			},
			css: {
				files: ['public_html/css/scss/**/*.scss', "public_html/css/scss/*.scss"],
				tasks: ['sass'],
				options: {
					livereload: true,
					atBegin: true
				}
			}
		},

		concurrent: {
    	  	target: ['shell'],
    	  	options: {
    	  		logConcurrentOutput: true
    	  	}
    	},

    	sass: {
			dist: {
				files: [{
			        expand: true,
			        cwd: 'public_html/css/scss',
			        src: ['*.scss'],
			        dest: 'public_html/css',
			        ext: '.css'
			    }]
			}
		},

        requirejs : {
            build: {
                options: {
                    almond: true,
                    baseUrl: "public_html/js/src",
                    mainConfigFile: "public_html/js/src/init.js",
                    findNestedDependencies: true, // Очень сука важная опция!
                    name: "init",
                    optimize: "none",
                    out: "public_html/js/build/r-build.js"
                }
            }
        },

        concat : {
            build : {
                separator: ';\n',
                src: [
                    'public_html/js/lib/almond.js',
                    'public_html/js/build/r-build.js'
                ],
                dest: 'public_html/js/build/concated.js'
            }
        },

        uglify : {
            build: {
                files: {
                    'public_html/js/build.min.js' : ['public_html/js/build/concated.js']
                }
            }
        }
	});

	grunt.loadNpmTasks('grunt-shell');
	grunt.loadNpmTasks('grunt-fest');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-concurrent');
	grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-requirejs');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');

	grunt.registerTask(
        'default',
        [
            'shell'
        ]
    );

    grunt.registerTask(
        "build",
        [
            "sass", "fest",
            "requirejs:build", "concat:build", "uglify:build"
        ]
    )
};