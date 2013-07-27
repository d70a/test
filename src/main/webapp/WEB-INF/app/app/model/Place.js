/**
 * Model for Place entity.
 */
Ext.define('CB.model.Place', {
    extend: 'Ext.data.Model',
    requires: ['Ext.String.format', 'Ext.data.association.BelongsTo',
        'CB.model.User'],
    fields: ['region', 'city', 'address'],
    associations: [{
            model: 'User',
            type: 'hasOne',
            autoLoad: true
        }],
    id: 'modelPlace',
    proxy: {
        type: 'rest',
        api: {
            read: new CB.Config().getUrlPlace(),
            create: new CB.Config().getUrlPlace(),
            update: new CB.Config().getUrlPlace(),
            destroy: new CB.Config().getUrlPlace()
        },
        reader: {
            type: 'json',
            root: 'payload',
            totalProperty: 'payload.totalElements'
        },
        writer: {type: 'json'}
    },
    validations: [
        {type: 'presence', field: 'region'},
        {type: 'length', field: 'region', min: 1, max:60},
        {type: 'presence', field: 'city'},
        {type: 'length', field: 'city', min: 1, max:60},
        {type: 'presence', field: 'address'},
        {type: 'length', field: 'address', min: 1, max:60}
    ]
});