/**
 * Created by yongliu on 7/27/16.
 */




Ext.define('Pagoda.configuration.view.ConfigRepository', {
  extend: 'Ext.tab.Panel',
  alias: 'widget.configrepository',
  requires: [
    'Pagoda.configuration.view.AddressList',
    'Pagoda.configuration.view.AccountList'
  ],
  
  initComponent: function(){
    var me = this;
    
    Ext.applyIf(this, {

      activeItem: 0,
      closable: false,
      items: [
        {
          title: '地址信息',
          xtype: 'addresslist',
          itemId: 'addressItem'
        },
        {
          title: '账号信息',
          xtype: 'accountlist',
          itemId: 'accountItem'
        }
      ]
      
    });
    
    
    this.callParent();
  }

});