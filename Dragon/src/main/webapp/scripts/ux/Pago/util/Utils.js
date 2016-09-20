/**
 * Created by yongliu on 7/28/16.
 */

Ext.define('Pago.util.Utils', {
  alternateClassName: 'Pago.Utils',
  statics: {
    
    readOnlyTitleTpl: '&nbsp;[<span class="highlight-text-readOnly">只读</span>]',

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
        return '是';
      }
      return '否';
    },

    /**
     * 
     * @param value input value
     * @param numberMaxLength integer max length
     * @param decimalSeparator default is '.'
     */
    checkNumberFieldIntegerLength: function(value, numberMaxLength, decimalSeparator){
      decimalSeparator = decimalSeparator || '.';
      if(value){
        var  precisionPart = value.split(decimalSeparator)[0];
        if(precisionPart.length > numberMaxLength){
          return Ext.String.format('整数部分最大允许输入{0}位', numberMaxLength);
        }
      }
      return true;
    },

    markReadOnlyTitle : function (title){
      var activeTemplate = new Ext.Template(this.readOnlyTitleTpl);
      var htmlSeg,activeTitle;
      activeTitle = activeTemplate.apply(htmlSeg);
      return title + activeTitle;
    }
    
    
  }
});