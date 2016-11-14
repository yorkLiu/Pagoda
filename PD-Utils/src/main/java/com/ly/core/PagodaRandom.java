package com.ly.core;

import java.util.Random;
import java.util.Stack;

import org.springframework.util.Assert;


/**
 * Created by yongliu on 10/27/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/27/2016 13:39
 */
public class PagodaRandom {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Stack<Integer> randomNumbers = new Stack<>();

  private int randomSize;

  private int seed;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new PagodaRandom object.
   *
   * @param  seed        int
   * @param  randomSize  int
   */
  public PagodaRandom(int seed, int randomSize) {
    Assert.notNull(seed);
    Assert.notNull(randomSize);
    this.seed       = seed;
    this.randomSize = randomSize;
    generateRandom();
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * nextInt.
   *
   * @return  int
   */
  public int nextInt() {
    return randomNumbers.pop();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void generateRandom() {
    int count = 0;
    int flag  = 0;

    while (count < randomSize) {
      Random random = new Random(System.currentTimeMillis());
      int    num    = Math.abs(random.nextInt()) % seed;

      for (int i = 0; i < count; i++) {
        if (randomNumbers.contains(num)) {
          flag = 1;

          break;
        } else {
          flag = 0;
        }
      }

      if (flag == 0) {
        randomNumbers.push(num);
        count++;
      }
    }
  }
} // end class PagodaRandom
