/**
 * Created with app source gen maven plugin
 * author: @author
 * Date: @Date
 * Time: @Time
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Pagoda.mytestmodel.view.MyTestModelList',{
  extend:'Ext.grid.Panel',
  alias: 'widget.mytestmodellist',
  requires:[
     'Pagoda.mytestmodel.store.MyTestModels',
     'Pagoda.mytestmodel.view.MyTestModelEdit'
  ],

  forceFit: true,
  selType: 'rowmodel',

  initComponent: function(){
    var me = this;

    this.store = this.getStore();

    this.columns = [
        {
          text: 'id',
          dataIndex: 'id',
          flex: 100
        },
        {
          text: 'email',
          dataIndex: 'email',
          flex: 100
        },
        {
          text: 'description',
          dataIndex: 'description',
          flex: 100
        },
        {
          text: 'name',
          dataIndex: 'name',
          flex: 100
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
    var store = Ext.StoreManager.lookup('MyTestModelStore');
    if(!store || store == null){
      store = Ext.create('Pagoda.mytestmodel.store.MyTestModels',{
        storeId: 'MyTestModelStore'
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
        title = 'Add MyTestModel';
    if(record){
      title = 'Update MyTestModel';
    }
    var win = Ext.widget('mytestmodeledit',{
      title: title,
      modal: true,
      width: 400,
      height: 250,
//      autoWidth: true,
//      autoHeight: true,
//      maxWidth: 800,
//      maxHeight: 600,
      activeRecord: record,
      addRecord: function(record){
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