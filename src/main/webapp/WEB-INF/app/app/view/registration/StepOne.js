/**
 * View for registration page (step one)
 */
Ext.define('CB.view.registration.StepOne', {
    extend: 'CB.view.Parent',
    alias: 'widget.stepOne',
    title: 'Please fill up your contact data',
    id: 'regStepOne',
    url: '/user',
    initComponent: function() {
        var me = this;

        me.labelWidth = 100;
        me.frame = true;

        me.defaultType = 'textfield';
        me.width = 300;

        me.monitorValid = true;

        me.items = [
            {
                fieldLabel: me.fldFirst,
                name: 'nameFirst',
                allowBlank: false,
                inputWidth: 180
            }, {
                fieldLabel: me.fldMiddle,
                name: 'nameMiddle',
                allowBlank: true,
                inputWidth: 180
            },
            {
                fieldLabel: me.fldLast,
                name: 'nameLast',
                allowBlank: false,
                inputWidth: 180
            },
            {
                fieldLabel: me.fldPhone,
                name: 'phone',
                allowBlank: false,
                inputWidth: 180,
                validator: function(val) {
                    if (!Ext.isEmpty(val) && val.length > 9 && val.length <13) {
                        return true;
                    } else {
                        return "Please enter phone number in format +7codenumber, for example +71119999999";
                    }
                }
            }
        ];

        me.buttons = [{
                xtype: 'button',
                text: me.btnNext,
                formBind: true,
                action: 'next',
                scope: me
            }
        ];

        me.dockedItems = [
            {xtype: 'toolbar',
                items: [
                    {
                        text: this.buttonAdd,
                        iconCls: 'icon-add'
                    }
                ]}
        ];

        this.callParent(arguments);

        Ext.create('CB.store.Users').load({
            scope: this,
            callback: function(records) {
                if (records[0]) {
                    var record = records[0];

                    var code = record.get('code');
                    var number = record.get('phone');
                    if (code && number) {
                        record.set('phone', "+7" + code + number);
                    }
                    this.getForm().loadRecord(record);
                }
            }
        });
    }
});