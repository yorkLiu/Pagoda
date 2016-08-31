/**
 * Created by yongliu on 7/27/16.
 */
Ext.define('Pagoda.configuration.model.Account',{
  extend: 'Ext.data.Model',
  fields: [
    {name: 'id',type:'int'} ,
    {name: 'username',type:'string'},
    {name: 'password',type:'string'},
    {name: 'accountLevel',type:'int'},
    {name: 'appTypeId',type:'int'},
    {name: 'appTypeName',type:'string'},
    {name: 'disabledDescription',type:'string'},
    {name: 'disabled',type:'bool'},
    {name: 'locked',type:'bool'}
  ]
});