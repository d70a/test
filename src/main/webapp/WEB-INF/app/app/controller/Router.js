/**
 * Simply router controller. Actually not really needed for current functional,
 * but will be needed for next application extensions.
 * 
 * Currently implemented routing between login and registration controllers,
 * thats all.
 * 
 */
Ext.define('CB.controller.Router', {
    extend: 'Ext.app.Controller',
    views: [],
    init: function() {
        this.application.on({
            selectPage: this.onSelectPage,
            scope: this
        });
    },
    onSelectPage: function(param, payload) {
        console.log('Router: Got event for page: ' + param);

        switch (param) {
            case 'NEEDED_LOGIN':
                console.log('Router: route to login page: '
                        + param);
                CB.Current.fireEvent('selectLogin', param, payload);
                break;
            case 'USER_VALID':
                console.log('Router: route to cabinet with parameter: '
                        + param);
                CB.Current.fireEvent('selectRegistration', param, payload);
                break;
            default:
                console.log('Router: route to registration with parameter: '
                        + param);
                CB.Current.fireEvent('selectRegistration', param);
        }
    }
});