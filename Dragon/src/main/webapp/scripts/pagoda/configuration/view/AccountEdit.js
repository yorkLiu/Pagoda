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
        //frame: true,
        border: false,
        height: '100%',
        margin: '3 3 3 3',
        defaults: {
          xtype: 'textfield',
          anchor: '95%',
          labelWidth: 100
        },
        trackResetOnLoad:true,
        items: [
          {
            xtype: 'hidden',
            name: 'id'
          },
          {
            fieldLabel: accountRes.fields.username,
            name: 'username',
            emptyText: '账号',
            maxLength: 50,
            allowBlank: false,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: accountRes.fields.password,
            name: 'password',
            maxLength: 50,
            allowBlank: false,
            validator: Pago.Utils.trimValidator
          },
          {
            xtype: 'combo',
            fieldLabel: accountRes.fields.categoryType,
            displayField: 'label',
            valueField: 'value',
            queryMode: 'local',
            triggerAction: 'all',
            editable: false,
            name: 'categoryType',
            store: me.getCategoryTypeStore(),
            allowBlank: false
          },
          {
            xtype: 'numberfield',
            fieldLabel: accountRes.fields.accountLevel,
            name: 'accountLevel',
            allowDecimals: false,
            step: 1,
            minValue: 0,
            maxValue: 5,
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
        title: globalRes.title.confirmClose,
        msg: globalRes.message.confirmClose,
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