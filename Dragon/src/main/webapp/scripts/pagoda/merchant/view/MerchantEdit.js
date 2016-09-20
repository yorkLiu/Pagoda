/**
 * Created by yongliu on 9/14/16.
 */

Ext.define('Pagoda.merchant.view.MerchantEdit', {
  extend: 'Ext.window.Window',
  alias: 'widget.merchantedit',
  requires: [
    'Ext.form.Panel',
    'Ext.form.field.Text',
    'Ext.form.field.Date',
    'Ext.form.field.Number',
    'Ext.button.Button'
  ],

  activeRecord: undefined,
  layout: 'fit',

  saveRecord: Ext.emptyFn,
  addRecord: Ext.emptyFn,
  
  existsNameErrorMsg: '别名[<span style="color:blue">{0}</span>]已存在,请重新输入',
  
  initComponent: function () {
    var me = this,
      today = new Date();

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
            fieldLabel: '别名',
            name: 'name',
            allowBlank: false,
            maxLength: 200,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: '商家名称',
            name: 'merchantName',
            allowBlank: false,
            maxLength: 200,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: '商家编号',
            name: 'merchantNo',
            maxLength: 50,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: '商铺首页地址',
            name: 'indexUrl',
            maxLength: 255,
            validator: Pago.Utils.trimValidator
          },
          {
            xtype: 'numberfield',
            fieldLabel: '刷单数量',
            name: 'totalCount',
            allowBlank: false,
            maxValue: 50000,
            minValue: 1,
            allowDecimals: false,
            listeners: {
              'blur': function(){
                var v = this.value;
                var commentCountField = me.down('#commentCount');
                if(commentCountField){
                  commentCountField.setValue(v);
                  if(v){
                    commentCountField.setMaxValue(v)
                  }
                }
              }
            }
          },
          {
            xtype: 'numberfield',
            fieldLabel: '评价数量',
            name: 'commentCount',
            itemId: 'commentCount',
            allowBlank: false,
            maxValue: 50000,
            minValue: 0,
            allowDecimals: false
          },
          {
            xtype: 'datefield',
            fieldLabel: '开始日期',
            name: 'scheduleDate',
            format: 'Y-m-d',
            value: today,
            minValue: today,
            maxValue: Ext.Date.add(today, Ext.Date.DAY, 7)
          },
          {
            xtype: 'timefield',
            fieldLabel: '开始时间',
            name: 'scheduleTime',
            minValue: '8:00 AM',
            maxValue: '11:00 PM',
            increment: 30
          },
          {
            xtype: 'combo',
            name: 'addressOption',
            fieldLabel: '首选地址',
            displayField: 'label',
            valueField: 'value',
            queryMode: 'local',
            triggerAction: 'all',
            value: 'ALL',
            store: me.getAddressOptionStore()
          },
          {
            xtype: 'numberfield',
            fieldLabel: '下单N天后收货',
            name: 'confirmReceiptDelay',
            allowBlank: false,
            maxValue: 15,
            minValue: 1,
            value: 4,
            allowDecimals: false,
            listeners:{
              'blur': function(){
                var v = this.value;
                var commentDelayField = me.down('#commentDelay');
                if(commentDelayField){
                  commentDelayField.setValue(v);
                  if(v){
                    commentDelayField.setMinValue(v);
                  }
                }
              }
            }
          },
          {
            xtype: 'numberfield',
            fieldLabel: '下单N天后评价',
            name: 'commentDelay',
            itemId: 'commentDelay',
            allowBlank: false,
            maxValue: 15,
            minValue: 4,
            value: 4,
            allowDecimals: false
          },
          
          {
            xtype: 'container',
            anchor: '100%',
            layout: 'hbox',
            padding: '3 3 3 105',
            items:[
              {
                xtype: 'checkbox',
                fieldLabel: '',
                boxLabel: '团购',
                name: 'groupBuy',
                inputValue: 'true',
                width: 80,
                handler: function(checkbox, checked){
                  var field = me.down('#groupBuyIndexUrl');
                  if(field){
                    field[checked ? 'show' : 'hide']();
                    field[checked ? 'enable' : 'disable']();
                  }
                }
              },
              {
                xtype: 'checkbox',
                fieldLabel: '',
                boxLabel: '闪购',
                name: 'flashBuy',
                inputValue: 'true',
                width: 80,
                handler: function(checkbox, checked){
                  var field = me.down('#flashBuyIndexUrl');
                  if(field){
                    field[checked ? 'show' : 'hide']();
                    field[checked ? 'enable' : 'disable']();
                  }
                }
              },
              {
                xtype: 'checkbox',
                boxLabel: '海购',
                name: 'overseas',
                inputValue: 'true',
                width: 80
              },
              {
                xtype: 'checkbox',
                boxLabel: '快速下单(直接点击商品链接)',
                name: 'quickOrder',
                inputValue: 'true'
              }
            ]
          },
          {
            fieldLabel: '团购入口地址',
            name: 'groupBuyIndexUrl',
            itemId: 'groupBuyIndexUrl',
            allowBlank: false,
            disabled: true,
            hidden: true,
            maxLength: 255,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: '闪购入口地址',
            name: 'flashBuyIndexUrl',
            itemId: 'flashBuyIndexUrl',
            allowBlank: false,
            disabled: true,
            hidden: true,
            maxLength: 255,
            validator: Pago.Utils.trimValidator
          },
          {
            xtype: 'numberfield',
            fieldLabel: '每单间隔时间(分)',
            name: 'intervalForOrder',
            allowBlank: false,
            maxValue: 15,
            minValue: 1,
            value: 3,
            allowDecimals: false
          },
          {
            xtype: 'numberfield',
            fieldLabel: '在商品页面停留时间(分)',
            name: 'viewingTimeForOrder',
            allowBlank: false,
            maxValue: 15,
            minValue: 1,
            value: 1,
            allowDecimals: false
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
        ],
        listeners: {
          dirtychange: function(form, dirty){
            me.down('button[action=save]')[dirty ? 'enable' : 'disable']()
          }
        }
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

  getAddressOptionStore: function(){
    var store = Ext.StoreManager.lookup('PagodaAddressOptionStore');
    if(!store || store == null){
      store = Ext.create('Ext.data.ArrayStore', {
        storeId: 'PagodaAddressOptionStore',
        fields: ['label', 'value'],
        data: [
          ['全部', 'ALL'],
          ['北上广', '北上广'],
          ['江淅沪', '江淅沪']
        ]
      });
    }
    return store;
  },

  checkUniqueName: function(){
    var me = this,
      form = me.down('#form').getForm(),
      values = form.getValues(),
      ret = false;
    
    var checkValues = {
      name : values.name,
      id: values.id
    };
    
    if(values.name){
      merchantController.checkUniqueName(checkValues, {
        async: false,
        callback: function(resp){
          ret = resp.success;
        }
      });
    }
    
    return ret;
  },

  onSaveHandler: function(){
    var me = this,
      form = me.down('#form').getForm(),
      values = form.getValues();
    if(form.isValid()){
      if(me.checkUniqueName()){
        if(me.activeRecord && me.activeRecord != null){
          form.updateRecord(me.activeRecord);
        } else {
          me.addRecord(values);
        }
        me.saveRecord();
      } else {
        var errorMsg = Ext.String.format(me.existsNameErrorMsg, values.name);
        var nameField = form.findField('name');
        Ext.MessageBox.show({
          title: me.title,
          msg: errorMsg,
          closable: false,
          icon: Ext.MessageBox.WARNING,
          buttons: Ext.MessageBox.OK,
          fn: function (btn) {
            nameField.markInvalid(errorMsg);
            nameField.focus();
          }
        });
      }
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
