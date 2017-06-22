package com.ly.web.utils;

import java.lang.reflect.Array;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.ly.web.command.CommentsInfo;


/**
 * Created by yongliu on 6/20/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  06/20/2017 13:13
 */
public class PagodaOrderSortUtils {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * sort.
   *
   * @param   list  List
   *
   * @return  List
   */
  public static List<CommentsInfo> sort(List<CommentsInfo> list) {
    List<CommentsInfo> randomRobinList = new LinkedList<>();

    List<List<CommentsInfo>> commentsInfoList = list.stream().collect(Collectors.groupingBy(CommentsInfo::getSku))
      .values().stream().sorted((f1, f2) -> Integer.compare(f2.size(), f1.size())).collect(Collectors.toList());

    for (List<CommentsInfo> commentsInfos : commentsInfoList) {
      System.out.println(commentsInfos.iterator().next().getSku() + ":" + commentsInfos.size());
    }

    int sizeOfGroupBySku    = commentsInfoList.size();
    int total               = list.size();
    int max                 = commentsInfoList.stream().max((c1, c2) -> Integer.compare(c1.size(), c2.size())).get()
      .size();
    int countOfExclusiveMax = total - max;


    System.out.println("max/count: " + max + ", " + countOfExclusiveMax);

    if (max == countOfExclusiveMax) {
      // p: p1, p2, p3, p4, p5 (5)
      // d: d1, d2             (2)
      // s: s1, s2             (2)
      // w: w2                 (1)
      // then after process the list is:
      // p1, d1
      // p2, s1
      // p3, w2
      // p4, d2
      // p5, s2
      //
      // random robin
      int pos = 0;

      for (int i = 0; i < max; i++) {
        /// the max size list
        /// every time should get the first one
        findAndPut(randomRobinList, commentsInfoList, 0, 0);

        pos++;

        if (pos >= sizeOfGroupBySku) {
          pos = 1;
        }

        /// from the @pos to get the list
        findAndPut(randomRobinList, commentsInfoList, pos, 0);

      } // end for

    } else if (countOfExclusiveMax > max) {
      int               secondlyMax  = 0;
      Set<CommentsInfo> notPuttedSet = new HashSet<>();

      if (commentsInfoList.size() > 1) {
        secondlyMax = commentsInfoList.get(1).size();
      }

      if ((max - secondlyMax) <= 1) {
        for (int i = 0; i < max; i++) {
          commentsInfoList.stream().forEach(commentsInfos -> { findAndPut2(randomRobinList, commentsInfos, 0); });
        }

      } else {
        int steps       = countOfExclusiveMax / max;
        int reduceCount = countOfExclusiveMax % max;


        int pos = 1;

        for (int i = 1; i <= max; i++, pos++) {
          if (pos >= sizeOfGroupBySku) {
            pos = 1;
          }

          CommentsInfo commentInfo = getItemRecycle(commentsInfoList, pos, sizeOfGroupBySku);

          if (commentInfo != null) {
            randomRobinList.add(commentInfo);
          }

          // iterater the max list
          for (int j = 0; j < steps; j++) {
            findAndPut(randomRobinList, commentsInfoList, 0, 0);
          }
        }

        if (reduceCount > 0) {
          for (int i = 0; i < reduceCount; i++) {
            commentsInfoList.stream().forEach(commentsInfos -> {
              if (!commentsInfos.isEmpty() && (commentsInfos.size() > 0)) {
                CommentsInfo lastInfo    = randomRobinList.get(randomRobinList.size() - 1);
                CommentsInfo currentInfo = commentsInfos.get(0);

                if (currentInfo.getSku().equalsIgnoreCase(lastInfo.getSku())) {
                  notPuttedSet.add(currentInfo);
                  commentsInfos.remove(currentInfo);
                } else {
                  findAndPut2(randomRobinList, commentsInfos, 0);
                }
              }


            });
          } // end for
        }   // end if

      } // end if-else

      // https://stackoverflow.com/questions/30012295/java-8-lambda-filter-by-lists
      if (!notPuttedSet.isEmpty() && (notPuttedSet.size() > 0)) {
        for (CommentsInfo commentsInfo : notPuttedSet) {
          System.out.println(commentsInfo.getOrderId() + "\t" + commentsInfo.getSku());
        }
// Predicate<CommentsInfo> hasSameNameAsOneUser =
// c -> notPuttedSet.stream().anyMatch(u -> u.getSku().equals(c.getSku()));
// List<CommentsInfo> duplicateList = randomRobinList.stream().filter(hasSameNameAsOneUser).collect(Collectors.toList());

        IntPredicate indicatePredicate = i ->
            notPuttedSet.stream().anyMatch(info -> info.getSku().equals(randomRobinList.get(i).getSku()));
        int[]        indicates         = IntStream.range(0, randomRobinList.size()).filter(indicatePredicate).toArray();
        System.out.println(Arrays.toString(indicates));


        if (indicates.length == 1) {
          int[] tmpIndicates;
          int   indicate = indicates[0];

          if (indicate == 0) {
            tmpIndicates    = new int[2];
            tmpIndicates[0] = 1;
            tmpIndicates[1] = randomRobinList.size() - 1;
          } else {
            tmpIndicates    = new int[3];
            tmpIndicates[0] = 0;
            tmpIndicates[1] = indicate;
            tmpIndicates[2] = randomRobinList.size() - 1;
          }

          indicates = tmpIndicates;
        }

        if (indicates.length > 0) {
          processRandomInsert(randomRobinList, notPuttedSet, indicates);
        }
      } // end if


    } else {
      // countOfExclusiveMax < max

      int tolerance = max - countOfExclusiveMax;

      if (tolerance <= 1) {
        for (int i = 0; i < max; i++) {
          commentsInfoList.stream().forEach(commentsInfos -> { findAndPut2(randomRobinList, commentsInfos, 0); });
        }
      } else {
        int steps       = max / countOfExclusiveMax;
        int reduceCount = max % countOfExclusiveMax;
        int pos         = 1;

        for (int i = 1; i <= countOfExclusiveMax; i++, pos++) {
          if (pos >= sizeOfGroupBySku) {
            pos = 1;
          }

          CommentsInfo commentInfo = getItemRecycle(commentsInfoList, pos, sizeOfGroupBySku);

          if (commentInfo != null) {
            randomRobinList.add(commentInfo);
          }

          // iterater the max list
          for (int j = 0; j < steps; j++) {
            findAndPut(randomRobinList, commentsInfoList, 0, 0);
          }
        }

        if (reduceCount > 0) {
          for (int i = 0; i < reduceCount; i++) {
            findAndPut(randomRobinList, commentsInfoList, 0, 0);
          }
        }
      } // end if-else

    } // end if-else

    return (!randomRobinList.isEmpty()) ? randomRobinList : list;
  } // end method sort

