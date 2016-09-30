/**
 * Created with app source gen maven plugin
 * author: @author
 * Date: @Date
 * Time: @Time
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Pagoda.Merchant', {
  extend:'Ext.ux.desktop.Module',

  requires:[
    'Pagoda.configuration.store.AppTypes',
    'Pagoda.merchant.view.MerchantList'
  ],

  id:'merchant-win',
  moduleName: 'merchant-win',

  init:function () {
    this.launcher = {
      text:'商家信息管理',
      iconCls:'icon-merchant',
      handler:this.createWindow,
      scope:this
    }
  },


  createWindow:function () {
    var me = this;
    var desktop = this.app.getDesktop();
    var win = desktop.getWindow('merchant-win');

    if (!win) {
      win = desktop.createWindow({
        id:'merchant-win',
        iconCls: 'icon-merchant',
        title:'商家信息管理',
        width: 1100,
        height: 600,
        shim: false,
        animCollapse: false,
        border: true,
        constrainHeader: true,
        resizable: true,
        maximized: true,

        defaults:{
          border:false
        },
        layout:'fit',
        layoutConfig:{
          animate:false
        },
        items: {
          xtype: 'merchantlist'
        },

        listeners:{
          'afterrender': me.onWinAfterRender

        }
      });
    }
    win.show();
    //return win;
  },

  onWinAfterRender: function(){

    // init app type store
    var appTypeStore = Ext.StoreManager.lookup('PagodaAppTypeStore');
    if(!appTypeStore || appTypeStore == null){
      appTypeStore = Ext.create('Pagoda.configuration.store.AppTypes', {
        storeId: 'PagodaAppTypeStore'
      });
    }
    if(appTypeStore != null){
      appTypeStore.load();
    }

  }


});