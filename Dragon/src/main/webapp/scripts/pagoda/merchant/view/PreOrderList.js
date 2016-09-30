/**
 * Created by yongliu on 9/22/16.
 */

Ext.define('Pagoda.merchant.view.PreOrderList', {
  extend: 'Ext.grid.Panel',
  alias: 'widget.preorderlist',
  requires: [
    'Pagoda.merchant.store.PreOrders',
    'Pagoda.merchant.view.PreOrderEdit'
  ],

  forceFit: true,
  selType: 'rowmodel',
  merchantId: undefined,

  initComponent: function () {
    var me = this;

    this.store = me.getStore();
    
    this.columns = [
      {
        xtype: 'rownumberer',
        text: '序号',
        flex: 50
      },
      {
        header: '刷单编号',
        dataIndex: 'preOrderNo',
        flex: 100
      },
      {
        header: '刷单数量(单)',
        dataIndex: 'totalCount',
        flex: 100
      },
      {
        header: '刷单平台',
        dataIndex: 'appTypeName',
        flex: 100
      },
      {
        header: '创建者',
        dataIndex: 'creatorName',
        flex: 100
      },
      {
        xtype: 'datecolumn',
        format:'Y-m-d H:i:s',
        header: '创建时间',
        dataIndex: 'createDate',
        flex: 100
      }
    ];

    this.dockedItems = [
      {
        xtype:'toolbar',
        dock: 'top',
        items:[
          {
            text: globalRes.buttons.create,
            iconCls: 'add',
            scope: me,
            handler: me.onAddHandler
          },{
            text: globalRes.buttons.update,
            iconCls: 'edit',
            action: 'edit',
            disabled: true,
            scope: me,
            handler: me.onUpdateHandler
          },{
            text: globalRes.buttons.delete,
            iconCls: 'remove',
            action: 'remove',
            disabled: true,
            scope: me,
            handler: me.onRemoveHandler
          }
        ]
      },
      {
        dock: 'bottom',
        xtype:'pagingtoolbar',
        store:this.store,
        displayInfo:true,
        prependButtons:true,
        doRefresh:function () {
          var pagebar = this,
            current = pagebar.store.currentPage,
            model = me.getSelectionModel(),
            lastRec = model.getLastSelected();
          if (pagebar.fireEvent('beforechange', pagebar, current) !== false) {
            pagebar.store.loadPage(current, {
              callback:function (records, operation, success) {
                if (success && lastRec) {
                  var idProperty = pagebar.store.getProxy().reader.getIdProperty();
                  var index = pagebar.store.find(idProperty, lastRec.get(idProperty), null, null, null, true);
                  model.select(index);
                }
              }
            });
          }
        }
      }
    ];

    this.listeners = {
      itemdblclick: this.onItemDblClickHandler,
      selectionchange : this.onRowSelectChanged
    };

    this.callParent();
  },

  getStore: function(){
    var store = Ext.StoreManager.lookup('PagodaPreOrdersStore');
    if(!store || store == null){
      store = Ext.create('Pagoda.merchant.store.PreOrders', {
        storeId: 'PagodaPreOrdersStore'
      });
      
      if(this.merchantId){
        store.setBaseParams('merchantId', this.merchantId);
      }
      store.load();
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
      this.addOrUpdate(record, '更改刷单信息');
    }
  },

  addOrUpdate: function(record, title) {
    var me = this,
      readOnly = false;
    title = title ? title : '添加刷单信息';

    if (readOnly) {
      title = Pago.util.Utils.markReadOnlyTitle(title);
    }

    var win = Ext.widget('preorderedit',{
      title: title,
      modal: true,
      width: 800,
      autoHeight: true,
      readOnly: readOnly,
      merchantId: me.merchantId,
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
  
  onRemoveHandler: function() {
    var me = this,
      model = me.getSelectionModel(),
      record = model.getLastSelected(),
      delTitle = '删除刷单信息';

    if (model.hasSelection() && record) {

    }
  }
  
});