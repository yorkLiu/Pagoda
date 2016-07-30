/**
 * Created by yongliu on 7/27/16.
 */

Ext.define('Pagoda.configuration.view.AddressEdit', {
  extend: 'Ext.window.Window',
  alias: 'widget.addressedit',
  uses: [
    'Ext.form.Panel',
    'Ext.form.field.Text',
    'Ext.form.field.Date',
    'Ext.form.field.Number',
    'Ext.button.Button'
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
            fieldLabel: "收货人",
            name: 'fullName',
            allowBlank: false,
            emptyText: '姓名',
            maxLength: 50,
            minLength: 5,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: '收货地址',
            name: 'address',
            maxLength: 50,
            allowBlank: false,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: '手机号码',
              name: 'telephone',
            maxLength: 50,
            allowBlank: false,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: '邮编',
            name: 'zipCode',
            maxLength: 10,
            allowBlank: true
          },
          {
            fieldLabel: '身份证号码',
            name: 'identityCardNum',
            emptyText: '用于海购时的实名认证信息',
            maxLength: 200,
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