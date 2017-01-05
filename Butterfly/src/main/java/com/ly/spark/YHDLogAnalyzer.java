package com.ly.spark;

import com.ly.utils.TimeUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import java.util.Comparator;

/**
 * Created by yongliu on 12/9/16.
 */
public class YHDLogAnalyzer {

  private static Function2<Long, Long, Long> SUM_REDUCER = (a, b) -> a + b;

  public static void main(String[] args) {
    // Create a Spark Context.
    SparkConf conf = new SparkConf().setAppName("Log Analyzer");
    JavaSparkContext sc = new JavaSparkContext(conf);
    sc.cancelAllJobs();
    
    if(args.length == 0){
      args = new String[]{"/Users/yongliu/Downloads/tmp/logs/JD-Comment-2016-12-27-135444.log"};
    }

    // Load the text file into Spark.
    if (args.length == 0) {
      System.out.println("Must specify an access logs file.");
      System.exit(-1);
    }
    String logFile = args[0];
    JavaRDD<String> logLines = sc.textFile(logFile);

    JavaRDD<String> logLines2 = logLines.filter(new Function<String, Boolean>() {
      @Override
      public Boolean call(String v1) throws Exception {
        return YHDLog.parseFromLogLine(v1) != null;
      }
    });


    System.out.println(String.format("Total commented order %d in log file: %s", logLines2.count(), args[0]));

    JavaRDD<YHDLog> accessLogs = logLines2.map(YHDLog::parseFromLogLine).cache();


    JavaRDD<String> orders = accessLogs.map(YHDLog::getOrderNum).cache();

    JavaRDD<Long> spentTimes = accessLogs.map(YHDLog::getSpentTime);
    long totaltime = spentTimes.reduce(SUM_REDUCER);
    
    System.out.println(TimeUtils.toTime(totaltime));
    System.out.println(spentTimes.min(Comparator.naturalOrder()));
    System.out.println(spentTimes.max(Comparator.naturalOrder()));

    sc.stop();
    
  }
  
}
