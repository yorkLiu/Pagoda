/**
 * Created by yongliu on 8/22/16.
 */

Ext.define('Pagoda.configuration.view.CommissionEdit', {
  extend: 'Ext.window.Window',
  alias: 'widget.commissionedit',
  uses: [
    'Ext.form.Panel',
    'Ext.form.field.Text',
    'Ext.form.field.Date',
    'Ext.form.field.Number',
    'Ext.button.Button',
    'Pago.access.RoleAccess'
  ],

  activeRecord: undefined,

  layout: 'fit',
  addRecord: Ext.emptyFn,
  saveRecord: Ext.emptyFn,
  
  initComponent: function () {
    var me = this;
    
    this.items = [
      {
        xtype: 'form',
        itemId: 'form',
        //frame: true,
        border: false,
        margin: '3 3 3 3',
        autoScroll: true,
        defaults: {
          xtype: 'numberfield',
          anchor: '95%',
          labelWidth: 100
        },
        trackResetOnLoad:true,
        items: me.getCommissionItems(),
        buttons: [
          {
            text: globalRes.buttons.save,
            scope: me,
            formBind: true,
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
  
  getAppTypeStore: function(){
    var appTypeStore = Ext.StoreManager.lookup('PagodaAppTypeStore');
    return appTypeStore;
  },
  
  
  getCommissionItems: function(){
    var me = this,
      items = [];
    
    items = items.concat(
      [
        {
          xtype: 'hidden',
          name: 'id'
        },
        {
          xtype: 'textfield',
          fieldLabel: '名称',
          name: 'description',
          maxLength: 200,
          allowBlank: false,
          maskRe: /[0-9a-zA-Z-_\s]/,
          validator: Pago.Utils.trimValidator,
          plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)})
        },
        {
          xtype: 'combo',
          fieldLabel: '所属平台',
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
          fieldLabel: '每增加一个商品加拥金',
          name: 'priceForOneMoreOffset',
          allowDecimals: true,
          decimalPrecision: 2,
          numberMaxLength: 10,
          minValue: 0,
          allowBlank: false,
          plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)}),
          validator: function(value){
            return Pago.Utils.checkNumberFieldIntegerLength(value, this.numberMaxLength);
          }
        }
      ]
    );
    
    
    for (var i = 1; i <= 5; i++) {
      var allowCommission = {
        xtype: 'checkbox',
        height: 24,
        name: 'allowCommission' + i,
        inputValue: true,
        labelSeparator: '',
        fieldLabel: '&nbsp;',
        boxLabel: Ext.String.format('拥金{0}详细信息', i),
        partnerItemId: 'commission' + i,
        partnerItemId2: 'dealPrice' + i,
        plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)}),
        handler: function (checkbox, checked) {
          me.down('#' + this.partnerItemId)[checked ? 'show' : 'hide']();
          me.down('#' + this.partnerItemId2)[checked ? 'enable' : 'disable']();
        }
      };
      var commissionInfoItem = {
        xtype: 'container',
        padding: '3 0 3 120',
        itemId: 'commission' + i,
        height: 90,
        hidden: true,
        items: [
          {
            xtype: 'numberfield',
            fieldLabel: '成交价低于(含)',
            labelWidth: 130,
            width: 285,
            name: 'dealPrice'+i,
            itemId: 'dealPrice'+i,
            allowDecimals: true,
            decimalPrecision: 2,
            numberMaxLength: 10,
            minValue: 0,
            allowBlank: false,
            disabled: true,
            plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)})
          },
          {
            xtype: 'container',
            height: 30,
            layout: 'hbox',
            items: [
              {
                xtype: 'radio',
                name: 'allowCommissionType'+i,
                boxLabel: '拥金(元)',
                inputValue: 1,
                partnerItemId: 'commissionAmount' + i,
                plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)}),
                handler: function (radio, checked) {
                  me.down('#' + this.partnerItemId)[checked ? 'enable' : 'disable']();
                }
              },
              {
                xtype: 'numberfield',
                name: 'commissionAmount'+i,
                itemId: 'commissionAmount'+i,
                padding: '0 0 0 68',
                width: 150,
                allowDecimals: true,
                decimalPrecision: 2,
                numberMaxLength: 10,
                allowBlank: false,
                minValue: 0,
                disabled: true,
                plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)}),
                validator: function (value) {
                  return Pago.Utils.checkNumberFieldIntegerLength(value, this.numberMaxLength);
                }
              }
            ]
          },
          {
            xtype: 'container',
            height: 30,
            layout: 'hbox',
            items: [
              {
                xtype: 'radio',
                name: 'allowCommissionType'+i,
                boxLabel: Ext.String.format('拥金{0}(百分比):', i),
                inputValue: 2,
                partnerItemId: 'commissionPercentage'+i,
                plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)}),
                handler: function (radio, checked) {
                  me.down('#'+this.partnerItemId)[checked ? 'enable' : 'disable']();
                }
              },
              {
                xtype: 'numberfield',
                name: 'commissionPercentage'+i,
                itemId: 'commissionPercentage'+i,
                padding: '0 0 0 32',
                width: 150,
                allowDecimals: false,
                allowBlank: false,
                disabled: true,
                plugins: Ext.create('Pago.ux.RoleAccess', {featureName: '#allowAll#', disableAll: (me.readOnly)}),
                validator: function (v) {
                  // minValue = 1, maxValue = 100 and maxLength = 3
                  if (v && (v.length > 3 || v <= 0 || v > 100)) {
                    return '非法数值,请输入1~100';
                  }
                  return true;
                }
              },
              {
                xtype: 'displayfield',
                padding: '0 0 0 5',
                value: '% (E.g: 1, 2, 100)'
              }
            ]
          }

        ]
      };


      items.push(allowCommission);
      items.push(commissionInfoItem);
    }
    
    
    return items;
  },
  
  
  checkUniqueName: function(data){
    var result = true;
    commissionController.checkUniqueName(data, {
      async: false,
      callback: function(resp){
        result = resp.success;
      }
    });
    return result;
  },
  
  onSaveHandler: function () {
    var me = this,
      form = this.down('form').getForm(),
      values = form.getValues();
    
    if(form.isValid() && me.checkUniqueName(values)){
      if (me.activeRecord && me.activeRecord != null) {
        form.updateRecord(me.activeRecord);
      } else {
        me.addRecord(values);
      }
      me.saveRecord();
    } else {
      var nameInvalidMsg = Ext.String.format('{0}已经存在, 请重新输入', values.description);
      Ext.MessageBox.show({
        title: me.title,
        msg: nameInvalidMsg,
        closable: false,
        icon: Ext.MessageBox.WARNING,
        buttons: Ext.MessageBox.OK,
        fn: function (btn) {
          form.findField('description').markInvalid(nameInvalidMsg);
        }
      });
    }
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