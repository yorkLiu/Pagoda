/**
 * Created by yongliu on 8/22/16.
 */
Ext.define('Pagoda.configuration.model.AppType',{
  extend: 'Ext.data.Model',
  fields: [
    {name: 'id',type:'int'} ,
    {name: 'appName',type:'string'} ,
    {name: 'description',type:'string'}
  ]
});