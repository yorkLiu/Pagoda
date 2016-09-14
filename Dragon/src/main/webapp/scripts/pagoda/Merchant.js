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
    'Pagoda.merchant.view.MerchantList'
  ],

  id:'merchant-win',
  moduleName: 'merchant-win',

  init:function () {
    this.launcher = {
      text:'商家信息管理',
      iconCls:'tabs',
      handler:this.createWindow,
      scope:this
    }
  },


  createWindow:function () {
    var desktop = this.app.getDesktop();
    var win = desktop.getWindow('merchant-win');

    if (!win) {
      win = desktop.createWindow({
        id:'merchant-win',
        title:'商家信息管理',
        width:800,
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
          xtype: 'merchantlist'
        }
      });
    }
    win.show();
    return win;

  }


});