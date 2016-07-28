/**
 * Created by yongliu on 7/27/16.
 */

Ext.define('Pagoda.configuration.store.Accounts',{
  extend: 'Pagoda.store.Base',
  requires: [
    'Pago.data.DwrProxy',
    'Pagoda.configuration.model.Account'
  ],

  model: 'Pagoda.configuration.model.Account',

  pageSize: 50,

  proxy: {
    type: 'dwr',

    api : {
      read:accountController.readAccounts,
      create:accountController.updateAccount,
      update:accountController.updateAccount
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