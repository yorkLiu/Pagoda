const electron = require('electron');
const glob = require('glob');
const path = require('path');
const app = electron.app;
const BrowserWindow = electron.BrowserWindow;

// electron.crashReporter.start();


if (process.mas) app.setName('Order Helper');

// 保持一个对于 window 对象的全局引用，不然，当 JavaScript 被 GC，
// window 会被自动地关闭
var mainWindow = null;



// 当所有窗口被关闭了，退出。
app.on('window-all-closed', function() {
  // 在 OS X 上，通常用户在明确地按下 Cmd + Q 之前
  // 应用会保持活动状态
  //if (process.platform != 'darwin') {
    app.quit();
  //}
});

function initialize () {
    var shouldQuit = makeSingleInstance();
    if (shouldQuit) return app.quit();

    loadRequires();

    function createWindow () {
        var windowOptions = {
            width: 800,
            minWidth: 680,
            height: 600,
            title: app.getName()
        };

        mainWindow = new BrowserWindow(windowOptions);
        mainWindow.loadURL(path.join('file://', __dirname, '/index.html'));

           // 当 window 被关闭，这个事件会被发出
        mainWindow.on('closed', function() {
          // 取消引用 window 对象，如果你的应用支持多窗口的话，
          // 通常会把多个 window 对象存放在一个数组里面，
          // 但这次不是。
          mainWindow = null;
        });
    }

  app.on('ready', function () {
    createWindow()
  });

}



// Make this app a single instance app.
//
// The main window will be restored and focused instead of a second window
// opened when a person attempts to launch a second instance.
//
// Returns true if the current version of the app should quit instead of
// launching.
function makeSingleInstance () {
  if (process.mas) return false;

  return app.makeSingleInstance(function () {
    if (mainWindow) {
      if (mainWindow.isMinimized()) mainWindow.restore();
      mainWindow.focus()
    }
  })
}

// Require each JS file in the main-process dir
function loadRequires () {
  var files = glob.sync(path.join(__dirname, 'main-process/**/*.js'));
  files.forEach(function (file) {
    require(file)
  })
}

initialize();
