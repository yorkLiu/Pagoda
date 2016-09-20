/**
 * Created by yongliu on 9/14/16.
 */

Ext.define('Pagoda.merchant.store.Merchants',{
  extend: 'Pagoda.store.Base',
  requires: [
    'Pago.data.DwrProxy',
    'Pagoda.merchant.model.Merchant'
  ],

  model: 'Pagoda.merchant.model.Merchant',


  pageSize: 20,

  proxy: {
    type: 'dwr',

    api : {
      read:merchantController.readMerchant,
      create:merchantController.updateMerchant,
      update:merchantController.updateMerchant
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
  autoSync: false,
  autoLoad: false
});