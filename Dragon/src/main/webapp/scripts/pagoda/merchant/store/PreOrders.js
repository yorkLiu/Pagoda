/**
 * Created by yongliu on 9/22/16.
 */

Ext.define('Pagoda.merchant.store.PreOrders',{
  extend: 'Pagoda.store.Base',
  requires: [
    'Pago.data.DwrProxy',
    'Pagoda.merchant.model.PreOrder'
  ],

  model: 'Pagoda.merchant.model.PreOrder',


  pageSize: 50,

  proxy: {
    type: 'dwr',

    api : {
      read:merchantController.readPreOrder,
      create:merchantController.updatePreOrder,
      update:merchantController.updatePreOrder
      //,
      //destroy:merchantController.removeMerchant
    },

    reader: {
      root : 'data',
      totalProperty  : 'total',
      successProperty: 'success',
      messageProperty: 'message',
      idProperty: 'id'
    },

    listeners: {
      exception: function(proxy, response, operation) {
        Ext.MessageBox.show({
            title: globalRes.exception.title,
            msg: operation.getError() || globalRes.exception.message,
            icon: Ext.MessageBox.ERROR,
            buttons: Ext.Msg.OK
          }
        );
      }
    }
  },
  sorters: [{property: 'priority', direction: 'ASC'}],
  autoSync: false,
  autoLoad: false
});