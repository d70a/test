/**
 * View for user profile
 */
Ext.define('CB.view.cabinet.Home', {
    extend: 'CB.view.Parent',
    alias: 'widget.cabinetHome',
    title: 'User profile',
    id: 'cabinetHomePanel',
    required: ['CB.store.Idcards', 'CB.store.Users'],
    /* resources definition */
    fldMessage: 'Cabinet',
    tag: 'Cabinet',
    initComponent: function() {
        var me = this;

        me.labelWidth = 80;
        me.frame = true;

        me.defaultType = 'textfield';
        me.width = 300;
        me.monitorValid = true;

        me.items = [
            {
                fieldLabel: me.fldFirst,
                name: 'nameFirst',
                readOnly: true
            },
            {
                fieldLabel: me.fldMiddle,
                name: 'nameMiddle',
                readOnly: true
            },
            {
                fieldLabel: me.fldLast,
                name: 'nameLast',
                readOnly: true
            },
            {
                fieldLabel: me.fldEmail,
                name: 'email',
                readOnly: true
            },
            {
                fieldLabel: me.fldCode,
                name: 'code',
                readOnly: true
            },
            {
                fieldLabel: me.fldPhone,
                name: 'phone',
                readOnly: true
            },
            {
                fieldLabel: me.fldIdSerial,
                name: 'serial',
                readOnly: true
            },
            {
                fieldLabel: me.fldIdNumber,
                name: 'number',
                readOnly: true
            },
            {
                fieldLabel: me.fldBirthDate,
                name: 'birthDate',
                readOnly: true,
                xtype: 'datefield',
                format: 'd/m/Y'
            }
        ];

        this.callParent(arguments);

        Ext.create('CB.store.Users').load({
            scope: this,
            callback: function(records) {
                this.getForm().loadRecord(records[0]);
            }
        });
        Ext.create('CB.store.Idcards').load({
            scope: this,
            callback: function(records) {
                this.getForm().loadRecord(records[0]);
            }
        });

        console.log(this.tag + ': initComponent completed');
    }
});