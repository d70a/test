/**
 * View for deafult error page
 * 
 * TODO implement and test this
 */
Ext.define('CB.view.error.Home', {
    extend: 'CB.view.Parent',
    alias: 'widget.errorHome',
    title: 'Sorry, something happen wrong',
    id: 'errorHomePanel',
    required: [{}],
    /* resources definition */
    fldMessage: 'Sorry, error found',
    initComponent: function() {
        var me = this;

        me.labelWidth = 80;
        me.frame = true;

        me.defaultType = 'textfield';
        me.width = 300;
        me.monitorValid = true;

        me.items = [{
                fieldLabel: me.fldMessage,
                name: 'errorMessage',
                readOnly: true
            }
        ];

        this.callParent(arguments);
    }
});