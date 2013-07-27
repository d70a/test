/**
 * Class realized form-to-server processing flow. Every click in forms routed
 * to this processor. Form data sended to server, server replies detected
 * and 'replies based' events generated and sended to application controllers.
 */
Ext.define('CB.core.FormProcessor', {
    errorHeader: 'Error',
    connectionError: 'Authentication server is unreachable',
    loginInvalid: 'Invalid login or password',
    alreadyRegistered: 'User with this login already registered',
    id: 'formProcessor',
    processForm: function(button, endpointUrl, jsonMode) {

        /** Get form from button reference */
        var loginForm = button.up('form');

        /** Send data to server */
        loginForm.submit({
            method: 'POST',
            url: endpointUrl,
            jsonSubmit: jsonMode ? true : false,
            /** Server return HTTP 200 */
            success: function(form, action) {
                Ext.Msg.alert('Status', 'Login Successful!', function(btn, text) {

                    var body = this.decodeResponse(action.response);
                    if (body && body.action) {
                        console.log("Server recommends action: " + body.action);
                        this.application.fireEvent('selectPage', body.action, body.payload);
                    } else {
                        Ext.Msg.alert('Error', 'No action from server side');
                    }
                });
            },
            /** Server return error or maybe not return anyting */
            failure: function(form, action) {

                var me = Ext.create('CB.core.FormProcessor');

// TODO replace if-else to switch+case+functions

                if (action.failureType === 'server') {
                    /** Connect success, but server return logical error */
                    var body = this.decodeResponse(action.response);
                    switch (body.status) {
                        case 'SUCCESS':
                        case 'SESSION_EXPIRED':
                            console.log("Server recommends action: " + body.action);
                            CB.Current.fireEvent('selectPage', body.action, body.payload);
                            break;
                        case 'INVALID_LOGIN_ATTEMPT':
                            CB.Current.fireEvent('selectPage',
                                    'NEEDED_LOGIN',
                                    body.status);
                            break;
                        default:
                            Ext.Msg.alert(me.errorHeader,
                                    action.response.reponseText);
                    }
                    return;
                }
                if (action.failureType === 'connect') {
                    /** Process critical failures */
                    return this.processErrorConnect(me, action.response);
                }
                /** Process critical failures which we cant process */
                Ext.Msg.alert(me.errorHeader,
                        me.connectionError +
                        +(action.response ? action.response.responseText : action.response));

//                reset not needed, but is possible
//                me.getForm().reset();

            }, // failure
            decodeResponse: function(response) {
                console.log('Decode response [' + response + "] to JSON.");
                var body = Ext.JSON.decode(response.responseText);
                if (body) {
                    console.log('Decoded response [' + body + "].");
                    return body;
                }
                console.warn('Body not found in response.');
            },
            processErrorConnect: function(me, response) {
                var body = this.decodeResponse(response);
                switch (body.status) {
                    case 'ALREADY_REGISTERED':
                        return this.processAlreadyRegistered(me, body);
                    default:
                        Ext.Msg.alert(me.errorHeader,
                                response.reponseText);
                }
            },
            processAlreadyRegistered: function(me, body) {
                console.log("Server connection error: " + body);
                for (var index in body.errors) {
                    var errCode = body.errors[index];
                    console.log("Process error: " + errCode);
                    switch (errCode) {
                        case 'ALREADY_REGISTERED_EMAIL':
                            CB.Current.fireEvent('selectPage',
                                    'NEEDED_LOGIN',
                                    errCode);
                            break;
                        case 'ALREADY_REGISTERED_PHONE':
                            CB.Current.fireEvent('selectRegistration',
                                    undefined,
                                    errCode);
                            break;
                        case 'ALREADY_REGISTERED_IDCARD':
                            CB.Current.fireEvent('selectRegistration',
                                    undefined,
                                    errCode);
                            break;
                        default:
                            Ext.Msg.alert(me.errorHeader, errCode);
                    }
                }
            }
        }); // loginForm.submit
    }
});