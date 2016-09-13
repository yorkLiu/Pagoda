import java.util.Arrays;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by yongliu on 9/1/16.
 */
public class Test {

  public static void main(String[] args) {
    List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
    costBeforeTax.stream().map((cost) -> Float.parseFloat(cost.toString()) + Float.parseFloat(cost.toString())*0.12).forEach(System.out::println);


    int a = costBeforeTax.stream().reduce((sum, cost) -> (sum+cost)).get();
    
    System.out.println("a=" + a);

    IntSummaryStatistics intSummaryStatistics = costBeforeTax.stream().mapToInt(x -> x).summaryStatistics();
    System.out.println("The max:" + intSummaryStatistics.getMax());
    System.out.println("The min: " + intSummaryStatistics.getMin());
    System.out.println("The avg: " + intSummaryStatistics.getAverage());
    System.out.println("The Sum: " + intSummaryStatistics.getSum());
    

    List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
    System.out.println("Languages which starts with J :");
//    languages.stream().filter((str) -> str.toString().startsWith("J")).forEach(System.out::println);
    filter(languages, (str) -> str.toString().startsWith("J"));
    System.out.println("Languages which ends with a :");
    filter(languages, (str) -> str.toString().endsWith("a"));
    System.out.println("Languages which contains a :");
    filter(languages, (str) -> str.toString().contains("a"));
    
  } 
  
  
  private static void filter(List<String> list, Predicate condition){
    list.stream().filter((item) -> (condition.test(item))).forEach(System.out::println);
  }
  
}
