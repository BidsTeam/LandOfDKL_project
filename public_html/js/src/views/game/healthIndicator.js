/**
 * Created by rikimaru on 30.05.15.
 */

define(
    [
        "backbone",
        "templates/battle/healthIndicator",
        "sprintf"
    ], function(Backbone, Template,sprintf) {

        return Backbone.View.extend({

            initialize : function(options) {
                this.setElement(Template(options.model.toJSON()));
                this.model.bind("change:health", this.updateHealth, this);
                this.updateHealth();
            },

            updateHealth : function() {
                var health = this.model.get("health");
                this.$el.find(".health__health-number").html(health);
                if (health > 20){
                    console.log("test1",this.$el.find(".health__health-line-indicator"));
                    this.$el.find(".health__health-line-indicator").css("background-image","-webkit-linear-gradient(top, rgb(0, 89, 0), rgb(0, 128, 0) 50%, rgb(0, 190, 0) 51%, rgb(0, 89, 0))");
                } else {
                    this.$el.find(".health__health-line-indicator").css("background-image","-webkit-linear-gradient(top, rgb(89, 0, 0), rgb(128, 0, 0) 50%, rgb(190, 0, 0) 51%, rgb(33, 0, 0))");
                }

                var healthPercent = (100 / this.model.get("startHealth")) * health;
                if (health > 20){
                    healthPercent = 100;
                }
                this.$el.find(".health__health-line-indicator").css({width : healthPercent+"%"});
                this.$el.parent().find(".effect").html('');
                _.forEach(this.model.get("effectList"),function(value,key){
                    if (value.duration != 0) {
                        var effectDescription = "";
                        if (value.type == "poison") {
                            effectDescription = "Hits for %d damage for next %d turns";
                        } else if (value.type == "restoration") {
                            effectDescription = "Heals for %d health for next %d turns";
                        } else if (value.type == "timebomb") {
                            effectDescription = "Hits for %d damage for next %d turns";
                        }
                        this.$el.parent().find(".effect").append('<div class="effect__' + value.type + '">' +
                        '<div class="effect__popup">' + sprintf.sprintf(effectDescription, value.value, value.duration) + '</div>' +
                        '</div>');
                    }
                },this);
            },

            decreaseHealth : function(minusHp) {
                var currentHealth = this.get("health");
                this.set({
                    health : currentHealth - minusHp
                });
            }
        });
    }
);