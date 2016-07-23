package com.ly.test.service;

import com.ly.model.User;
import com.ly.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yongliu on 7/23/16.
 */
@Transactional
public class UserServiceTest extends BaseManagerTestCase {
  
  @Autowired
  private UserService userService;
  
  @Test
  public void testCreateUser(){
    User user = new User();
    
    user.setUsername("admin2");
    user.setPassword("tomcat");
    user.setEmail("test01@pagoda.com");
    user.setFirstName("Tester");
    user.setLastName("Pagoda");
    user.setTelephone("12213123");
    user.setTelephone2("234234234");
    user.setPasswordHint("don't forget me");
    userService.saveObject(user);

    System.out.println("user.id:" + user.getId());
    
    
  }
  
}
