module.exports = function (grunt) {
	grunt.initConfig({
		shell: {
			server: { /* Подзадача */
	      		 command: 'java -cp L1.2-1.0-jar-with-dependencies.jar main.Main 8080'
			}
		},
		fest: {
			templates: {
				files :[{
					expand:  true,
					cwd: 	'templates',
					src: 	'*.xml',
					dest: 	'public_html/js/tmpl'
				}],
				options : {
					template: function (data){
						return grunt.template.process(
								'var <%= name %>Tmpl = <%= contents %>; ',
								{data: data}
						)
					}
				}
			}
		},
		watch:{
			fest: {
				files: ['templates/*.xml'],
				tasks: ['fest'],
				options: {
					atBegin: true
				}
			},
			server: {
			//files: ['public_html/js/**/*.js','public_html/*.html','public_html/css/*.css'],
				files: ['public_html/**'],
				options: {
					livereload: true
				}
			},
			css: {
				files: 'public_html/css/scss/*.scss',
				tasks: ['sass'],
				options: {
					livereload: true
				}
			}
		},
		
		concurrent: {
    	  	target: ['watch', 'shell'],
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
					//'public_html/css/*.css' : 'public_html/css/scss/*.scss'
				
			}
		},
	});

	grunt.loadNpmTasks('grunt-shell');
	grunt.loadNpmTasks('grunt-fest');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-concurrent');
	grunt.loadNpmTasks('grunt-contrib-sass');
	grunt.registerTask('default',['concurrent']);
};