  //~ ------------------------------------------------------------------------------------------------------------------

  private static void findAndPut(List<CommentsInfo> randomRobinList, List<List<CommentsInfo>> operationList, int pos1,
    int pos2) {
    List<CommentsInfo> maxList = operationList.get(pos1);
    CommentsInfo       info    = getItemFromList(maxList, pos2);

    if (info != null) {
      randomRobinList.add(info);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private static void findAndPut2(List<CommentsInfo> randomRobinList, List<CommentsInfo> list, int pos) {
    CommentsInfo info = getItemFromList(list, pos);

    if (info != null) {
      randomRobinList.add(info);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private static CommentsInfo getItemFromList(List<CommentsInfo> list, int pos) {
    if ((list != null) && !list.isEmpty() && (list.size() > 0)) {
      CommentsInfo info = list.get(pos);
      list.remove(info);

      return info;
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private static CommentsInfo getItemRecycle(List<List<CommentsInfo>> list, int pos, int totalCount) {
    if ((list != null) && !list.isEmpty() && (list.size() > 0)) {
      if (pos >= totalCount) {
        pos = 1;
      }

      List<CommentsInfo> exclusiveMaxList = list.get(pos);

      if ((exclusiveMaxList != null) && (exclusiveMaxList.size() > 0)) {
        CommentsInfo commentInfo = exclusiveMaxList.get(0);
        exclusiveMaxList.remove(commentInfo);

        return commentInfo;
      } else {
        pos++;

        return getItemRecycle(list, pos, totalCount);
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Insert the same sku record to correct position.
   *
   * @param  randomRobinList  $param.type$
   * @param  set              $param.type$
   * @param  indicates        $param.type$
   */
  private static void processRandomInsert(List<CommentsInfo> randomRobinList, Set<CommentsInfo> set, int[] indicates) {
    int startPos = 0;
    int endPos   = 0;
    int setSize  = set.size();

    for (int i = 0; i < indicates.length; i++) {
      int indicate = indicates[i];

      if (i == 0) {
        startPos = indicate;

        continue;
      } else {
        endPos = indicate;
      }

      Integer stp = null;

      if (endPos > startPos) {
        int posSize = endPos - startPos - 1;

        if (posSize > 0) {
          if ((posSize > set.size()) || (posSize > (set.size() * 2))) {
            stp = posSize / (setSize + 1);
          }
        }
      }

      if (stp != null) {
        if ((set == null) || set.isEmpty()) {
          break;
        }

        if (stp != null) {
          int insertPos = startPos;

          for (int j = 0; j < setSize; j++) {
            insertPos = insertPos + stp;

            if (insertPos >= endPos) {
              break;
            }

            if (set.iterator().hasNext()) {
              CommentsInfo commentsInfo = set.iterator().next();
              randomRobinList.add(insertPos, commentsInfo);
              set.remove(commentsInfo);
            }
          }
        }
      }
    } // end for
  } // end method processRandomInsert

} // end class PagodaOrderSortUtils
