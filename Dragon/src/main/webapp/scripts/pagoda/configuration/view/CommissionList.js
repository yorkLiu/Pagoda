/**
 * Created by yongliu on 8/22/16.
 */

Ext.define('Pagoda.configuration.view.CommissionList', {
  extend: 'Ext.grid.Panel',
  alias: 'widget.commissionlist',
  requires: [
    'Pagoda.configuration.store.Commissions',
    'Pagoda.configuration.view.CommissionEdit'
  ],
  
  status: {
    enable: 'ENABLE',
    disabled: 'DISABLED',
    published: 'PUBLISHED',
    retired: 'RETIRED',
    deleted: 'DELETED'
  },

  initComponent: function(){
    var me = this;

    this.store = me.getStore();

    this.columns = [
      {
        header: '所属平台',
        dataIndex: 'appTypeName',
        flex: 100
      },
      {
        header: '名称',
        dataIndex: 'description',
        flex: 100
      },
      {
        header: '拥金1最高成交价',
        dataIndex: 'dealPrice1',
        flex: 130
      },
      {
        header: '拥金1',
        dataIndex: 'commissionAmount1',
        flex: 70
      },
      {
        header: '拥金2最高成交价',
        dataIndex: 'dealPrice2',
        flex: 130
      },
      {
        header: '拥金2',
        dataIndex: 'commissionAmount2',
        flex: 70
      },
      {
        header: '拥金3最高成交价',
        dataIndex: 'dealPrice3',
        flex: 130
      },
      {
        header: '拥金3',
        dataIndex: 'commissionAmount3',
        flex: 70
      },
      {
        header: '状态',
        dataIndex: 'status',
        flex: 70,
        renderer: function(v){
          if(v === 'ENABLE'){
            return '可用';
          } else if(v === 'DISABLED'){
            return '不可用';
          } else if(v === 'PUBLISHED'){
            return '已发布';
          } else if(v === 'DELETED'){
            return '已删除'
          } else if(v === 'RETIRED'){
            return '已撤销'
          }
          return v;
        }
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
          },
          {
            text: globalRes.buttons.update,
            iconCls: 'edit',
            action: 'edit',
            disabled: true,
            scope: me,
            handler: me.onUpdateHandler
          },
          {
            text: globalRes.buttons.enable,
            iconCls: 'icon-enable',
            action: 'enable',
            hidden: true,
            scope: me,
            handler: me.onEnableHandler
          },
          {
            text: globalRes.buttons.disable,
            iconCls: 'icon-disable',
            action: 'disable',
            hidden: true,
            scope: me,
            handler: me.onDisableHandler
          },
          {
            text: globalRes.buttons.publish,
            iconCls: 'icon-publish',
            action: 'publish',
            hidden: true,
            scope: me,
            handler: me.onPublishHandler
          },
          {
            text: globalRes.buttons.unpublish,
            iconCls: 'icon-unpublish',
            action: 'unpublish',
            hidden: true,
            scope: me,
            handler: me.onUnpublishHandler
          },
          {
            text: globalRes.buttons.delete,
            iconCls: 'remove',
            action: 'remove',
            disabled: true,
            scope: me,
            handler: me.onRemoveHandler
          }
        ]
      },{
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
    
    this.viewConfig = {
      getRowClass: function(record, rowIndex, rowParams, store){
        var status = record.get('status');
        if(status){
          if('DELETED' == status.toUpperCase()){
            return 'deleted-row';
          } else if('DISABLED' == status.toUpperCase()){
            return 'disabled-row';
          } else if('PUBLISHED' == status.toUpperCase()){
            return 'published-row';
          } else if('RETIRED' == status.toUpperCase()){
            return 'retired-row';
          }
        }
      }
    };


    this.listeners = {
      itemdblclick: this.onItemDblClickHandler,
      selectionchange : this.onRowSelectChanged
    };

    this.callParent();
  },

  getStore: function(){
    var store = Ext.StoreManager.lookup('PagodaCommissionStore');
    if(!store || store == null){
      store = Ext.create('Pagoda.configuration.store.Commissions',{
        storeId: 'PagodaCommissionStore'
      });
      store.load();
    }
    return store;
  },


  onItemDblClickHandler: function(view,record){
    if(record && record.get('status') != this.status.deleted){
      this.addOrUpdate(record);
    }
  },

  onRowSelectChanged: function(model){
    var enabled = model.hasSelection(),
      rec = model.getLastSelected(),
      status = rec ? rec.get('status') : undefined;

    this.down('button[action=edit]')[enabled && status == 'ENABLE' ? 'enable' : 'disable']();
    this.down('button[action=remove]')[enabled && (status != 'DELETED' && status != 'PUBLISHED') ? 'enable' : 'disable']();
    this.down('button[action=remove]')[enabled && status == 'PUBLISHED' ? 'hide' : 'show']();
    this.down('button[action=disable]')[enabled && status == 'ENABLE' ? 'show' : 'hide']();
    this.down('button[action=enable]')[enabled && status == 'DISABLED' ? 'show' : 'hide']();
    this.down('button[action=publish]')[enabled && (status == 'ENABLE' || status == 'RETIRED') ? 'show' : 'hide']();
    this.down('button[action=unpublish]')[enabled&& status == 'PUBLISHED' ? 'show' : 'hide']();
  },

  addOrUpdate: function(record){
    var me = this,
      readOnly = false,
      title = '新建拥金';
    if(record){
      title = '更新拥金';
      readOnly = record.get('status') != me.status.enable;
    }
    
    if(readOnly){
      title = Ext.String.format(globalRes.title.markReadOny, title);
    }
    
    var win = Ext.widget('commissionedit',{
      title: title,
      modal: true,
      width: 700,
      height: 500,
      readOnly: readOnly,
      activeRecord: record,
      addRecord: function(record){
        me.store.add(record);
      },
      saveRecord: function(){
        try {
          me.store.sync({
            success: function () {
              win.close();
              me.store.load();
            },
            failed: function () {
              Ext.MessageBox.show({
                title: title + " Error",
                msg: "There is some errors when " + title,
                icon: Ext.MessageBox.ERROR,
                buttons: Ext.MessageBox.YES
              });
            }
          });
        }catch (e){
          console.log('e:', e);
        }
      }

    }).show();
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

  onEnableHandler: function(){
    this.updateStatusHandler(this.status.enable, '标记为可用', '你确定你要将此条记录标记为可用状态吗?')
  }, 
  
  onDisableHandler: function(){
    this.updateStatusHandler(this.status.disabled, '标记为不可用', '你确定你要将此条记录标记为不可用状态吗?')
  },
  
  onPublishHandler: function(){
    this.updateStatusHandler(this.status.published, '发布', '你确定你要发布此条记录吗?')
  },
  
  onUnpublishHandler: function(){
    this.updateStatusHandler(this.status.retired, '撤销发布', '你确定你要撤销此条记录的发布吗?')
  },
  
  updateStatus:function(record, status, callbackFn){
    var me = this;
    if(record && status && me.status[status.toLowerCase()]){
      commissionController.updateStatus(record.data, me.status[status.toLowerCase()], {
        callback: function(resp){
          if(callbackFn){
            callbackFn(resp);
          }
        }
      });  
    }
  },
  
  updateStatusHandler: function(status, msgTitle, msg, callbackFn){
    var me = this,
      model = me.getSelectionModel(),
      record = model.getLastSelected();
    if(model.hasSelection() && record){
      Ext.MessageBox.show({
        title: msgTitle,
        msg: msg,
        buttons: Ext.MessageBox.YESNO,
        icon: Ext.MessageBox.QUESTION,
        fn: function(btn){
          if(btn === 'yes'){
            me.updateStatus(record, status, function(resp){
              if(resp.success){
                Ext.MessageBox.show({
                  title: msgTitle,
                  msg: '已成功' + msgTitle,
                  buttons: Ext.MessageBox.OK,
                  icon: Ext.MessageBox.INFO
                });
              }
              model.deselect(record);
              me.store.load();
            });
          }
        }
      });
    }
  },
  
  onRemoveHandler: function(){
    var me = this,
      model = me.getSelectionModel(),
      record = model.getLastSelected();

    if(model.hasSelection() && record){
      me.updateStatusHandler(me.status.deleted, '删除拥金', '你确定要删除本条拥金吗?');
    }
  }

});