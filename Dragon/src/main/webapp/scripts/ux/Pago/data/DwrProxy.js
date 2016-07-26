/**
 * Created by IntelliJ IDEA.
 * User: tony liu
 * Date: 11-07-12
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Pago.data.DwrProxy', {
  /* Begin Definitions */

  extend: 'Ext.data.proxy.Server',
  alternateClassName: ['Pago.data.proxy.Dwr','Pago.proxy.DwrProxy'],

  alias: 'proxy.dwr',

  params: undefined,

  statics: {
    isBatching: false,

    addBatchCall: function (obj, fn, args) {
      if (!this.isBatching) {
        this.isBatching = true;
        delete this.batchCalls;
        this.batchCalls = [];
        setTimeout('Pago.data.DwrProxy.executeBatchCall()', 2000);
      }
      this.batchCalls.push({obj: obj, fn: fn, args: args});
    },

    executeBatchCall: function () {
      // console.log('executeBatchCall -- start');
      var calls = this.batchCalls;
      this.isBatching = false;
      dwr.engine.beginBatch();
      for (var i = 0; i < calls.length; i++) {
        var call = calls[i];
        call.fn.apply(call.obj, call.args);
      }
      dwr.engine.endBatch(true);
      // console.log('executeBatchCall -- end');
    }
  },

  /* End Definitions */

  constructor: function(config){
    var me = this;

    Ext.apply(me, config);

    me.callParent(arguments);
  },

  doRequest: function(operation, callback, scope) {
    var me = this,
      writer = me.getWriter(),
      request = me.buildRequest(operation, callback, scope),
      fn = me.api[request.action],
      args = [],
      params = request.params;

    //<debug>
    if (!fn) {
      Ext.Error.raise('No dwr function specified for this proxy');
    }
    //</debug>

    if (operation.allowWrite()) {
      request = writer.write(request);
      args.push(Ext.pluck(operation.records, 'data') || []);
    }

    if(Ext.isDefined(this.params)){
      params = Ext.apply(params, this.params);
    }
    args.push(params);

    args.push(me.createRequestCallback(request, operation, callback, scope));

    if (operation.useBatching || this.useBatching) {
      Pago.data.DwrProxy.addBatchCall(window, fn, args);
    }
    else {
      fn.apply(window, args);
    }
  },

  createRequestCallback: function(request, operation, callback, scope){
    var me = this;

    return function(response){
      me.processResponse(response.success, operation, request, response, callback, scope);
    };
  },

  // inherit docs
  extractResponseData: function(response){
    return Ext.isDefined(response.result) ? response.result : response;
  },

  // inherit docs
  setException: function(operation, response) {
    operation.setException(response.message);
  },

  setParams: function(params){
    this.params = Ext.apply(params, this.params|| {});
  },

  setExtraParam: function (name, value){
    this.extraParams = this.extraParams || {};
    this.extraParams[name] = value;
    this.applyEncoding(this.extraParams);
  },

  // inherit docs
  buildUrl: function(){
    return '';
  }
});