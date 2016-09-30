/**
 * Created by yongliu on 9/14/16.
 */

Ext.define('Pagoda.merchant.view.MerchantList', {
  extend:'Ext.grid.Panel',
  alias: 'widget.merchantlist',
  requires:[
    'Pagoda.merchant.store.Merchants',
    'Pagoda.merchant.view.MerchantEdit',
    'Pagoda.merchant.view.PreOrderList'
  ],

  forceFit: true,
  selType: 'rowmodel',
  
  orderStatus: {
    'INIT': '安排中',
    'PENDING': '已安排',
    'IN_PROGRESS': '正在刷单',
    'CANCELLED': '已取消',
    'COMPLETED': '已完成'
  },
  
  initComponent: function(){
    var me = this;
    
    this.store = me.getStore();
    
    this.columns = [
      {
        header: '别名',
        dataIndex: 'name',
        flex: 100
      },
      {
        header: '商家名称',
        dataIndex: 'merchantName',
        flex: 100
      },
      {
        header: '商家ID',
        dataIndex: 'merchantNo',
        flex: 100
      },
      {
        header: '刷单数量(单)',
        dataIndex: 'totalCount',
        flex: 100
      },
      {
        xtype: 'datecolumn',
        format:'Y-m-d H:i',
        header: '开始时间',
        dataIndex: 'scheduleDateTime',
        flex: 100
      },
      {
        header: '状态',
        dataIndex: 'status',
        flex: 100,
        renderer: function(v){
          if(v){
            return me.orderStatus[v.toUpperCase()];
          }
          return v;
        }
      },
      {
        header: '团购?',
        dataIndex: 'groupBuy',
        flex: 100,
        renderer: Pago.Utils.booleanRenderer
      },
      {
        header: '海购?',
        dataIndex: 'overseas',
        flex: 100,
        renderer: Pago.Utils.booleanRenderer
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
            text: globalRes.buttons.copy,
            iconCls: 'icon-copy',
            disabled: true,
            action: 'copy',
            scope: me,
            handler: me.onCopyHandler
          }, {
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
          }, {
            text: '商家刷单详情',
            action: 'orderManager',
            disabled: true,
            scope: me,
            handler: me.onOrderManagerHandler
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
    var store = Ext.StoreManager.lookup('PagodaMerchantStore');
    if(!store || store == null){
      store = Ext.create('Pagoda.merchant.store.Merchants', {
        storeId: 'PagodaMerchantStore'
      });
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
    this.down('button[action=copy]')[enabled ? 'enable' : 'disable']();
    this.down('button[action=orderManager]')[enabled ? 'enable' : 'disable']();
  },

  addOrUpdate: function(record, title){
    var me = this,
      readOnly = false;
      title = title ? title : '添加商家';
    
    if(readOnly){
      title = Pago.util.Utils.markReadOnlyTitle(title);
    }
    
    var win = Ext.widget('merchantedit',{
      title: title,
      modal: true,
      width: 600,
      autoHeight: true,
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

  onAddHandler: function(){
    this.addOrUpdate(null);

  },

  onCopyHandler: function(){
    var record = this.getSelectionModel().getLastSelected();
    if(record){
      var title = '拷贝商家信息', 
        copyFromId = record.get('id'),
        newRecord = this.store.createModel(record.data);
      var newName = Ext.Date.format(new Date(), 'm.d') + ' ' + record.data.name;

      newRecord.data['id'] = null;
      newRecord.data['copyFromId'] = copyFromId;
      newRecord.data['fromCopy'] = true;
      newRecord.set('name', newName);
      
      this.addOrUpdate(newRecord, title);
    }
  },
  
  onUpdateHandler: function(){
    var record = this.getSelectionModel().getLastSelected();
    if(record){
      this.addOrUpdate(record, '更改商家信息');
    }
  },
  
  onRemoveHandler: function(){
    var me = this,
      model = me.getSelectionModel(),
      record = model.getLastSelected(),
      delTitle = '删除商家信息';

    if(model.hasSelection() && record){
      Ext.MessageBox.show({
        title: delTitle,
        msg: Ext.String.format('你确定要删除[<span style="color:blue">{0}</span>]此商家及该商家的所有订单信息吗?', record.get('name')),
        buttons: Ext.MessageBox.YESNO,
        icon: Ext.MessageBox.QUESTION,
        fn: function(btn){
          if(btn === 'yes'){
            // do remove this record
            me.store.remove(record);
            me.store.sync({
              success: function(){
                me.store.load();
                Ext.MessageBox.show({
                  title: delTitle,
                  msg: '商家信息已删除成功',
                  icon: Ext.MessageBox.INFO,
                  buttons: Ext.MessageBox.OK
                });
              },
              failed: function(){
                Ext.MessageBox.show({
                  title: delTitle,
                  msg: '该商家信息暂时不能删除',
                  icon: Ext.MessageBox.ERROR,
                  buttons: Ext.MessageBox.YES
                });
              }
            });
          }
        }
      });
    }
  },

  onOrderManagerHandler: function(){
    var me = this,
      model = me.getSelectionModel(),
      record = model.getLastSelected(),
      merchantId  = record ? record.get('id'): null;

    if(model.hasSelection() && record){
      var win = Ext.createWidget('window', {
        title: Ext.String.format("<span style='color: blue'>{0}</span>刷单详情", record.data.name),
        modal: true,
        width: 800,
        height: 500,
        layout: 'fit',
        items:{
          xtype: 'preorderlist',
          merchantId: merchantId
        },
        activeRecord: record
      }).show();
    }
  }
});