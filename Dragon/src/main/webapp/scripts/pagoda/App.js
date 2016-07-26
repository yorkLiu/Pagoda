/**
 * Created with app source gen maven plugin
 * author: @author
 * Date: @Date
 * Time: @Time
 * To change this template use File | Settings | File Templates.
 */


Ext.define('Pagoda.App', {
  extend: 'Ext.ux.desktop.App',

  requires: [
    'Ext.window.MessageBox',

    'Ext.ux.desktop.ShortcutModel',
    'Pagoda.Settings'
    //,

    // import modules
    //'Pagoda.MyTestModel'

    // todo import you modules
  ],

  init: function() {
    this.callParent();
  },

  getModules : function(){

    // todo add your modules

    return [
      //new Pagoda.MyTestModel()
    ];
//      eg:
//    return [
//      new Pagoda.VideoWindow(),
//      new Pagoda.SystemStatus(),
//      new Pagoda.GridWindow(),
//      new Pagoda.TabWindow()
//    ];

    return [

    ];
  },

  getDesktopConfig: function () {
    var me = this, ret = me.callParent();

    return Ext.apply(ret, {
      //cls: 'ux-desktop-black',
      contextMenuItems: [
        { text: 'Change Settings', handler: me.onSettings, scope: me }
      ],

      shortcuts: Ext.create('Ext.data.Store', {
        model: 'Ext.ux.desktop.ShortcutModel',
        data: [
          // todo add your module @module: 'your defined module id'
          //{ name: 'Test', iconCls: 'grid-shortcut', module: 'mytestmodel-win' }
          //eg:
//          { name: 'Grid Window', iconCls: 'grid-shortcut', module: 'grid-win' },
//          { name: 'Accordion Window', iconCls: 'accordion-shortcut', module: 'acc-win' },
//          { name: 'Notepad', iconCls: 'notepad-shortcut', module: 'notepad' },
//          { name: 'System Status', iconCls: 'cpu-shortcut', module: 'systemstatus'}
        ]
      }),

      wallpaper: basePath + 'scripts/desktop/wallpapers/desk.jpg',
      wallpaperStretch: false
    });
  },

  // quick task bar
  getTaskbarConfig: function () {
    var ret = this.callParent();

    return Ext.apply(ret, {
      quickStart: [
        // todo add module on quick start bar
        // eg:
//        { name: 'Accordion Window', iconCls: 'accordion', module: 'acc-win' },
//        { name: 'Grid Window', iconCls: 'icon-grid', module: 'grid-win' }
      ],
      trayItems: [
        { xtype: 'trayclock', flex: 1 }
      ]
    });
  },

  // config for the start menu
  getStartConfig : function() {
    var me = this, ret = me.callParent();

    return Ext.apply(ret, {
      title: 'Don Griffin',
      iconCls: 'user',
      height: 300,
      toolConfig: {
        width: 100,
        items: [
          {
            text:'Settings',
            iconCls:'settings',
            handler: me.onSettings,
            scope: me
          },
          '-',
          {
            text:'Logout',
            iconCls:'logout',
            handler: me.onLogout,
            scope: me
          }
        ]
      }
    });
  },

  onLogout: function () {
    Ext.Msg.confirm('Logout', 'Are you sure you want to logout?');
  },

  onSettings: function () {
    var dlg = new Pagoda.Settings({
      desktop: this.desktop
    });
    dlg.show();
  }
});