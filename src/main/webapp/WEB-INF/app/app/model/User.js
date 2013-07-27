/**
 * Model for User entity.
 */
Ext.define('CB.model.User', {
    extend: 'Ext.data.Model',
    requires: ['Ext.data.association.HasMany', 'Ext.data.association.BelongsTo'],
    fields: ['nameFirst', 'nameMiddle', 'nameLast', 'email', 'department', 'code', 'phone'],
    hasOne: {
        associationKey: 'idcard',
        model: 'CB.model.Idcard',
        reader: {
            type: 'json',
            root: ''
        }
    },
    proxy: {
        type: 'rest',
        api: {
            read: new CB.Config().getUrlUser(),
            create: new CB.Config().getUrlUser(),
            update: new CB.Config().getUrlUser(),
            destroy: new CB.Config().getUrlUser()
        },
        reader: {
            type: 'json',
            root: 'payload',
            successProperty: 'status'
        },
        writer: {type: 'json'}
    },
    validations: [
        {type: 'presence', field: 'nameFirst'},
        {type: 'length', field: 'nameFirst', min: 1},
        {type: 'presence', field: 'nameLast'},
        {type: 'length', field: 'nameLast', min: 1},
        {type: 'presence', field: 'email'},
        {type: 'presence', field: 'code'},
        {type: 'length', field: 'code', min: 3, max:3},
        {type: 'presence', field: 'phone'},
        {type: 'length', field: 'phone', min: 7, max:7}
    ]    
});