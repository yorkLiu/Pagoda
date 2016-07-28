/**
 * Created by yongliu on 7/26/16.
 */

//Ext.define('Pagoda.userrole.store.Users', {
//  extend: 'Pagoda.store.Base',
//  requires: [
//    'Pagoda.userrole.model.User',
//    'Pago.data.DwrProxy'
//  ],
//
//  model:'Pagoda.userrole.model.User',
//
//  //pageSize:20,
//
//  proxy:{
//    type:'dwr',
//    api:{
//      read:userRoleController.listUsers,
//      create:userRoleController.createUser,
//      update:userRoleController.createUser
//      //destroy:myTestModelController.destroyMyTestModel
//    },
//
//    reader:{
//      type:'json',
//      root:'data',
//      totalProperty:'total',
//      messageProperty:'message',
//      successProperty:'success',
//      idProperty:'id'
//    },
//
//    writer:{
//      type:'json',
//      writeAllFields:false,
//      root:'data'
//    },
//
//    listeners:{
//      exception:function (proxy, response, operation) {
//        Ext.MessageBox.show({
//          title:'Remote Exception',
//          msg:operation.getError(),
//          icon:Ext.MessageBox.ERROR,
//          buttons:Ext.Msg.OK
//        });
//      }
//    }
//  },
////  sorters: [{property:'name',direction:'ASC'}],
//  autoLoad:false,
//  autoSync:false
//});


Ext.define('Pagoda.userrole.store.Users',{
  extend: 'Pagoda.store.Base',
  requires: [
    'Pago.data.DwrProxy',
    'Pagoda.userrole.model.User'
  ],

  model: 'Pagoda.userrole.model.User',


  pageSize: 50,
  
  proxy: {
    type: 'dwr',

    api : {
      read:userRoleController.listUsers,
      create:userRoleController.createUser,
      update:userRoleController.createUser
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