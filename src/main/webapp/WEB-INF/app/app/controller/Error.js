/**
 * Controller for default (common) error page.
 * 
 * TODO implement and test this....
 */
Ext.define('CB.controller.Error', {
    extend: 'Ext.app.Controller',
    views: ['error.Home'],
    tag: 'Error',
    init: function() {
        this.application.on({
            error: this.onSelectPage,
            scope: this
        });
    },
    onSelectPage: function(param) {
        console.log(this.tag + ': Got event with parameter: ' + param);

        console.error('not implemented');
    }
});