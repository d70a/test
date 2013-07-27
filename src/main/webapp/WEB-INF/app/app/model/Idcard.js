/**
 * Model for Idcard entity.
 */
Ext.define('CB.model.Idcard', {
    extend: 'Ext.data.Model',
    requires: ['Ext.String.format', 'Ext.data.association.BelongsTo',
        'CB.model.User'],
    fields: ['serial', 'number',
        {name: 'birthDate', type: 'date', dateFormat: 'Y-m-d'}],
    associations: [{
            model: 'User',
            type: 'hasOne',
            autoLoad: true
        }],
    id: 'modelIdcard',
    proxy: {
        type: 'rest',
        api: {
            read: new CB.Config().getUrlIdcard(),
            create: new CB.Config().getUrlIdcard(),
            update: new CB.Config().getUrlIdcard(),
            destroy: new CB.Config().getUrlIdcard()
        },
        reader: {
            type: 'json',
            root: 'payload',
            totalProperty: 'payload.totalElements'
        },
        writer: {type: 'json'}
    },
    validations: [
        {type: 'presence', field: 'serail'},
        {type: 'length', field: 'serial', min: 4, max: 4},
        {type: 'presence', field: 'number'},
        {type: 'length', field: 'number', min: 6, max: 6},
        {type: 'presence', field: 'birthDate'}
    ]
});