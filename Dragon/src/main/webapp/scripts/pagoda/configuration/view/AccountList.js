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
        header: accountRes.fields.username,
        dataIndex: 'username',
        flex: 130
      },
      {
        header: accountRes.fields.password,
        dataIndex: 'password',
        flex: 100
      },
      {
        header: accountRes.fields.accountLevel,
        dataIndex: 'accountLevel',
        flex: 100
      },
      {
        header: accountRes.fields.categoryType,
        dataIndex: 'appTypeName',
        flex: 130
      },
      {
        header: accountRes.fields.locked,
        dataIndex: 'locked',
        flex: 150,
        renderer: Pago.Utils.booleanRenderer
      },
      {
        header: userRoleRes.fields.disabled,
        dataIndex: 'disabled',
        flex: 100,
        renderer: Pago.Utils.booleanRenderer
      },
      {
        header: accountRes.fields.disabledDescription,
        dataIndex: 'disabledDescription',
        flex: 200
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
      title = accountRes.title.createAccount;
    if(record){
      title = accountRes.title.updateAccount;
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
        me.store.add(record);
      },
      saveRecord: function () {
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
        title: accountRes.title.deleteAccount,
        msg: accountRes.message.deleteAccount,
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