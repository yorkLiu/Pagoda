/**
 * Created by yongliu on 7/27/16.
 */




Ext.define('Pagoda.configuration.view.ConfigRepository', {
  extend: 'Ext.tab.Panel',
  alias: 'widget.configrepository',
  requires: [
    'Pagoda.configuration.view.AddressList',
    'Pagoda.configuration.view.AccountList',
    'Pagoda.configuration.view.CommissionList'
  ],
  
  initComponent: function(){
    var me = this;
    
    Ext.applyIf(this, {

      activeItem: 0,
      closable: false,
      items: [
        {
          title: '地址管理',
          xtype: 'addresslist',
          itemId: 'addressItem'
        },
        {
          title: '账号管理',
          xtype: 'accountlist',
          itemId: 'accountItem'
        },
        {
          title: '拥金管理',
          xtype: 'commissionlist',
          itemId: 'commissionItem'
        }
      ]
      
    });
    
    
    this.callParent();
  }

});