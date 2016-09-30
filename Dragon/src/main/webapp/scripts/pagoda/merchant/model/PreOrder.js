/**
 * Created by yongliu on 9/22/16.
 */

Ext.define('Pagoda.merchant.model.PreOrder', {
  extend: 'Ext.data.Model',
  fields: [
    {name: 'id', type: 'int'},
    {name: 'merchantId', type: 'int'},
    {name: 'preOrderNo', type: 'string'},
    {name: 'totalCount', type: 'int'},
    {name: 'appTypeId',  type: 'int'},
    {name: 'appTypeName',  type: 'string'},
    {name: 'priority',  type: 'int'},
    {name: 'creatorName', type: 'string'},
    {name: 'createDate', type: 'date'},
    {name: 'items', default: []}
  ]
});
