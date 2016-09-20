/**
 * Created with app source gen maven plugin
 * author: @author
 * Date: @Date
 * Time: @Time
 * To change this template use File | Settings | File Templates.
 */


Ext.define('Pagoda.App', {
  extend: 'Ext.ux.desktop.App',

  requires: [
    'Ext.window.MessageBox',
    'Ext.tip.QuickTipManager',

    'Ext.ux.desktop.ShortcutModel',
    'Pagoda.Settings',
    
    'Pago.util.Utils',

    // import modules
    'Pagoda.UserRole',
    'Pagoda.Configuration',
    'Pagoda.Merchant'

    // todo import you modules
  ],

  constructor: function (config) {
    this.callParent(config);

    ///**
    // * Fixed the issues of Ext.ux.desktop.Desktop.
    // * Issue 1: The shortcuts are displayed as only one column, no matter how many;
    // * Issue 2: The desktop do not redraw the shortcut when resizing browser.
    // */
    Ext.override(Ext.ux.desktop.Desktop, {
      createDataView: function () {
        var me = this;
        return {
          xtype: 'dataview',
          overItemCls: 'x-view-over',
          trackOver: true,
          itemSelector: me.shortcutItemSelector,
          store: me.shortcuts,
          style: {
            position: 'absolute'
          },
          x: 0, y: 0,
          tpl: new Ext.XTemplate(me.shortcutTpl),
          listeners: {
            'refresh': me.positionItems,
            'resize': me.positionItems
          }
        };
      },
    
      positionItems: function() {
        var me = this,
          height = this.getHeight(),
          x = 0,
          y = 0;
        ////////// fixed the icon hide in IE./////////////////////////
        if(Ext.isIE){
          var cssHeight = me.getEl().getStyle('height');
          if(cssHeight){
            height = parseFloat(cssHeight.replace(/px/gi, ''));
          }
        }
        ///////////////////////////////////////////////////////////
        if (!me.itemWidth && !me.itemHeight) {
          me.itemHeight = 0;
          var h = 0, rh;
          this.all.each(function(item) {
            var el = Ext.get(item),
              box = el.getBox();
            me.itemWidth = me.itemWidth || box.right;
            me.itemHeight = (rh = box.bottom - h) > me.itemHeight ? rh : me.itemHeight;
            h = box.bottom;
          });
        }
    
        this.all.each(function(item) {
          var el = Ext.get(item);
          if ((y + me.itemHeight) > height) {
            x += me.itemWidth;
            y = 0;
          }
          el.setXY([x, y]);
          y += me.itemHeight;
        });
      }
    });

    /**
     * @Override Ext.form.field.Base
     * @For show indicator for required field
     */
    Ext.override(Ext.form.field.Base,{
      childEls: [
      /**
       * @property {Ext.Element} labelCell
       * The `<TD>` Element which contains the label Element for this component. Only available after the component has been rendered.
       */
        'labelCell',

      /**
       * @property {Ext.Element} labelEl
       * The label Element for this component. Only available after the component has been rendered.
       */
        'labelEl',

      /**
       * @property {Ext.Element} bodyEl
       * The div Element wrapping the component's contents. Only available after the component has been rendered.
       */
        'bodyEl',

        // private - the TD which contains the msgTarget: 'side' error icon
        'sideErrorCell',

      /**
       * @property {Ext.Element} errorEl
       * The div Element that will contain the component's error message(s). Note that depending on the configured
       * {@link #msgTarget}, this element may be hidden in favor of some other form of presentation, but will always
       * be present in the DOM for use by assistive technologies.
       */
        'errorEl',

        'inputRow',

        'showRequiredIndicatorEl',
        
         'imageBtnEl'
      ],

      /**
       * Generates the arguments for the field decorations {@link #labelableRenderTpl rendering template}.
       * @return {Object} The template arguments
       * @protected
       */
      getLabelableRenderData: function() {
        var me = this,
          data,
          tempEl,
          topLabel = me.labelAlign === 'top';

        if (!Ext.form.Labelable.errorIconWidth) {
          tempEl = Ext.getBody().createChild({style: 'position:absolute', cls: Ext.baseCSSPrefix + 'form-invalid-icon'});
          Ext.form.Labelable.errorIconWidth = tempEl.getWidth() + tempEl.getMargin('l');
          tempEl.remove();
        }

        var showRequiredIndicator = false;
        if (false == me.allowBlank && me.getXType() != 'hiddenfield' && me.getXType() != 'displayfield' && !me.hideRequiredIndicator) {
          showRequiredIndicator = true;
        }

        var allowBlankTips = '<span class="required-filed-mark" data-qtip="' + me.blankText + '" data-qclass="qtip-wrap">*</span>';
        if (me.qtipAlign === 'left') {
          allowBlankTips = '<span class="required-filed-mark" data-qtip="' + me.blankText + '" data-qclass="qtip-wrap" data-qalign="tr-bl">*</span>';
        }

        data = Ext.copyTo({
            inFormLayout   : me.ownerLayout && me.ownerLayout.type === 'form',
            inputId        : me.getInputId(),
            labelOnLeft    : !topLabel,
            hideLabel      : !me.hasVisibleLabel(),
            fieldLabel     : me.getFieldLabel(),
            labelCellStyle : me.getLabelCellStyle(),
            labelCellAttrs : me.getLabelCellAttrs(),
            labelCls       : me.getLabelCls(),
            labelStyle     : me.getLabelStyle(),
            bodyColspan    : me.getBodyColspan(),
            externalError  : !me.autoFitErrors,
            errorMsgCls    : me.getErrorMsgCls(),
            errorIconWidth : Ext.form.Labelable.errorIconWidth,
            showRequiredIndicator:showRequiredIndicator,
            allowBlankTips:allowBlankTips
          },
          me, me.labelableRenderProps, true);

        me.getInsertionRenderData(data, me.labelableInsertions);

        return data;
      },

      toggleRequiredIndicator:function(hide){
        var me = this;
        if(hide){
          me.showRequiredIndicatorEl.setStyle('display','none');
        } else {
          me.showRequiredIndicatorEl.setStyle('display','table-cell');
        }
      },

      labelableRenderTpl: [

        // body row. If a heighted Field (eg TextArea, HtmlEditor, this must greedily consume height.
        '<tr role="presentation" id="{id}-inputRow" <tpl if="inFormLayout">id="{id}"</tpl> class="{inputRowCls}">',

        // Label cell
        '<tpl if="labelOnLeft">',
        '<td role="presentation" id="{id}-labelCell" style="{labelCellStyle}" {labelCellAttrs}>',
        '{beforeLabelTpl}',
        '<label id="{id}-labelEl" {labelAttrTpl}<tpl if="inputId"> for="{inputId}"</tpl> class="{labelCls}"',
        '<tpl if="labelStyle"> style="{labelStyle}"</tpl>',
        // Required for Opera
        ' unselectable="on"',
        '>',
        '{beforeLabelTextTpl}',
        '<tpl if="fieldLabel">{fieldLabel}{labelSeparator}</tpl>',
        '{afterLabelTextTpl}',
        '</label>',
        '{afterLabelTpl}',
        '</td>',
        '</tpl>',

        // Body of the input. That will be an input element, or, from a TriggerField, a table containing an input cell and trigger cell(s)
        '<td role="presentation" class="{baseBodyCls} {fieldBodyCls} {extraFieldBodyCls}" id="{id}-bodyEl" colspan="{bodyColspan}" role="presentation">',
        '{beforeBodyEl}',

        // Label just sits on top of the input field if labelAlign === 'top'
        '<tpl if="labelAlign==\'top\'">',
        '{beforeLabelTpl}',
        '<div role="presentation" id="{id}-labelCell" style="{labelCellStyle}">',
        '<label id="{id}-labelEl" {labelAttrTpl}<tpl if="inputId"> for="{inputId}"</tpl> class="{labelCls}"',
        '<tpl if="labelStyle"> style="{labelStyle}"</tpl>',
        // Required for Opera
        ' unselectable="on"',
        '>',
        '{beforeLabelTextTpl}',
        '<tpl if="fieldLabel">{fieldLabel}{labelSeparator}</tpl>',
        '{afterLabelTextTpl}',
        '</label>',
        '</div>',
        '{afterLabelTpl}',
        '</tpl>',

        '{beforeSubTpl}',
        '{[values.$comp.getSubTplMarkup(values)]}',
        '{afterSubTpl}',

        // Side required indicator
        '<tpl if="showRequiredIndicator==true">',
        '<td id="{id}-showRequiredIndicatorEl" width="13" style="width: 13px;"><div style="padding: 5px 0px 0px 3px;">{allowBlankTips}</div></td>',
        '</tpl>',

        //Image button element
        '<td id="{id}-imageBtnEl" style="display:none;padding: 2px 0px 0px 3px;" width="19" ></td>',

        // Final TD. It's a side error element unless there's a floating external one
        '<tpl if="msgTarget===\'side\'">',
        '{afterBodyEl}',
        '</td>',
        '<td role="presentation" id="{id}-sideErrorCell" vAlign="{[values.labelAlign===\'top\' && !values.hideLabel ? \'bottom\' : \'middle\']}" style="{[values.autoFitErrors ? \'display:none\' : \'\']}" width="{errorIconWidth}">',
        '<div role="presentation" id="{id}-errorEl" class="{errorMsgCls}" style="display:none"></div>',
        '</td>',
        '<tpl elseif="msgTarget==\'under\'">',
        '<div role="presentation" id="{id}-errorEl" class="{errorMsgClass}" colspan="2" style="display:none"></div>',
        '{afterBodyEl}',
        '</td>',
        '</tpl>',
        '</tr>',
        {
          disableFormats: true
        }
      ]

    });


    Ext.override(Ext.form.field.Text, {
      /**
       * @override
       */
      onBlur: function () {
        this.callOverridden();
        if (this.xtype === 'textfield' && this.getValue() && this.getValue() != '') {
          this.setValue(Ext.String.trim(this.getValue()));
        }
      },

      /**
       * @override
       * @param value
       * @returns {*}
       */
      setValue: function (value) {
        var me = this,
          inputEl = me.inputEl;

        if (value && Ext.isString(value)
          && (me.xtype == 'textfield' || me.xtype == 'textareafield' || me.xtype == 'textarea')
          && (value.indexOf('&lt;') != -1 || value.indexOf('&gt;') != -1)) {
          value = value.replace(/&lt;/gi, '<').replace(/&gt;/gi, '>');
        }
        if (inputEl && me.emptyText && !Ext.isEmpty(value)) {
          inputEl.removeCls(me.emptyCls);
        }

        me.callParent(arguments);
        me.applyEmptyText();
        return me;
      },

      /**
       * @override
       * @returns {*}
       */
      getValue: function () {
        var me = this,
          val = me.rawToValue(me.processRawValue(me.getRawValue()));

        if (me.allowEncodeHtml && val && Ext.isString(val)
          && (me.xtype == 'textfield' || me.xtype == 'textareafield' || me.xtype == 'textarea')
          && (val.indexOf('<') != -1 || val.indexOf('>') != -1)) {
          val = val.replace(/</gi, '&lt;').replace(/>/gi, '&gt;');
        }

        me.value = val;
        return val;
      }
    });

    Ext.override(Ext.form.field.TextArea,{
      /**
       * @Override
       */
      onBlur: function(){
        this.callOverridden();
        if(this.xtype==='textarea' && this.getValue() && this.getValue() != ''){
          this.setValue(Ext.String.trim(this.getValue()));
        }
      }
    });
    
  },

  init: function() {
    this.callParent();
  },

  getModules : function(){

    // todo add your modules

    return [
      new Pagoda.UserRole(),
      new Pagoda.Configuration(),
      new Pagoda.Merchant()
    ];
//      eg:
//    return [
//      new Pagoda.VideoWindow(),
//      new Pagoda.SystemStatus(),
//      new Pagoda.GridWindow(),
//      new Pagoda.TabWindow()
//    ];

    return [

    ];
  },

  getDesktopConfig: function () {
    var me = this, ret = me.callParent();

    return Ext.apply(ret, {
      //cls: 'ux-desktop-black',
      contextMenuItems: [
        { text: '更改设置', handler: me.onSettings, scope: me }
      ],

      shortcuts: Ext.create('Ext.data.Store', {
        model: 'Ext.ux.desktop.ShortcutModel',
        data: [
          // todo add your module @module: 'your defined module id'
          { name: '用户与权限管理', iconCls: 'userRole-shortcut', module: 'userRole-win' },
          { name: '配置管理', iconCls: 'configuration-shortcut', module: 'configuration-win' },
          { name: '商家信息管理', iconCls: 'configuration-shortcut', module: 'merchant-win' }
        ]
      }),

      wallpaper: basePath + 'scripts/desktop/wallpapers/desk.jpg',
      wallpaperStretch: false
    });
  },

  // quick task bar
  getTaskbarConfig: function () {
    var ret = this.callParent();

    return Ext.apply(ret, {
      quickStart: [
        // todo add module on quick start bar
        // eg:
//        { name: 'Accordion Window', iconCls: 'accordion', module: 'acc-win' },
//        { name: 'Grid Window', iconCls: 'icon-grid', module: 'grid-win' }
      ],
      trayItems: [
        { xtype: 'trayclock', flex: 1 }
      ]
    });
  },

  // config for the start menu
  getStartConfig : function() {
    var me = this, ret = me.callParent();

    return Ext.apply(ret, {
      title: 'Don Griffin',
      iconCls: 'user',
      height: 300,
      toolConfig: {
        width: 100,
        items: [
          {
            text:'设置',
            iconCls:'settings',
            handler: me.onSettings,
            scope: me
          },
          '-',
          {
            text:'退出',
            iconCls:'logout',
            handler: me.onLogout,
            scope: me
          }
        ]
      }
    });
  },

  onLogout: function () {
    Ext.Msg.confirm('退出', '确定要退出吗?');
  },

  onSettings: function () {
    var dlg = new Pagoda.Settings({
      desktop: this.desktop
    });
    dlg.show();
  }
});