/**
 * Created by yongliu on 7/27/16.
 */






Ext.define('Pagoda.configuration.view.AccountEdit', {
  extend: 'Ext.window.Window',
  alias: 'widget.accountedit',
  uses: [
    'Ext.form.Panel',
    'Ext.form.field.Text',
    'Ext.form.field.Date',
    'Ext.form.field.Number',
    'Ext.button.Button',
    'Ext.form.field.ComboBox',
    'Ext.data.ArrayStore'
  ],

  activeRecord: undefined,

  layout: 'fit',

  initComponent: function () {
    var me = this;

    this.items = [
      {
        xtype: 'form',
        itemId: 'form',
        frame: true,
        height: '100%',
        margin: '3 3 3 3',
        defaults: {
          xtype: 'textfield',
          anchor: '100%',
          labelWidth: 100
        },
        trackResetOnLoad:true,
        items: [
          //{
          //  xtype: 'hidden',
          //  name: 'id'
          //},
          {
            fieldLabel: "账号",
            name: 'username',
            emptyText: '账号',
            maxLength: 50,
            allowBlank: false
          },
          {
            fieldLabel: '账号密码',
            name: 'password',
            maxLength: 50,
            allowBlank: false
          },
          {
            xtype: 'numberfield',
            fieldLabel: '账号等级',
            name: 'accountLevel',
            allowDecimals: false,
            step: 1,
            minValue: 0,
            maxValue: 5,
            allowBlank: true
          },
          {
            xtype: 'combo',
            fieldLabel: '所属平台',
            displayField: 'label',
            valueField: 'value',
            queryMode: 'local',
            triggerAction: 'all',
            editable: false,
            name: 'categoryType',
            store: me.getCategoryTypeStore(),
            allowBlank: true
          }
        ],
        buttons: [
          {
            text: globalRes.buttons.save,
            scope: me,
            formBind: true,
            handler: me.onSaveHandler
          }, {
            text: globalRes.buttons.cancel,
            scope: me,
            handler: me.onCloseHandler
          }
        ]
      }
    ];


    this.callParent();
  },

  afterRender: function () {
    this.callParent();
    if (this.activeRecord && this.activeRecord != null) {
      this.down('form').getForm().loadRecord(this.activeRecord);
    }
  },

  addRecord: Ext.emptyFn,
  saveRecord: Ext.emptyFn,

  onSaveHandler: function () {
    var me = this,
      form = this.down('form').getForm(),
      values = form.getValues();
    if (me.activeRecord && me.activeRecord != null) {
      form.updateRecord(me.activeRecord);
    } else {
      me.addRecord(values);
    }
    me.saveRecord();
  },

  getCategoryTypeStore: function(){
    var store = Ext.StoreManager.lookup('PagodaCategoryTypeStore');
    if(!store || store == null){
      store = Ext.create('Ext.data.ArrayStore', {
        storeId: 'myStore',
        fields: ['label', 'value'],
        data: [
          ['京东', 'JD'],
          ['一号店', 'YHD'],
          ['当当', 'DD']
        ]
      });
    }
    return store;
  },

  onCloseHandler: function () {
    var me = this,
      form = me.down('#form').getForm();
    if (form.isDirty()) {
      Ext.MessageBox.show({
        title: 'Confirm Close',
        msg: 'Are you sure you want to close this window',
        icon: Ext.MessageBox.QUESTION,
        buttons: Ext.MessageBox.YESNO,
        fn: function (btn) {
          if (btn == 'yes') {
            me.close();
          }
        }
      });
    } else {
      me.close();
    }
  }

});