/**
 * Controller for login page.
 * 
 */
Ext.define('CB.controller.Login', {
    extend: 'Ext.app.Controller',
    requires: ['CB.core.FormProcessor', 'CB.view.login.Home'],
    id: 'loginController',
    tag: 'Login',
    views: [
        'login.Home'
    ],
    me: this,
    /** resources */
    errAlreadyRegisteredEmail: 'This email address already registered',
    errInvalidLoginOrPassword: 'Login or password invalid',
    init: function() {
        this.application.on({
            selectLogin: this.onSelectLogin,
            scope: this
        });
        this.control({
            'login button[action=login]': {
                click: this.login
            },
            'login button[action=register]': {
                click: this.register
            }
        });
    },
    login: function(button) {
        console.log(this.tag + ': process login button event');

        var url = 'j_spring_security_check';
        this.processForm(button, url);

        console.log(this.tag + ': login button event processed success');
    },
    register: function(button) {
        console.log(this.tag + ': process register button event');

        var url = '/auth/register';
        this.processForm(button, url);

        console.log(this.tag + ': register button event processed success');
    },
    processForm: function(button, endpointUrl) {
        console.debug(this.tag + ': process form by endpoint: ' + endpointUrl);


        var processor = Ext.create('CB.core.FormProcessor');
        processor.processForm(button, endpointUrl);

        console.debug(this.tag + ': form processed by endpoint: ' + endpointUrl);
    }, // processForm
    onSelectLogin: function(param, error) {
        console.debug(this.tag + ': process login event: ' + param
                + ', error: ' + error);

        if (error === 'ALREADY_REGISTERED_EMAIL') {
            CB.Current.getViewport().getComponent(1).getForm()
                    .findField('j_username')
                    .markInvalid(this.errAlreadyRegisteredEmail);
            return;
        }
        if (error === 'INVALID_LOGIN_ATTEMPT') {
            var form = CB.Current.getViewport().getComponent(1).getForm();
            form.findField('j_username')
                    .markInvalid(this.errInvalidLoginOrPassword);
            form.findField('j_password')
                    .markInvalid(this.errInvalidLoginOrPassword);
            return;
        }

        var view = Ext.getCmp('loginFormPanel');
        view = view ? view : Ext.create('CB.view.login.List');

        var viewPort = Ext.ComponentQuery.query('viewport')[0];
        viewPort.removeAll();
        viewPort.add(view);

        console.debug(this.tag + ': success process login event.');
    } // onSelectLogin

});