/**
 * Created with app source gen maven plugin
 * author: @author
 * Date: @Date
 * Time: @Time
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Pagoda.mytestmodel.store.MyTestModels', {
  extend:'Ext.data.Store',
  requires:[
    'Pagoda.mytestmodel.model.MyTestModel',
    'Ext.data.proxy.DwrProxy'
  ],

  model:'Pagoda.mytestmodel.model.MyTestModel',

  pageSize:20,

  proxy:{
    type:'dwr',
    api:{
      read:myTestModelController.readMyTestModel,
      create:myTestModelController.createMyTestModel,
      update:myTestModelController.updateMyTestModel,
      destroy:myTestModelController.destroyMyTestModel
    },

    reader:{
      type:'json',
      root:'data',
      totalProperty:'total',
      messageProperty:'message',
      successProperty:'success',
      idProperty:'id'
    },

    writer:{
      type:'json',
      writeAllFields:false,
      root:'data'
    },

    listeners:{
      exception:function (proxy, response, operation) {
        Ext.MessageBox.show({
          title:'Remote Exception',
          msg:operation.getError(),
          icon:Ext.MessageBox.ERROR,
          buttons:Ext.Msg.OK
        });
      }
    }
  },
//  sorters: [{property:'name',direction:'ASC'}],
  autoLoad:false,
  autoSync:false
});