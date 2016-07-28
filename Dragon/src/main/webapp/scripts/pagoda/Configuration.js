Ext.define('Pagoda.Configuration', {
  extend:'Ext.ux.desktop.Module',

  requires:[
    'Pagoda.configuration.view.ConfigRepository'
  ],

  id:'configuration-win',
  moduleName: 'configuration-win',

  init:function () {
    this.launcher = {
      text:'User & Role Manager',
      iconCls:'tabs',
      handler:this.createWindow,
      scope:this
    }
  },


  createWindow:function () {
    var desktop = this.app.getDesktop();
    var win = desktop.getWindow('configuration-win');

    if (!win) {
      win = desktop.createWindow({
        id:'configuration-win',
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
        }
      });
    }
    win.show();
    return win;

  }


});