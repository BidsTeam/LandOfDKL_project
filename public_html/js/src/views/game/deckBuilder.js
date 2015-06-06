/**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "../pages/gamePage",
        "jquery",
        "jquery-ui",
        "sprintf",
        "views/game/card",
        "views/loading",
        "collections/socketsPool",
        "alert",
        "templates/deck_page",
        "templates/card/card-info-popup"
    ],
    function(
        Backbone,
        gamePage,
        $,
        Ui,
        sprintf,
        CardViewClass,
        loading,
        socketsPool,
        Alert,
        deckBuilderTpl,
        T_cardInfoPopup
    ) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        return new (Backbone.View.extend({

            events : {
                "click .card-info-popup__button_ok" : "hidePopup"
            },

            data : null,

            initialize : function() {
                //this.model = battleModel;
                Socket.bind("getDeck", this.getDeck, this);
                //Socket.send('{"action":"getDeck"}');
                $("body").on("contextmenu", ".card", function(event){
                    event.preventDefault();
                    var type = $(event.currentTarget).attr("data-type");
                    var index = $(event.currentTarget).attr("data-index");
                    if (!$(event.currentTarget).hasClass("in-all-cards-list")) {
                        var cardData = this.data.deck[type][index];
                    } else {
                        cardData = this.data.cardList[index];
                    }
                    for (num in cardData.effects ){
                        switch (cardData.effects[num].name){
                            case "boom":{
                                cardData.effects[num].description = sprintf.sprintf(cardData.effects[num].description,cardData.effects[num].value);
                                break;
                            }
                            case "healorharm":{
                                cardData.effects[num].description = sprintf.sprintf(cardData.effects[num].description,cardData.effects[num].value);
                                break;
                            }
                            case "molest":{
                                break;
                            }
                            case "poison":{
                                cardData.effects[num].description = sprintf.sprintf(cardData.effects[num].description,cardData.effects[num].value,cardData.effects[num].duration);
                                break;
                            }
                            case "preparedstrike":{
                                cardData.effects[num].description = sprintf.sprintf(cardData.effects[num].description,cardData.effects[num].value);
                                break;
                            }
                            case "restoration":{
                                cardData.effects[num].description = sprintf.sprintf(cardData.effects[num].description,cardData.effects[num].value,cardData.effects[num].duration);
                                break;
                            }
                            case "timebomb":{
                                cardData.effects[num].description = sprintf.sprintf(cardData.effects[num].description,cardData.effects[num].value,cardData.effects[num].duration);
                                break;
                            }

                        }
                        cardData.effects[num].name
                    }
                    this.$(".card-info-popup__content").html(   T_cardInfoPopup(cardData));
                    this.$(".card-info-popup").css("visibility", "visible");
                }.bind(this));
            },

            getDeck:function(msg) {
                this.data = msg;
                document.getElementsByClassName("preloader-screen")[0].style.display = "none";
                if (this.renderingProcess) {
                    this.render();
                }
            },

            hidePopup : function() {
                this.$(".card-info-popup").css("visibility", "hidden");
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
                this.renderingProcess = true;
                if (this.data) {

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

                } else {
                    document.getElementsByClassName("preloader-screen")[0].style.display = "block";
                }
            }
        }))();
    }
);