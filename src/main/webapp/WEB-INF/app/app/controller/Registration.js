/**
 * Controller for registration pages. Supports 3 view with step-by-step
 * moving between views. Implemented server side data validation and
 * fields level validation in controller views.
 * 
 */
Ext.define('CB.controller.Registration', {
    extend: 'Ext.app.Controller',
    requires: ['CB.core.LinkedList'],
    /** Our controller views */
    views: [
        'registration.StepOne',
        'registration.StepTwo',
        'registration.StepThree'
    ],
    /** Linked list store our view and additional data for step-by-step moving
     * between views. See core.Node and core.LinkedList for details. */
    list: Ext.create('CB.core.LinkedList'),
    tag: 'Registration',
    config: {
        /** Latest action recommended by server. */
        lastAction: undefined
    },
    me: this,
    /** resources */
    errPhoneUsed: 'This phone number already registered by other user',
    errIdcardUsed: 'This idcard already registered by other user',
    init: function() {
        /** Init linked views, first parameter is a view, second is 
         * allowed 'server side action' for this view. */
        var cabCrtl = Ext.create('CB.controller.Cabinet');
        var cabinetView = cabCrtl.getCabinetHomeView();
        
        this.list.add(this.getRegistrationStepOneView(), 'NEEDED_REG_STEP_1');
        this.list.add(this.getRegistrationStepTwoView(), 'NEEDED_REG_STEP_2');
        this.list.add(this.getRegistrationStepThreeView(), 'NEEDED_REG_STEP_3');
        this.list.add(cabinetView, 'USER_VALID');

        this.application.on({
            selectRegistration: this.onSelectRegistration,
            scope: this
        });
        this.control({
            'button[action=next]': {
                click: this.processFormData
            },
            'button[action=back]': {
                click: this.processFormData
            }
        });
    },
    /** Main step-by-step logic implementation */
    onSelectRegistration: function(serverRecommendedStep, errorField) {
        console.log(this.tag + ': got event for step: ' + serverRecommendedStep
                + ', error field: ' + errorField);        

        /** User provide already registered phone, mark 'phone' field as invalid */
        if (errorField === 'ALREADY_REGISTERED_PHONE') {
            Ext.getCmp('regStepOne').getForm().findField('phone').markInvalid(
                    this.errPhoneUsed);
            return;
        }
        /** User provide already registered id card, mark 'id card' fields as invalid */
        if (errorField === 'ALREADY_REGISTERED_IDCARD') {
            Ext.getCmp('regStepTwo').getForm().findField('serial').markInvalid(
                    this.errIdcardUsed);
            Ext.getCmp('regStepTwo').getForm().findField('number').markInvalid(
                    this.errIdcardUsed);
            return;
        }
        

        /** Trap 'next' and 'back' buttons events when forms still not valid,
         * here we obtain next or previous view from linked list storage. */
        var view = undefined;
        switch (this.getLastAction()) {
            case 'next':
                console.log(this.tag + ': get next view by saved action');
                view = this.list.next(serverRecommendedStep);
                break;
            case 'back':
                console.log(this.tag + ': get back view by saved action');
                view = this.list.back();
                break;
            default:
                console.log(this.tag + ': no saved action, get view by server ID');
                view = this.list.getByServerId(serverRecommendedStep);
        }

        /** Ok, set obtained view in viewport */
        this.activateViewLinked(view);

        console.log(this.tag + ': event processed success.');
    }, // onSelectRegistration
    activateViewLinked: function(view) {
        console.log(this.tag + ': activate view [' + view.displayName
                + ']');

        CB.Current.setView(view);

        console.log(this.tag + ': view ' + view.displayName
                + ' added to viewport.');
    },
    /** Function process button click event, here we extract data
     * from form, try to save data on server and route event processing
     * to self.
     */
    processFormData: function(button) {
        console.log(this.tag + ': make step by [' + button.getId() + "].");
        /** Save last button for next step processing */
        this.setLastAction(button.action);
        /** Extract data frim form */
        var form = button.up('form');
        if (!form) {
            console.error('Owner of button [' + button.getId()
                    + '] doesn\'t contains url field');
        }

        /** Perform form based validation */
        if (form.isValid()) {
            /** If form valid, send form data to server, from processor
             * fire next event based on server side reply */
            var processor = Ext.create('CB.core.FormProcessor');
            processor.processForm(button, form.url, true);
        } else {
            /** If form not valid, fire event to self, user should fix
             * errors on form. */
            CB.Current.fireEvent('selectRegistration');
        }
    } // processFormData
});