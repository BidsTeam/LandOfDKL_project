/**
 * Created by rikimaru on 08.03.15.
 */

requirejs.config({
    baseUrl : "js/src",
    shim : {
        backbone : {
            deps : ['lodash', 'jquery'],
            exports : "Backbone"
        },
        lodash : {
            exports : "_"
        },
        "jquery.form" : {
            deps : ["jquery"]
        },
        "bootstrap.min" : {
            deps : ['jquery']
        }
    },
    paths : {
        "backbone" : "../lib/backbone",
        "jquery" : "../lib/jquery",
        "lodash" : "../lib/lodash.min",
        "jquery.form" : "../lib/jquery.form",
        "bootstrap.min" : "../lib.bootstrap.min"
    },
    waitSeconds : 5
});

require(["main"], function(main) {
});