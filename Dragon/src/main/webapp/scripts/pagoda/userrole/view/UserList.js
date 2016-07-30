/**
 * Created by yongliu on 7/26/16.
 */

Ext.define('Pagoda.userrole.view.UserList', {
  extend:'Ext.grid.Panel',
  alias: 'widget.userlist',
  requires:[
    'Pagoda.userrole.store.Users',
    'Pagoda.userrole.view.UserEdit'
  ],

  forceFit: true,
  selType: 'rowmodel',

  initComponent: function(){
    var me = this;
    this.store = this.getStore();
    
    this.columns = [
      {
        header: userRoleRes.fields.username,
        dataIndex: 'username'
      },
      {
        header: userRoleRes.fields.fullName,
        dataIndex: 'fullName'
      },
      {
        header: userRoleRes.fields.email,
        dataIndex: 'email'
      },
      {
        header: userRoleRes.fields.telephone,
        dataIndex: 'telephone'
      },
      {
        header: userRoleRes.fields.telephone2,
        dataIndex: 'telephone2'
      },
      {
        header: userRoleRes.fields.status,
        dataIndex: 'status'
      },
      {
        header: userRoleRes.fields.locked,
        dataIndex: 'locked',
        renderer: Pago.Utils.booleanRenderer
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
    var store = Ext.StoreManager.lookup('PagodaUserListStore');
    if(!store || store == null){
      store = Ext.create('Pagoda.userrole.store.Users',{
        storeId: 'PagodaUserListStore'
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
      title = 'Add User';
    if(record){
      title = 'Update User';
    }
    var win = Ext.widget('useredit',{
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
        console.log('record:', record);
        me.store.add(record);
      },
      saveRecord: function(){
        me.store.sync({
          success: function(){
            // todo after success
            win.close();
          },
          failed: function(){
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
