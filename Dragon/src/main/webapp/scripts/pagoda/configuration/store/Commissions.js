/**
 * Created by yongliu on 8/22/16.
 */

Ext.define('Pagoda.configuration.store.Commissions',{
  extend: 'Pagoda.store.Base',
  requires: [
    'Pago.data.DwrProxy',
    'Pagoda.configuration.model.Commission'
  ],

  model: 'Pagoda.configuration.model.Commission',

  pageSize: 50,

  proxy: {
    type: 'dwr',

    api : {
      read:commissionController.readCommissions,
      create:commissionController.updateAccount,
      update:commissionController.updateAccount
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