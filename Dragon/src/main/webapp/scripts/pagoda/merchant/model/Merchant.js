/**
 * Created by yongliu on 9/14/16.
 */

Ext.define('Pagoda.merchant.model.Merchant', {
  extend: 'Ext.data.Model',
  fields: [
    {name: 'id', type: 'int'},
    {name: 'name', type: 'string'},
    {name: 'merchantNo', type: 'string'},
    {name: 'merchantName', type: 'string'},
    {name: 'addressOption', type: 'string'},
    {name: 'totalCount', type: 'int'},
    {name: 'commentCount', type: 'int'},
    {name: 'commentDelay', type: 'int'},
    {name: 'confirmReceiptDelay', type: 'int'},
    {name: 'flashBuy', type: 'bool'},
    {name: 'flashBuyIndexUrl', type: 'string'},
    {name: 'groupBuy', type: 'bool'},
    {name: 'groupBuyIndexUrl', type: 'int'},
    {name: 'indexUrl', type: 'string'},
    {name: 'intervalForOrder', type: 'int'},
    {name: 'overseas', type: 'bool'},
    {name: 'quickOrder', type: 'bool'},
    {name: 'scheduleDate', type: 'date'},
    {name: 'scheduleTime', type: 'string'},
    {name: 'scheduleDateTime', type: 'date'},
    {name: 'status', type: 'string'},
    {name: 'viewingTimeForOrder', type: 'int'},
    {name: 'copyFromId', type: 'int'},
    {name: 'fromCopy', type: 'bool'},
    {name: 'creatorName', type: 'string'},
    {name: 'createDate', type: 'date'}
  ]
});