/**
 * Created with app source gen maven plugin
 * author: @author
 * Date: @Date
 * Time: @Time
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Pagoda.UserRole', {
  extend:'Ext.ux.desktop.Module',

  requires:[
    'Pagoda.userrole.view.UserList'
  ],

  id:'userRole-win',
  moduleName: 'userRole-win',

  init:function () {
    this.launcher = {
      text:'用户与权限管理',
      iconCls:'icon-userRole',
      handler:this.createWindow,
      scope:this
    }
  },


  createWindow:function () {
    var desktop = this.app.getDesktop();
    var win = desktop.getWindow('userRole-win');

    if (!win) {
      win = desktop.createWindow({
        id:'userRole-win',
        iconCls: 'icon-userRole',
        title:'用户与权限管理',
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
          xtype: 'userlist'
        }
      });
    }
    win.show();
    //return win;
  }
});