Ext.define('Pagoda.Configuration', {
  extend:'Ext.ux.desktop.Module',

  requires:[
    'Pagoda.configuration.view.ConfigRepository',
    'Pagoda.configuration.store.AppTypes'
  ],

  id:'configuration-win',
  moduleName: 'configuration-win',

  init:function () {
    this.launcher = {
      text:'配置管理',
      iconCls:'icon-configuration',
      handler:this.createWindow,
      scope:this
    }
  },


  createWindow:function () {
    var me = this;
    var desktop = this.app.getDesktop();
    var win = desktop.getWindow('configuration-win');

    if (!win) {
      win = desktop.createWindow({
        id:'configuration-win',
        iconCls: 'icon-configuration',
        title:'配置管理',
        width:880,
        height:600,
        shim:false,
        animCollapse:false,
        border:true,
        constrainHeader:true,
        resizable:true,

        defaults:{
          border:false
        },
        layout:'fit',
        layoutConfig:{
          animate:false
        },
        items: {
          xtype: 'configrepository'
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