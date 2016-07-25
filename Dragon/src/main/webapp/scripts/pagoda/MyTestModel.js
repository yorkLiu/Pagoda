/**
 * Created with app source gen maven plugin
 * author: @author
 * Date: @Date
 * Time: @Time
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Pagoda.MyTestModel', {
  extend:'Ext.ux.desktop.Module',

  requires:[
    'Pagoda.mytestmodel.view.MyTestModelList'
  ],

  id:'mytestmodel-win',
  moduleName: 'mytestmodel-win',

  init:function () {
    this.launcher = {
      text:'MyTestModel Window',
      iconCls:'tabs',
      handler:this.createWindow,
      scope:this
    }
  },


  createWindow:function () {
    var desktop = this.app.getDesktop();
    var win = desktop.getWindow('mytestmodel-win');

    if (!win) {
      win = desktop.createWindow({
        id:'mytestmodel-win',
        title:'MyTestModel',
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
          xtype: 'mytestmodellist'
        }
      });
    }
    win.show();
    return win;

  }


});