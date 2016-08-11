/**
 * Created by yongliu on 7/28/16.
 */

Ext.define('Pago.util.Utils', {
  alternateClassName: 'Pago.Utils',
  statics: {

    trim: function(str) {
      var str = str.replace(/^\s\s*/, ''),
        ws = /\s/,
        i = str.length;
      while (ws.test(str.charAt(--i)));
      return str.slice(0, i + 1);
    },

    /**
     * validator trim (#allowBlank : false)
     * @param v
     */
    trimValidator: function (v) {
      //v = v ? v : Ext.String.trim('' + v);
      if (this.minLength > 0 && v && v.length >= this.minLength && Ext.String.trim(v).length < this.minLength) {
        return Ext.String.format("最小长度为{0}, 请输入不少于{0}个字符", this.minLength);
      } else if (v && Ext.String.trim(v) == '') {
        return '非法值,不能全是空格';
      } else {
        return true;
      }
    },
    
    booleanRenderer: function(v, metaData, rec, rowIdx, colIdx, store, view){
      if(v){
        return 'Y';
      }
      return 'N';
    }
    
    
  }
});