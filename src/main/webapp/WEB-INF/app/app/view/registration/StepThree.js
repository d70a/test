/**
 * View for registration page (step three)
 */
Ext.define('CB.view.registration.StepThree', {
    extend: 'CB.view.Parent',
    alias: 'widget.stepThree',
    title: 'Please fill up your location',
    id: 'regStepThree',
    required: [{}],
    /* resources definition */
    url: '/place',

    initComponent: function() {
        var me = this;

        me.labelWidth = 100;
        me.frame = true;

        me.defaultType = 'textfield';
        me.width = 300;

        me.monitorValid = true;

        me.items = [
            {
                fieldLabel: this.fldRegion,
                name: 'region',
                allowBlank: false,
                inputWidth: 180
            }, {
                fieldLabel: this.fldCity,
                name: 'city',
                allowBlank: true,
                inputWidth: 180
            },
            {
                fieldLabel: this.fldAddress,
                name: 'address',
                allowBlank: false,
                inputWidth: 180
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
                text: me.btnFinish,
                formBind: true,
                action: 'next',
                scope: me
            }
        ];

        this.callParent(arguments);
        
        Ext.create('CB.store.Places').load({
            scope: this,
            callback: function(records) {
                if (records[0]) {
                    this.getForm().loadRecord(records[0]);
                }
            }
        });
    }
});