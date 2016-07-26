Ext.define('Pagoda.store.Base', {
  extend: 'Ext.data.Store',
  alias: 'store.base',

  constructor: function (config) {
    var me = this;

    Ext.apply(this, config);
    this.callParent();

    if (this.baseParams && me.proxy) {
      var fn = function (param) {
        for (var v in param) {
          if (typeof(param[v]) != 'function') {
            me.proxy.extraParams[v] = param[v];
          }
        }
      }
      if (Ext.isArray(this.baseParams)) {
        Ext.each(this.baseParams, fn);
      }
      else {
        fn(this.baseParams);
      }
    }
  },

  setBaseParams: function (k, v) {
    if (this.proxy)
      this.proxy.extraParams[k] = v;
  },

  getBaseParamValue: function (k) {
    if (this.proxy)
      return this.proxy.extraParams[k];
  },

  reload: function (config) {
    var params = this.proxy.extraParams;
    if (params && params != null) {
      this.proxy.extraParams = params;
      if (config && config != null)
        this.load(config);
      else
        this.load();
    }
  }
});