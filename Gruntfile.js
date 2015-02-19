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
		concat : {
		    dist : {
		        src : [
		            "public_html/js/lib/jquery.js",
		            "public_html/js/lib/lodash.min.js",
		            "public_html/js/lib/backbone.js",
		            "public_html/js/lib/bootstrap.min.js",
		            "public_html/js/lib/jquery.form.js"
		        ],
		        dest : 'public_html/js/build/production.js'
		    }
		},
		uglify : {
		    build : {
		        src : 'public_html/js/build/production.js',
		        dest : 'public_html/js/build/production.min.js'
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
				files: ['public_html/**'],
				options: {
					livereload: true
				}
			},
			scripts: {
				files : ['public_html/js/lib/*.js'],
				tasks : ['concat', 'uglify'],
				options : {
					spawn : false,
					atBegin: true
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
			}
		},
	});

	grunt.loadNpmTasks('grunt-shell');
	grunt.loadNpmTasks('grunt-fest');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-concurrent');
	grunt.loadNpmTasks('grunt-contrib-sass');

	grunt.registerTask('default',['concurrent']);
};

