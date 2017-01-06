# Pagoda


## How to support Safari
- You need add extension to safari (http://www.tuicool.com/articles/NZVZNbF)
  SafariDriver.safariextz download:  http://en.osdn.jp/projects/sfnet_gnuhub/downloads/SafariDriver.safariextz/
- selenium version list http://selenium-release.storage.googleapis.com/index.html
- chinese doc: http://www.cnblogs.com/meiling-ji/p/5283476.html
- [geckodriver download](https://github.com/mozilla/geckodriver/releases)
- For more geckodriver visit: https://github.com/mozilla/geckodriver
- [Download Firefox install file old and newest version](https://ftp.mozilla.org/pub/firefox/releases/)


## Selenium set proxy for web driver
### [Selenium Proxy for IE and Firefox](http://www.seleniumhq.org/docs/04_webdriver_advanced.jsp)

#### For IE
```
String PROXY = "localhost:8080";

org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
proxy.setHttpProxy(PROXY)
     .setFtpProxy(PROXY)
     .setSslProxy(PROXY);
DesiredCapabilities cap = new DesiredCapabilities();
cap.setCapability(CapabilityType.PROXY, proxy);

WebDriver driver = new InternetExplorerDriver(cap);
```

#### For FireFox
```
String PROXY = "localhost:8080";

org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
proxy.setHttpProxy(PROXY)
     .setFtpProxy(PROXY)
     .setSslProxy(PROXY);
DesiredCapabilities cap = new DesiredCapabilities();
cap.setCapability(CapabilityType.PROXY, proxy);
WebDriver driver = new FirefoxDriver(cap);
```
