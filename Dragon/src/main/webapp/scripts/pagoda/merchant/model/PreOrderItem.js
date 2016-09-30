/**
 * Created by yongliu on 9/22/16.
 */



Ext.define('Pagoda.merchant.model.PreOrderItem', {
  extend: 'Ext.data.Model',
  fields: [
    {name: 'id', type: 'int'},
    {name: 'keyword', type: 'string'},
    {name: 'itemUrl', type: 'string'},
    {name: 'sku', type: 'string'},
    {name: 'name', type: 'string'},
    {name: 'price'},
    {name: 'count', type: 'int'},
    {name: 'startPrice'},
    {name: 'endPrice'},
    {name: 'priority', type: 'int'},
    {name: 'commentContent', type: 'string'},
    {name: 'creatorName', type: 'string'},
    {name: 'createDate', type: 'date'}
  ]
});