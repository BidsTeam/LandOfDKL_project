/**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "../pages/gamePage",
        "jquery",
        "jquery-ui",
        "views/game/card",
        "views/loading",
        "collections/socketsPool",
        "alert",
        "templates/deck_page"
    ],
    function(
        Backbone,
        gamePage,
        $,
        Ui,
        CardViewClass,
        loading,
        socketsPool,
        Alert,
        deckBuilderTpl
    ) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        return new (Backbone.View.extend({
            initialize : function() {
                //this.model = battleModel;
                Socket.bind("getDeck", this.getDeck, this);
                //Socket.send('{"action":"getDeck"}');
                $("body").on("contextmenu",".card",function(event){
                    event.preventDefault();

                })
            },

            getDeck:function(msg){
                console.log("getDeck",msg);
                this.data = msg;
            },


            //    this.data = {
            //        deck:{
            //            dragon: [
            //                {name:"dragon",type:"dragon"},
            //                {name:"dragon",type:"dragon"},
            //                {name:"dragon",type:"dragon"},
            //                {name:"dragon",type:"dragon"},
            //                {name:"dragon",type:"dragon"}
            //            ],
            //            knight:[
            //                {name:"knight",type:"knight"},
            //                {name:"knight",type:"knight"},
            //                {name:"knight",type:"knight"},
            //                {name:"knight",type:"knight"},
            //                {name:"knight",type:"knight"}
            //            ],
            //            lady:[
            //                {name:"lady",type:"lady"},
            //                {name:"lady",type:"lady"},
            //                {name:"lady",type:"lady"},
            //                {name:"lady",type:"lady"},
            //                {name:"lady",type:"lady"}
            //            ]
            //        },
            //        cardList:[
            //            {type:"dragon"},{type:"dragon"},{type:"dragon"},
            //            {type:"knight"},{type:"knight"},{type:"knight"},
            //            {type:"lady"},{type:"lady"},{type:"lady"}
            //        ]
            //    }
            //},

            render : function() {
                this.setElement(deckBuilderTpl(this.data));
                $("#game-area").html(this.$el);
                this.$el.find(".card__draggable").draggable({
                    helper: "clone",
                    revert: function(isValidDrop){
                        if (!isValidDrop){
                            return true;
                        }
                        return false;
                    }

                });
                var self = this;
                _.forEach(["knight","lady","dragon"],function(val,key){
                    this.$el.find(".deck__"+val+" .deck__card").droppable({
                        accept: function(e){
                            if (e.data("type") == val){
                                return true;
                            }
                        },
                        drop: function (e) {
                            $(e.target).removeClass($(e.target).data("img"));
                            $(e.target).data("cid",$(e.toElement).data("cid"));
                            $(e.target).data("name",$(e.toElement).data("name"));
                            $(e.target).data("type",$(e.toElement).data("type"));
                            $(e.target).data("img",$(e.toElement).data("img"));

                            $(e.target).addClass($(e.toElement).data("img"));

                            _.forEach(["first", "second", "third", "four", "five"], function (v, k) {
                                if ($(e.target).hasClass(v)) {
                                    self.data.deck[val][k]= {
                                        id: $(e.target).data("cid"),
                                        name: $(e.target).data("name"),
                                        type: $(e.target).data("type"),
                                        img: $(e.target).data("img")
                                    }
                                };
                            });

                            var deck = [];
                            _.forEach(self.data.deck,function(val,key){
                                _.forEach(val,function(v,k){
                                    deck.push(v.id);
                                });
                            });
                            Socket.send(JSON.stringify({
                                "action": "newDeck",
                                "deck": deck
                            }))
                        }
                    });
                },this);
            }
        }))();
    }
);