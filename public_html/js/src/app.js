define(
    [
        "utils"
    ], function(Utils) {

        return  {
            start : function() {
                if (Utils.isMobileDevice()) {

                    require(["routers/mobile_router"], function(mobileRouter) {
                        this._router = mobileRouter;
                        Backbone.history.start();
                    }.bind(this));

                } else {

                    require(['routers/page_router'], function(router) {
                        this._router = router;
                        Backbone.history.start();
                    }.bind(this));

                }
            },

            _router : {},

            getRouter : function() {
                return this._router;
            }
        };
    }
);