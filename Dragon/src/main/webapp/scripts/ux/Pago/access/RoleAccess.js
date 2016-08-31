Ext.define('Pago.access.RoleAccess', {
  extend: 'Ext.util.Observable',

  alternateClassName: 'Pago.ux.RoleAccess',
  alias: 'plugin.roleaccess',

  requires: [
    'Ext.Function',
    'Ext.util.Observable'
  ],

  init: function(control) {
    Ext.applyIf(this, {
      featureName: '',
      byPass: false,
      allowDD: false,
      allowDbClick: false,
      disableAll: false,
      readOnly: true,
      mode: 'disable'
    });

    function handleField(field){
      if(field.isXType('field') || field.isXType('textarea')){
        if(this.readOnly && (field.isXType('textfield') && (!field.isXType('combo')))||(field.isXType('textarea'))){
          var cfg = {
            readOnly: disabled || field.disabled,
            disabled: field.disabled,
            enable: Ext.Function.createInterceptor(control.enable, function(value) {
              return (!disabled);
            }),
            setDisabled: Ext.Function.createInterceptor(field.setDisabled, function(value) {
              return (!disabled);
            })
          };

          Ext.apply(field, cfg);
        }else{
          var cfg = {
            disabled: disabled || field.disabled,
            enable: Ext.Function.createInterceptor(control.enable, function(value) {
              return (!disabled);
            }),
            setDisabled: Ext.Function.createInterceptor(field.setDisabled, function(value) {
              return (!disabled);
            })
          };

          Ext.apply(field, cfg);
        }
      }
    }

    function checkAccess(featureName){
      if(featureName == '#allowAll#'){
        return true;
      }

      var parts = featureName.split('|');
      for(var i = 0; i < parts.length; i ++){
        if(accessRes[parts[i]] == true){
          // any feature allowed, return allow
          return true;
        }
      }

      return false;
    }

    if(!this.byPass){
      // default is disable
      var disabled = true;
      var hidden = true;

      if(accessRes){
        if(this.featureName){
          disabled = (!(checkAccess(this.featureName))) || this.disableAll;
          hidden = ((this.mode == 'hide') && disabled);
        }
      }

      if((control.isXType('button')) || (control.isXType('buttongroup'))
        || (control.isXType(Ext.button.Button)) || (control.isXType( Ext.container.ButtonGroup))){
        // handle button
        var cfg = {
          disabled: disabled || control.disabled,
          hidden: hidden || control.hidden,
          enable: Ext.Function.createInterceptor(control.enable, function(value) {
            return (!disabled);
          }),
          setDisabled: Ext.Function.createInterceptor(control.setDisabled, function(value) {
            return (!disabled);
          })
        };

        Ext.apply(control, cfg);
      }
      else if((control.isXType('menuitem')) || (control.isXType(Ext.menu.Item))){
        // handle menu
        var cfg = {
          disabled: disabled || control.disabled,
//          hidden: hidden || control.hidden,
          enable: Ext.Function.createInterceptor(control.enable, function(value) {
            return (!disabled);
          }),
          setDisabled: Ext.Function.createInterceptor(control.setDisabled, function(value) {
            return (!disabled);
          })
        };

        Ext.apply(control, cfg);
      }
      else if((control.isXType('grid')) || (control.isXType(Ext.grid.Panel))){
        // handle grid
        // disable drag and drop
        if(!this.allowDD){
          // check the DrogDrop plugin
          var plugins = control.plugins,
            ln = plugins.length;
          for (var i; i < ln; i++) {
            if (plugins[i].ptype === 'gridviewdragdrop') {
              plugins[i].enableDrop = (!disabled) && plugins[i].enableDrop;
              plugins[i].enableDrag = (!disabled) && plugins[i].enableDrag;
            }
          }
        }

        if(!this.allowDbClick){
          // disable row dbClick
          Ext.util.Observable.capture(control, function(e){
            if((e == 'itemdblclick') || (e == 'itemclick')){
              return (!disabled);
            }

            return true;
          });

          var plugins = control.plugins,
            ln = plugins.length;
          for (var i; i < ln; i++) {
            if ((plugins[i].ptype === 'editing') || (plugins[i].ptype === 'cellediting') || (plugins[i].ptype === 'rowediting')){
              plugins[i].beforeEdit = function(){return false;};
            }
          }
        }

        // deal with row action
        var columns = control.columns,
          ln = columns.length;
        for (var i; i < ln; i++) {
          if (columns[i].isXType('actioncolumn')) {
            columns[i].setVisible(disabled);
          }
        }
      }
      else if(control.ownerCt && (control.ownerCt.isXType('tabpanel') || control.ownerCt.isXType(Ext.tab.Panel))){
        // handle tab panel, in tab panel
        // hide this panel
        var cfg = {
          disabled: disabled || control.disabled,
          hidden: disabled || control.hidden,
          enable: Ext.Function.createInterceptor(control.enable, function(value) {
            return (!disabled);
          }),
          setDisabled: Ext.Function.createInterceptor(control.setDisabled, function(value) {
            return (!disabled);
          })
        };

        Ext.apply(control, cfg);
      }
      else if(control.isXType('treepanel') || control.isXType(Ext.tree.Panel) || (control.root && control.loader)){
        // handle tree
        if(!this.allowDD){
          // disable drag and drop
          var cfg = {
            draggable: (!disabled) && control.draggable,
            enableDrag: (!disabled) && control.enableDrag,
            enableDrop: (!disabled) && control.enableDrop,
            enableDD: (!disabled) && control.enableDD
          };

          Ext.apply(control, cfg);
        }

        if(!this.allowDbClick){
          // disable row dbClick
          Ext.util.Observable.capture(control, function(e){
            if(e == 'dblclick'){
              return (!disabled);
            }

            return true;
          });
        }
      }
      else{
        if(control.cascade){
          control.cascade(function(child){
            handleField(child);
          });
        }
        else{
          handleField(control);
        }
      }
    }
  }
});