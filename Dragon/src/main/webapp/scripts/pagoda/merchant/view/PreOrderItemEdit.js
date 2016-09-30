/**
 * Created by yongliu on 9/22/16.
 */

Ext.define('Pagoda.merchant.view.PreOrderItemEdit', {
  extend: 'Ext.window.Window',
  alias: 'widget.preorderitemedit',
  requires: [
    'Ext.form.Panel',
    'Ext.form.field.Text',
    'Ext.form.field.TextArea',
    'Ext.form.field.Number',
    'Ext.button.Button'
  ],

  activeRecord: undefined,
  layout: 'fit',
  saveRecord: Ext.emptyFn,
  addRecord: Ext.emptyFn,
  readOnly: false,

  initComponent: function () {
    var me = this;

    this.items = [
      {
        xtype: 'form',
        itemId: 'form',
        border: false,
        height: '100%',
        bodyPadding: 5,
        defaults: {
          xtype: 'textfield',
          anchor: '95%',
          labelWidth: 100
        },
        trackResetOnLoad: true,
        items: [
          {
            xtype: 'hidden',
            name: 'id'
          },
          {
            fieldLabel: '搜索关键字',
            name: 'keyword',
            maxLength: 50,
            allowBlank: false
          },
          {
            fieldLabel: '商品链接',
            name: 'itemUrl',
            maxLength: 255,
            allowBlank: true
          },
          {
            fieldLabel: '商品编号(SKU)',
            name: 'sku',
            maxLength: 50,
            allowBlank: false
          },
          {
            fieldLabel: '商品名称',
            name: 'name',
            maxLength: 255,
            allowBlank: false
          },
          {
            xtype: 'numberfield',
            fieldLabel: '商品价格',
            name: 'price',
            decimalPrecision: 2,
            minValue: 0,
            maxLength: 10,
            allowBlank: false
          },
          {
            xtype: 'numberfield',
            fieldLabel: '单品下单数量',
            name: 'count',
            allowDecimals : false,
            minValue: 1,
            maxLength: 50,
            allowBlank: false
          },
          {
            xtype: 'textarea',
            fieldLabel: '评价内容',
            name: 'commentContent',
            maxLength: 255
          }
        ],
        buttons:[
          {
            text: globalRes.buttons.save,
            action: 'save',
            scope: me,
            formBind: me.activeRecord ? false : true,
            disabled: me.activeRecord? true : false,
            plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)}),
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

  onSaveHandler: function(){
    var me = this,
      form = me.down('#form').getForm(),
      values = form.getValues();
    if(form.isValid()){
      if(me.activeRecord && me.activeRecord != null ){
        form.updateRecord(me.activeRecord);
      } else {
        me.addRecord(values);
      }
      //me.saveRecord();
      me.close();
    }
  },
  
  onCloseHandler: function(){
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
        