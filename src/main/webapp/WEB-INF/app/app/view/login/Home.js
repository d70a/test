/**
 * View for login page
 */
Ext.define('CB.view.login.Home', {
    extend: 'CB.view.Parent',
    alias: 'widget.login',
    title: 'Please login',
    id: 'login',
    initComponent: function() {
        var me = this;

        me.labelWidth = 100;
        me.frame = true;

        me.defaultType = 'textfield';
        me.width = 300;
        me.monitorValid = true;

        me.items = [{
                fieldLabel: me.fldEmail,
                name: 'j_username',
                allowBlank: false,
                vtype: 'email',
                inputWidth: 180
            }, {
                fieldLabel: me.fldPassword,
                name: 'j_password',
                inputType: 'password',
                allowBlank: false,
                inputWidth: 180
            }];

        me.buttons = [{
                id: 'btnLogin',
                xtype: 'button',
                text: me.btnLogin,
                formBind: true,
                action: 'login',
                scope: me
            },
            {
                id: 'btnRegister',
                xtype: 'button',
                text: me.btnRegister,
                formBind: false,
                action: 'register'
            }];

        this.callParent(arguments);
    }
});