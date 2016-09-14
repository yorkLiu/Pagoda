/**
 * Created with app source gen maven plugin
 * author: @author
 * Date: @Date
 * Time: @Time
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Pagoda.userrole.view.UserEdit', {
  extend: 'Ext.window.Window',
  alias: 'widget.useredit',
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
        border: false,
        height: '100%',
        bodyPadding: 5,
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
            fieldLabel: userRoleRes.fields.username,
            name: 'username',
            allowBlank: false,
            emptyText: 'Please input username',
            maxLength: 50,
            minLength: 5,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: userRoleRes.fields.firstName,
            name: 'firstName',
            maxLength: 50,
            allowBlank: false,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: userRoleRes.fields.lastName,
            name: 'lastName',
            maxLength: 50,
            allowBlank: false,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: userRoleRes.fields.password,
            type: 'password',
            name: 'password',
            maxLength: 100,
            allowBlank: false,
            disabled: me.activeRecord ? true : false,
            hidden: me.activeRecord ? true : false,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: userRoleRes.fields.confirmPassword,
            type: 'password',
            name: 'confirmPassword',
            maxLength: 100,
            allowBlank: false,
            disabled: me.activeRecord ? true : false,
            hidden: me.activeRecord ? true : false,
            validator: function(v){
              var password = this.previousSibling('[name=password]');
              return (v === password.getValue()) ? true : userRoleRes.error.newPasswordNotMatch
            }
          },
          {
            fieldLabel: userRoleRes.fields.passwordHint,
            name: 'passwordHint',
            maxLength: 200,
            allowBlank: true
          },
          {
            fieldLabel: userRoleRes.fields.email,
            name: 'email',
            vtype: 'email',
            maxLength: 200,
            allowBlank: false
          },

          {
            fieldLabel: userRoleRes.fields.telephone,
            name: 'telephone',
            maxLength: 20,
            allowBlank: false,
            validator: Pago.Utils.trimValidator
          },
          {
            fieldLabel: userRoleRes.fields.telephone2,
            name: 'telephone2',
            maxLength: 20,
            allowBlank: true,
            validator: Pago.Utils.trimValidator
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