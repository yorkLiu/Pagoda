/**
 * Created by yongliu on 7/27/16.
 */

Ext.define('Pagoda.configuration.store.Addresses',{
  extend: 'Pagoda.store.Base',
  requires: [
    'Pago.data.DwrProxy',
    'Pagoda.configuration.model.Address'
  ],

  model: 'Pagoda.configuration.model.Address',

  pageSize: 50,

  proxy: {
    type: 'dwr',

    api : {
      read:addressController.readAddresses,
      create:addressController.updateAddress,
      update:addressController.updateAddress
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
