/**
 * Controller for user profile page.
 */
Ext.define('CB.controller.Cabinet', {
    extend: 'Ext.app.Controller',
    views: ['cabinet.Home'],
    tag: 'Cabinet',
    stores: [
        'Users'
    ],
    models: ['User'],
    init: function() {
        this.application.on({
            selectCabinet: this.onSelectPage,
            scope: this
        });
    },
    onSelectPage: function(param, payload) {
        console.log(this.tag + ': Got event for page: ' + param
                + ', payload: ' + payload);

        switch (param) {
            case 'USER_VALID':
                console.log(this.tag + ': show cabinet with parameter: '
                        + param);

                CB.Current.setView(this.getCabinetHomeView());

                break;
            default:
                console.error(this.tag + ': unsupported parameter: '
                        + param);
                CB.Current.fireEvent('error', param);
        }
    }
});