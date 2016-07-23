package com.ly.test.service;

import com.ly.model.Account;
import com.ly.model.type.CategoryType;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ly.service.AccountService;

import java.util.Date;


/**
 * Created by yongliu on 7/21/16.
 *
 * @author <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version 07/21/2016 16:57
 */
public class AccountServiceTest extends BaseManagerTestCase {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired
  private AccountService accountService;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * testSaveAccount.
   */
  @Test
  public void testSaveAccount() {
    Assert.notNull(accountService);

    Account account = new Account();
    account.setUsername("1234234234");
    account.setPassword("abcd123");
    account.setAccountLevel(1);
    account.setCategoryType(CategoryType.YHD);
    account.setLocked(false);
    account.setDisabled(false);
    account.setCreateDate(new Date());

    accountService.save(account);

  }

}
