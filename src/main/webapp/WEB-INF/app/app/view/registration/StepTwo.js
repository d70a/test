/**
 * View for registration page (step two)
 */
Ext.define('CB.view.registration.StepTwo', {
    extend: 'CB.view.Parent',
    alias: 'widget.stepTwo',
    title: 'Please fill up your id card and birth date',
    id: 'regStepTwo',
    width: 300,
    url: '/idcard',
    initComponent: function() {
        var me = this;

        me.labelWidth = 100;
        me.frame = true;

        me.defaultType = 'textfield';
        me.width = 300;

        me.monitorValid = true;

        me.items = [
            {
                fieldLabel: me.fldIdSerial,
                name: 'serial',
                anchor: '96%',
                allowBlank: false,
                tabIndex: 1,
                inputWidth: 180,
                validator: function(val) {
                    if (!Ext.isEmpty(val) && val.length === 4) {
                        return true;
                    } else {
                        return "Please enter four digits";
                    }
                }
            }, {
                fieldLabel: me.fldIdNumber,
                name: 'number',
                anchor: '100%',
                allowBlank: false,
                tabIndex: 2,
                inputWidth: 180,
                validator: function(val) {
                    if (!Ext.isEmpty(val) && val.length === 6) {
                        return true;
                    } else {
                        return "Please enter six digits";
                    }
                }
            }, {
                xtype: 'datefield',
                fieldLabel: me.fldBirthDate,
                name: 'birthDate',
                anchor: '96%',
                format: 'd/m/Y',
                submitFormat: 'Y-m-d H:i:s',
                allowBlank: false,
                tabIndex: 3,
                inputWidth: 180,
                maxValue: new Date()
            }
        ];

        me.buttons = [{
                xtype: 'button',
                text: me.btnBack,
                formBind: false,
                action: 'back',
                scope: me
            }, {
                xtype: 'button',
                text: me.btnNext,
                formBind: true,
                action: 'next',
                scope: me
            }
        ];

        this.callParent(arguments);

        Ext.create('CB.store.Idcards').load({
            scope: this,
            callback: function(records) {
                if (records[0]) {
                    this.getForm().loadRecord(records[0]);
                }
            }
        });
    }
});