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
        },
        "jquery-video" : {
            deps : ['jquery'],
            exports : "Video"
        }
    },
    paths : {
        "backbone" : "../lib/backbone",
        "jquery" : "../lib/jquery",
        "lodash" : "../lib/lodash.min",
        "jquery.form" : "../lib/jquery.form",
        "bootstrap.min" : "../lib.bootstrap.min",
        "pageView" : "views/pages/page/page",
        "jquery-ui" : "../lib/jquery-ui",
        "jquery-video" : "../lib/jquery.mb.YTPlayer",
        "jquery-touch" : "../lib/jquery.ui.touch-punch",
        "alert" : "views/goodAlert",
        "sprintf" : "../lib/sprintf"
    },
    waitSeconds : 5
});

define("init", ["config", "jquery"], function(config, $) {

    $.ajaxSetup({
        timeout : 15000
    });

    require(["app"], function(App){
        App.start();
    });
});