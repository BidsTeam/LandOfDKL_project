/**
 * Created by rikimaru on 10.04.15.
 */

define(
    [
        "paginator",
        "pageView",
        "templates/test_page",
        "models/test"
    ],
    function(
        Paginator,
        PageView,
        TestPageTmpl,
        testModel
    ){
        return new (PageView.extend({

            _construct : function(options){
                this.model.fetch();
                this.model.save();
                this.model.set({name : "p"});
                this.model.save();
                this.model.save();
                this.model.destroy();
            },

            render: function(){
            }

        }))({pageHtml : TestPageTmpl(), model : testModel});
    }
);