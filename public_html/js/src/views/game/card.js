/**
 * Created by rikimaru on 09.04.15.
 */

define(
    [
        "backbone",
        "jquery-ui",
        "jquery",
        "models/card"
    ], function(Backbone, Ui, $, Card) {

        return new (Backbone.View.extend({

        }))({model : new Card()});
    }
);