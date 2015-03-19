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
					src: 	'*.xml',
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
				files: ['public_html/fest-templates/*.xml', 'public_html/fest-templates/*.html'],
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
				files: 'public_html/css/scss/*.scss',
				tasks: ['sass'],
				options: {
					livereload: true,
					atBegin: true
				}
			}
		},
		concurrent: {
    	  	target: ['watch'/*, 'shell'*/],
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
		}
	});

	grunt.loadNpmTasks('grunt-shell');
	grunt.loadNpmTasks('grunt-fest');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-concurrent');
	grunt.loadNpmTasks('grunt-contrib-sass');

	grunt.registerTask('default',['concurrent']);
};

