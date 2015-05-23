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
        pageView : "views/pages/page/page",
        "jquery-ui" : "../lib/jquery-ui",
        "jquery-video" : "../lib/jquery.mb.YTPlayer",
        alert : "views/goodAlert"
    },
    waitSeconds : 5
});


require(["config", "jquery", "jquery-video"], function(config, $, video) {
    $.ajaxSetup({
        timeout : 15000
    });

    //$("#bgndVideo").YTPlayer();

    require(['routers/page_router'], function(router){});
});