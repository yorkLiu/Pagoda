
/**
 * Created by yongliu on 8/22/16.
 */

Ext.define('Pagoda.configuration.store.AppTypes',{
  extend: 'Pagoda.store.Base',
  requires: [
    'Pago.data.DwrProxy',
    'Pagoda.configuration.model.AppType'
  ],

  model: 'Pagoda.configuration.model.AppType',

  pageSize: 50,

  proxy: {
    type: 'dwr',

    api : {
      read:commissionController.readAppTypes
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