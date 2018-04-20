/**
 * Created by yongliu on 11/27/17.
 */


const electron = require('electron');
const path = require('path');
const BrowserWindow = electron.BrowserWindow;
const Menu = electron.Menu;
const app = electron.app;

let template = [
    {
      label: '系统',
      submenu: [
          {
              label: '设置',
              click: function (item, focusedWindow) {
                  if (focusedWindow) {
                      focusedWindow.webContents.executeJavaScript("document.getElementById('tabForSysSetting').click();");
                    // focusedWindow.document.getElementById('tabForSysSetting').click()
                    // focusedWindow.loadURL(path.join('file://', __dirname, '../setting.html'));
                  }
              }
          }
      ]
    },
    {
      label: '窗口',
      submenu: [
          {
            label: '最小化',
            role: 'minimize'
          },
          {
            label: '关闭',
            role: 'close'
          },
          {
              label: 'Debug',
             click: function (item, focusedWindow) {
              if (focusedWindow) {
                focusedWindow.toggleDevTools()
              }
            }
          }
      ]
    },
    {
      label: '帮助',
      submenu: [
          {
            label: '如何下单',
            role: 'orderHelp',
            click: function (item, focusedWindow) {
              if (focusedWindow) {
                const options = {
                  type: 'info',
                  title: '应用程序菜单演示',
                  buttons: ['好的'],
                  message: '此演示用于 "菜单" 部分, 展示如何在应用程序菜单中创建可点击的菜单项.'
                };
                electron.dialog.showMessageBox(focusedWindow, options, function () {})
              }
            }
          },
          {
            label: '如何收菜',
            role: 'receiveHelp'
          }
      ]
    }
];

function addUpdateMenuItems (items, position) {
  if (process.mas) return

  const updateItems = [];

  // const version = electron.app.getVersion();
  // let updateItems = [{
  //   label: 'Version ${version}',
  //   enabled: false
  // }, {
  //   label: '正在检查更新',
  //   enabled: false,
  //   key: 'checkingForUpdate'
  // }, {
  //   label: '检查更新',
  //   visible: false,
  //   key: 'checkForUpdate',
  //   click: function () {
  //     require('electron').autoUpdater.checkForUpdates()
  //   }
  // }, {
  //   label: '重启并安装更新',
  //   enabled: true,
  //   visible: false,
  //   key: 'restartToUpdate',
  //   click: function () {
  //     require('electron').autoUpdater.quitAndInstall()
  //   }
  // }];

  items.splice.apply(items, [position, 0].concat(updateItems))
}

if (process.platform === 'darwin') {
  const name = electron.app.getName();
  template.unshift({
    label: name,
    submenu: [{
      label: '关于 ' + name,
      role: 'about'
    }, {
      type: 'separator'
    }, {
      label: '服务',
      role: 'services',
      submenu: []
    }, {
      type: 'separator'
    }, {
      label: '隐藏 '+name,
      accelerator: 'Command+H',
      role: 'hide'
    }, {
      label: '隐藏其它',
      accelerator: 'Command+Alt+H',
      role: 'hideothers'
    }, {
      label: '显示全部',
      role: 'unhide'
    }, {
      type: 'separator'
    }, {
      label: '退出',
      accelerator: 'Command+Q',
      click: function () {
        app.quit()
      }
    }]
  });

  addUpdateMenuItems(template[0].submenu, 1)
}

if (process.platform === 'win32') {
  const helpMenu = template[template.length - 1].submenu;
  addUpdateMenuItems(helpMenu, 0)
}

app.on('ready', function () {
    const menu = Menu.buildFromTemplate(template);
    Menu.setApplicationMenu(menu);
});
