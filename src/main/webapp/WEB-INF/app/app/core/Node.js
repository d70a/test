Ext.define('CB.core.Node', {
    tag: 'Node',
    me: this,
    config: {
        back: undefined,
        next: undefined,
        view: undefined,
        serverId: undefined
    },
    constructor: function(cfg) {
        this.initConfig(cfg);
    }
});


