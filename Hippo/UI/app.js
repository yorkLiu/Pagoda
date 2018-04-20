/**
 * Created by yongliu on 11/27/17.
 */

const electron = require('electron');
// const path = require('path');
// const fs = require('fs');


function saveConfigs(){
    let fields_id = ['mark_receive_address_symbol','delay_seconds_for_next_by_goods_id','sms_username', 'sms_password', 'adsl_dial_on', 'adsl_username', 'adsl_password'];
    let values = getElementsByIds(fields_id);
    alert('---')
    console.log(values);

    const options = {
                  type: 'info',
                  title: '更新系统设置',
                  buttons: ['我知道了'],
                  message: '保存成功'
                };
                electron.dialog.showMessageBox(mainWindow, options, function () {})
    return false;

}

function getElementsByIds(fieldIds){
    var map = {};
    for (let id of fieldIds) {
        let field = document.getElementById(id);
        if(field){
            map[id] = field.value;
        }
    }

    return map;
}

String.prototype.format = function(keyValueMap){
    try {
        var v = this;
        if (keyValueMap) {
            for (var key in keyValueMap) {
                let reg = new RegExp('\\{' + key + '\\}', 'gi');
                v = v.replace(reg, keyValueMap[key]);
            }
        }
        return v;
    }catch (e){
        console.log(e);
    }
}

function generateFields(content){
    // content: {title: 'title', fields: [{field...}]
    // field {fieldType: 'text', maxLength: 5, fieldId: 'test1', label1:'test text', label2: '', value: 'defaultValue'}
    //
    var prefix = '<div class="row"><form class="form-horizontal"><fieldset><legend> <p>{title}</p> </legend>';
    var field_tpl = '<div class="form-group"><label class="col-md-4 control-label" for="{fieldId}">{label1}</label>' +
            '<div class="col-md-4">'+
            ' <input type="{fieldType}" maxlength="{maxLength}" name="{fieldId}" id="{fieldId}" value="{value}"  class="form-control input-md">'+
            '</div>'+
            '{label2}'+
        '</div>';
    var label2_tpl = '<label class="col-md-1 control-label">{label2}</label>';
    var buttons = '<div class="form-group"><label class="col-md-4 control-label" for="submit"></label><div class="col-md-4"><button id="submit" class="btn btn-primary" onclick="saveConfigs()" style="padding: 0">保存</button></div></div>'
    var suffix = buttons + '</form></fieldset></div>';


    var html = '';

    var title = content['title'];
    var fields = content['fields'];

    // html += prefix.replace("{title}", title);
    html += prefix.format({title:title});
    for (let field of fields) {
        if (field){
            let label2 = field['label2'] || '';
            field['label2'] =  label2? label2_tpl.format({'label2':label2 }): '';
            field['value'] = field['value'] || '';
            field['maxLength'] = field['maxLength'] || '200';

            html += field_tpl.format(field);
        }
    }
    html +=suffix;

    return html;
}

function loadSysSetting() {
    var formContent = {
        title: '系统设置',
        fields:[
            {
                fieldType: 'text',
                label1: '地址前加',
                label2: '作为暗号',
                fieldId: 'mark_receive_address_symbol',
                maxLength: 5
            },
            {
                fieldType: 'number',
                label1: '同一个商品间隔',
                label2: '秒',
                fieldId: 'delay_seconds_for_next_by_goods_id',
                value: 60,
                maxlength: 6
            },
            {
                fieldType: 'text',
                label1: '速码短信平台 登陆用户名',
                label2: '<a href="http://www.eobzz.com/newsUpPage/registered.html">点击注册</a>',
                fieldId: 'sms_username'
            },
            {
                fieldType:  'password',
                label1: '速码短信平台 登陆密码',
                fieldId: 'sms_password'
            },
            {
                fieldType: 'checkbox',
                label1: '使用宽带拨号',
                fieldId: 'adsl_dial_on',
                value: '0'
            },
            {
                fieldType: 'text',
                label1: '宽带账号',
                fieldId: 'adsl_username'
            },
            {
                fieldType: 'password',
                label1: '宽带密码',
                fieldId: 'adsl_password'
            }
        ]
    };

    let formHtml = generateFields(formContent);
    $('#tabForSetting').empty();
    $('#tabForSetting').append(formHtml);
}


$(function() {
   let $sysSetting = $('#tabForSysSetting');

   $sysSetting.click(function(){
       loadSysSetting();
   })
});