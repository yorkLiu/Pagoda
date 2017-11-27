**Python Implementation**

# Requirement
```
pip install requests logging selenium pycrypto
```

# Required PhantomJS brwoser
About more [PhantomJS](http://phantomjs.org/)


# Set Cookie to webdriver 
[Click Here to get the implementation](https://stackoverflow.com/a/37105419)
```
    def save_cookies(driver, file_path):
        LINE = "document.cookie = '{name}={value}; path={path}; domain={domain}; expires={expires}';\n"
        with open(file_path, 'w') as file :
            for cookie in driver.get_cookies() :
                file.write(LINE.format(**cookie))
    
    def load_cookies(driver, file_path):
        with open(file_path, 'r') as file:
            driver.execute_script(file.read())
    
    
    from selenium import webdriver
    
    driver = webdriver.PhantomJS()
    
    # load the domain
    driver.get("https://stackoverflow.com/users/login")
    
    # save the cookies to a file
    save_cookies(driver, r"cookies.js")
    
    # delete all the cookies
    driver.delete_all_cookies()
    
    # load the cookies from the file
    load_cookies(driver, r"cookies.js")
```

# Electron
  [Download](https://github.com/electron/electron/releases)
  [中文教程](https://www.w3cschool.cn/electronmanual/p9al1qkx.html)