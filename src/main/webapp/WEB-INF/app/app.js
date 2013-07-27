Ext.application({
    requires: [
        'Ext.container.Viewport',
        'Ext.window.MessageBox',
        'Ext.tip.*',
        //
        'CB.Config',
        'CB.controller.Login',
        'CB.controller.Router',
        'CB.controller.Registration',
        'CB.controller.Cabinet',
        'CB.controller.Error'
    ],
    name: 'CB',
    rootFolder: '/app',
    appFolder: '/app/app',
    appProperty: 'Current',
    controllers: ['Login', 'Router', 'Registration', 'Cabinet', 'Error'],
    tag: 'CB.app',
    appTitle: 'Cabinet example',
    /* Main startup function, create global viewport here. */
    launch: function() {
        
        this.addConsole();

        /* Try to load localization based on browser/OS locale settings */
        loadLocale = function(me) {
            loadUrl = function(url) {
                return Ext.Ajax.request({
                    url: url,
                    success: me.onLocaleSuccess,
                    failure: me.onLocaleFailure,
                    scope: me
                });
            };

            var language = window.navigator.userLanguage || window.navigator.language;
            if (!language) {
                var message = 'Can\'t determine browser locale';
                console.log(this.tag + ': ' + message);
                alert(message);
                return;
            }
            var lang = language.split('-');
            if (!lang || lang.length < 1) {
                var message = 'Can\'t determine browser locale from ['
                        + language + '].';
                console.log(this.tag + ': ' + message);
                alert(message);
                return;
            }

            var localeExt = Ext.util.Format.format(me.rootFolder + "/extjs/locale/ext-lang-{0}.js", lang[0]);
            var localeHR = Ext.util.Format.format(me.appFolder + "/locale/CB-lang-{0}.js", lang[0]);

            loadUrl(localeExt);
            loadUrl(localeHR);
        };

        loadLocale(this);
    },
    /* Callback for loading localization files */
    onLocaleSuccess: function(response) {
        console.log(this.tag + ': Localization file loaded success');
        eval(response.responseText);
        
        this.createViewport();
    },
    /* Callback for loading localization files */
    onLocaleFailure: function() {
        
        this.createViewport();
        
        var message = 'Sorry, can\'t load localization file, you can see only '
                + 'default english interface';
        console.log(this.tag + ': ' + message);
        Ext.Msg.alert('Failure', message);
    },
    createViewport: function() {
        /* Create global viewport (aka application). */
        
        if(this.getViewport()) {
            console.log('Viewport already created.');
            return;
        }
        
        Ext.create('Ext.container.Viewport', {
            layout: {
                align: 'center',
                type: 'vbox'
            },
            items: [
                {
                    id: 'content-header',
                    html: '<div></div>'
                },
                {
                    id: 'content-form',
                    padding: 5,
                    xtype: 'login'
                }
            ]
        });

    },
    addConsole: function() {

        if (window.console) {
            return;
        } else {
            window.console = {};
        }
        // union of Chrome, FF, IE, and Safari console methods
        var m = [
            "log", "info", "warn", "error", "debug", "trace", "dir", "group",
            "groupCollapsed", "groupEnd", "time", "timeEnd", "profile", "profileEnd",
            "dirxml", "assert", "count", "markTimeline", "timeStamp", "clear"
        ];
        // define undefined methods as noops to prevent errors
        for (var i = 0; i < m.length; i++) {
            if (!window.console[m[i]]) {
                window.console[m[i]] = function() {
                };
            }
        }
    },
    getViewport: function() {
        return Ext.ComponentQuery.query('viewport')[0];
    },
    setView: function(view) {
        console.log(this.tag + ': activate view [' + view.displayName + ']');
        var viewPort = this.getViewport();

        var oldView = viewPort.getComponent(1);
        viewPort.remove(oldView);
        viewPort.add(view);
    }
});