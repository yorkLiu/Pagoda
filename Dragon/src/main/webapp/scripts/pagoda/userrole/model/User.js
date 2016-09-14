/**
 * Created by yongliu on 7/26/16.
 */

Ext.define('Pagoda.userrole.model.User',{
  extend: 'Ext.data.Model',
  fields: [
    {name: 'id',type:'int'} ,
    {name: 'username',type:'string'} ,
    {name: 'password',type:'string'} ,
    {name: 'firstName',type:'string'} ,
    {name: 'lastName',type:'string'} ,
    {name: 'fullName',type:'string'} ,
    {name: 'email',type:'string'} ,
    {name: 'passwordHint',type:'string'} ,
    {name: 'status',type:'string'} ,
    {name: 'telephone',type:'string'} ,
    {name: 'telephone2',type:'string'}, 
    {name: 'locked',type:'bool'} ,
    {name: 'disabled',type:'bool'},
    {name: 'enabled',type:'bool'}
  ]
});