/**
 * Created by yongliu on 9/22/16.
 */

Ext.define('Pagoda.merchant.view.PreOrderEdit', {
  extend: 'Ext.window.Window',
  alias: 'widget.preorderedit',
  requires: [
    'Ext.form.Panel',
    'Ext.form.field.Text',
    'Ext.form.field.TextArea',
    'Ext.form.field.Number',
    'Ext.button.Button',
    'Pagoda.merchant.view.PreOrderItemList'
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
          anchor: '98%',
          labelWidth: 100
        },
        trackResetOnLoad: true,
        items: [
          {
            xtype: 'hidden',
            name: 'id'
          },
          {
            xtype: 'hidden',
            name: 'merchantId',
            value: me.merchantId
          },
          {
            xtype: 'combo',
            fieldLabel: '刷单平台',
            name: 'appTypeId',
            displayField: 'description',
            valueField: 'id',
            queryMode: 'local',
            triggerAction: 'all',
            editable: false,
            store: me.getAppTypeStore(),
            allowBlank: false,
            plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)})
          },
          {
            xtype: 'numberfield',
            name: 'totalCount',
            fieldLabel: '刷单数量',
            allowDecimals: false,
            minValue: 1,
            maxValue: 10000,
            allowBlank: false
          },
          {
            xtype: 'preorderitemlist',
            itemId: 'preorderitemlist',
            height: 300,
            border: true,
            readOnly: me.readOnly,
            preData: me.activeRecord ? me.activeRecord.get('items'): null
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
      }];

    this.callParent();
  },

  afterRender: function(){
    this.callParent();
    if (this.activeRecord && this.activeRecord != null) {
      this.down('form').getForm().loadRecord(this.activeRecord);
    }
  },

  getAppTypeStore: function(){
    var appTypeStore = Ext.StoreManager.lookup('PagodaAppTypeStore');
    return appTypeStore;
  },

  onSaveHandler: function(){
    var me = this,
      form = me.down('form').getForm(),
      orderListField = me.down('#preorderitemlist');
    
    if(form.isValid()){
      var productInfos = orderListField.getSubmitValues();

      console.log(productInfos);
      
      if(productInfos == null || productInfos.length <=0){
        Ext.MessageBox.show({
          title: me.title,
          msg: '请添加至少一个商品',
          icon: Ext.MessageBox.ERROR,
          buttons: Ext.MessageBox.OK
        });
      } else {
        var values = form.getValues();
        values['items'] = productInfos;
        
        if(me.activeRecord){
          form.updateRecord(me.activeRecord);
          me.activeRecord.set('items', productInfos);
        } else {
          me.addRecord(values);
        }
        me.saveRecord();
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