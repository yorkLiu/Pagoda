/**
 * Created by yongliu on 7/27/16.
 */





Ext.define('Pagoda.configuration.view.AccountList', {
  extend: 'Ext.grid.Panel',
  alias: 'widget.accountlist',
  requires: [
    'Pagoda.configuration.store.Accounts',
    'Pagoda.configuration.view.AccountEdit'
  ],

  initComponent: function(){
    var me = this;
    
    this.store = me.getStore();

    this.columns = [
      {
        header: '账号',
        dataIndex: 'username'
      },
      {
        header: '账号密码',
        dataIndex: 'password'
      },
      {
        header: '账号等级',
        dataIndex: 'accountLevel'
      },
      {
        header: '所属平台',
        dataIndex: 'categoryType'
      },
      {
        header: '是否被锁',
        dataIndex: 'locked'
      },
      {
        header: '是否不可用',
        dataIndex: 'disabled'
      },
      {
        header: '不可用原因',
        dataIndex: 'disabledDescription'
      }
    ];


    this.dockedItems = [
      {
        xtype:'toolbar',
        dock: 'top',
        items:[
          {
            text: 'Add',
            iconCls: 'add',
            scope: me,
            handler: me.onAddHandler
          },{
            text: 'Edit',
            iconCls: 'edit',
            action: 'edit',
            disabled: true,
            scope: me,
            handler: me.onUpdateHandler
          },{
            text: 'Delete',
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


    this.listeners = {
      itemdblclick: this.onItemDblClickHandler,
      selectionchange : this.onRowSelectChanged
    };

    this.callParent();
  },

  getStore: function(){
    var store = Ext.StoreManager.lookup('PagodaAccountsStore');
    if(!store || store == null){
      store = Ext.create('Pagoda.configuration.store.Accounts',{
        storeId: 'PagodaAccountsStore'
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
  },

  addOrUpdate: function(record){
    var me = this,
      title = '新建账号';
    if(record){
      title = '更新账号';
    }
    var win = Ext.widget('accountedit',{
      title: title,
      modal: true,
      width: 600,
      autoHeight: true,
//      autoWidth: true,
//      autoHeight: true,
//      maxWidth: 800,
//      maxHeight: 600,
      activeRecord: record,
      addRecord: function(record){
        console.log('***record:', record);
        me.store.add(record);
      },
      saveRecord: function(){
        console.log("*****")
        try {
          me.store.sync({
            success: function () {
              // todo after success
              win.close();
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
          console.log('**e:', e);
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
  onRemoveHandler: function(){
    var me = this,
      model = me.getSelectionModel(),
      record = model.getLastSelected();

    if(model.hasSelection() && record){
      Ext.MessageBox.show({
        title: 'Delete MyTestModel',
        msg: 'Are you sure you want to delete all information of this MyTestModel?',
        buttons: Ext.MessageBox.YESNO,
        icon: Ext.MessageBox.QUESTION,
        fn: function(btn){
          if(btn === 'yes'){
            // do remove this record
            me.store.remove(record);
            me.store.sync();
          }
        }
      });
    }
  }

});