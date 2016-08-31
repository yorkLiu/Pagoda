/**
 * Created by yongliu on 8/22/16.
 */

Ext.define('Pagoda.configuration.model.Commission',{
  extend: 'Ext.data.Model',
  fields: [
    {name: 'id',type:'int'} ,
    {name: 'appTypeId',type:'int'} ,
    {name: 'appTypeName',type:'string'},
    {name: 'description',type:'string'},
    {name: 'status',type:'string'},
    'priceForOneMoreOffset',
    'dealPrice1',
    'dealPrice2',
    'dealPrice3',
    'dealPrice4',
    'dealPrice5',
    'commissionAmount1',
    'commissionAmount2',
    'commissionAmount3',
    'commissionAmount4',
    'commissionAmount5',
    'commissionPercentage1',
    'commissionPercentage2',
    'commissionPercentage3',
    'commissionPercentage4',
    'commissionPercentage5',
    {name: 'allowCommission1', type: 'bool'},
    {name: 'allowCommission2', type: 'bool'},
    {name: 'allowCommission3', type: 'bool'},
    {name: 'allowCommission4', type: 'bool'},
    {name: 'allowCommission5', type: 'bool'},
    {name: 'allowCommissionType1', type: 'int'},
    {name: 'allowCommissionType2', type: 'int'},
    {name: 'allowCommissionType3', type: 'int'},
    {name: 'allowCommissionType4', type: 'int'},
    {name: 'allowCommissionType5', type: 'int'}
  ]
});
