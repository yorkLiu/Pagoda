/**
 * Created with app source gen maven plugin
 * author: @author
 * Date: @Date
 * Time: @Time
 * To change this template use File | Settings | File Templates.
 */

Ext.define('WallpaperModel',{
  extend: 'Ext.data.Model',
  fields: [
    { name: 'text', type: 'string'},
    { name: 'img', type: 'string'}
  ]
});

Ext.define('Pagoda.Settings', {
  extend: 'Ext.window.Window',

  uses: [
    'Ext.tree.Panel',
    'Ext.data.Model',
    'Ext.tree.View',
    'Ext.form.field.Checkbox',
    'Ext.layout.container.Anchor',
    'Ext.layout.container.Border',
    'Ext.ux.desktop.Wallpaper'
  ],

  layout: 'anchor',
  title: 'Change Settings',
  modal: true,
  width: 640,
  height: 480,
  border: false,


  initComponent: function () {
    var me = this;

    me.selected = me.desktop.getWallpaper();
    me.stretch = me.desktop.wallpaper.stretch;

    me.preview = Ext.create('widget.wallpaper');
    me.preview.setWallpaper(me.selected);
    me.tree = me.createTree();

    me.buttons = [
      { text: 'OK', handler: me.onOK, scope: me },
      { text: 'Cancel', handler: me.close, scope: me }
    ];

    me.items = [
      {
        anchor: '0 -30',
        border: false,
        layout: 'border',
        items: [
          me.tree,
          {
            xtype: 'panel',
            title: 'Preview',
            region: 'center',
            layout: 'fit',
            items: [ me.preview ]
          }
        ]
      },
      {
        xtype: 'checkbox',
        boxLabel: 'Stretch to fit',
        checked: me.stretch,
        listeners: {
          change: function (comp) {
            me.stretch = comp.checked;
          }
        }
      }
    ];

    me.callParent();
  },

  createTree : function() {
    var me = this;

    function child (img) {
      return { img: img, text: me.getTextOfWallpaper(img), iconCls: '', leaf: true };
    }

    var tree = new Ext.tree.Panel({
      title: 'Desktop Background',
      rootVisible: false,
      lines: false,
      autoScroll: true,
      width: 150,
      region: 'west',
      split: true,
      minWidth: 100,
      listeners: {
        afterrender: { fn: this.setInitialSelection, delay: 100 },
        select: this.onSelect,
        scope: this
      },
      store: new Ext.data.TreeStore({
        model: 'WallpaperModel',
        root: {
          text:'Wallpaper',
          expanded: true,
          children:[
            { text: "None", iconCls: '', leaf: true },
            child('Blue-Sencha.jpg'),
            child('Dark-Sencha.jpg'),
            child('Wood-Sencha.jpg'),
            child('blue.jpg'),
            child('desk.jpg'),
            child('desktop.jpg'),
            child('desktop2.jpg'),
            child('sky.jpg')
          ]
        }
      })
    });

    return tree;
  },

  getTextOfWallpaper: function (path) {
    var text = path, slash = path.lastIndexOf('/');
    if (slash >= 0) {
      text = text.substring(slash+1);
    }
    var dot = text.lastIndexOf('.');
    text = Ext.String.capitalize(text.substring(0, dot));
    text = text.replace(/[-]/g, ' ');
    return text;
  },

  onOK: function () {
    var me = this;
    if (me.selected) {
      me.desktop.setWallpaper(me.selected, me.stretch);
    }
    me.destroy();
  },

  onSelect: function (tree, record) {
    var me = this;

    if (record.data.img) {
      me.selected = basePath + 'scripts/desktop/wallpapers/' + record.data.img;
    } else {
      me.selected = Ext.BLANK_IMAGE_URL;
    }

    me.preview.setWallpaper(me.selected);
  },

  setInitialSelection: function () {
    var s = this.desktop.getWallpaper();
    if (s) {
      var path = '/Wallpaper/' + this.getTextOfWallpaper(s);
      this.tree.selectPath(path, 'text');
    }
  }
});