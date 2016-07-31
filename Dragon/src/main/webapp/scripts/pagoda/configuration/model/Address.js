/**
 * Created by yongliu on 7/27/16.
 */
Ext.define('Pagoda.configuration.model.Address',{
  extend: 'Ext.data.Model',
  fields: [
    {name: 'id',type:'int'} ,
    {name: 'fullName',type:'string'},
    {name: 'address',type:'string'},
    {name: 'province',type:'string'},
    {name: 'city',type:'string'},
    {name: 'county',type:'string'},
    {name: 'town',type:'string'},
    {name: 'telephone',type:'bool'},
    {name: 'zipCode',type:'bool'},
    {name: 'identityCardNum',type:'bool'}
  ]
});
