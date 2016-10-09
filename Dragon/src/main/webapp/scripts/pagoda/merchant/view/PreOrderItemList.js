/**
 * Created by yongliu on 9/22/16.
 */

Ext.define('Pagoda.merchant.view.PreOrderItemList', {
  extend: 'Ext.grid.Panel',
  alias: 'widget.preorderitemlist',
  requires: [
    'Pagoda.merchant.model.PreOrderItem',
    'Pagoda.merchant.view.PreOrderItemEdit'
  ],

  forceFit: true,
  layout: 'fit',
  selType: 'rowmodel',
  readOnly: false,
  

  preData: undefined,

  initComponent: function () {
    var me = this;

    this.store = me.getStore();
    
    this.columns = [
      {
        xtype: 'rownumberer',
        text: '编号',
        flex: 50
      },
      {
        header: '搜索关键字',
        dataIndex: 'keyword',
        flex: 100
      },
      {
        header: '商品编号(SKU)',
        dataIndex: 'sku',
        flex: 150
      },
      {
        header: '商品名',
        dataIndex: 'name',
        flex: 150
      },
      {
        header: '商品价格',
        dataIndex: 'price',
        flex: 100
      },
      {
        header: '购买数量',
        dataIndex: 'count',
        flex: 100
      },
      {
        header: '评价内容',
        dataIndex: 'commentContent',
        flex: 150
      }
    ];

    this.dockedItems = [
      {
        xtype:'toolbar',
        dock: 'top',
        items:[
          {
            text: '添加商品信息',
            iconCls: 'add',
            scope: me,
            plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)}),
            handler: me.onAddHandler
          },
          {
            text: '更新商品信息',
            iconCls: 'edit',
            action: 'edit',
            disabled: true,
            scope: me,
            handler: me.onUpdateHandler
          },{
            text: '删除商品信息',
            iconCls: 'remove',
            action: 'remove',
            disabled: true,
            scope: me,
            plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)}),
            handler: me.onRemoveHandler
          }
        ]
      }
    ];

    this.listeners = {
      itemdblclick: this.onItemDblClickHandler,
      selectionchange : this.onRowSelectChanged
    };

    this.callParent();
  },

  getStore: function(){
    var me = this;
    var store = Ext.StoreManager.lookup('PagodaPreOrderItemStore');
    if(!store || store == null){
      store = Ext.create('Ext.data.Store', {
        storeId: 'PagodaPreOrderItemStore',
        model: 'Pagoda.merchant.model.PreOrderItem',
        proxy: {
          type: 'memory',
          reader: {
            type: 'json'
          }
        }
      });
    }
    
    if(me.preData){
      store.loadData(me.preData);
    }
    return store;
  },

  onItemDblClickHandler: function(view,record){
    if(record){
      this.addOrUpdate(record);
    }
  },

  onRowSelectChanged: function(model){
    var enabled = model.hasSelection();
    this.down('button[action=edit]')[enabled ? 'enable' : 'disable']();
    this.down('button[action=remove]')[enabled ? 'enable' : 'disable']();
  },

  onAddHandler: function(){
    this.addOrUpdate(null);
  },

  onUpdateHandler: function(){
    var record = this.getSelectionModel().getLastSelected();
    if(record){
      this.addOrUpdate(record);
    }
  },

  addOrUpdate: function(record) {
    var me = this,
      title = '添加商品信息';

    if(record){
      title = '更新商品信息'
    }
    
    if (me.readOnly) {
      title = Pago.util.Utils.markReadOnlyTitle(title);
    }

    var win = Ext.widget('preorderitemedit',{
      title: title,
      modal: true,
      width: 600,
      autoHeight: true,
      readOnly: me.readOnly,
      activeRecord: record,
      addRecord: function(record){
        me.store.add(record);
      },
      saveRecord: function(){
        me.store.sync({
          success: function(){
            me.store.load();
            win.close();
          },
          failed: function(){
            Ext.MessageBox.show({
              title: title + "错误",
              msg: title + '发生错误了',
              icon: Ext.MessageBox.ERROR,
              buttons: Ext.MessageBox.YES
            });
          }
        });
      }
    }).show();
    
  },

  onRemoveHandler: function(){
    
  },
  
  getSubmitValues: function(){
    var me = this, data = [];
    me.getStore().each(function(rec){
      data.push(rec.data);
    });
    
    return data;
  }
});