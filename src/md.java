import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: Automate
 * @description:
 * @author: xin
 * @create: 2020-05-05 20:20
 **/
public class md {


        public static void main(String args[])
        {

            // Creating an empty set
            Set<String>
                    set = new HashSet<String>();

            // Use add() method to
            // add elements in the set
            set.add("0");
            set.add("1");
            set.add("3");
            set.add("4");
            set.add("7");

            // prints the set
            System.out.println("Set 1: "
                    + set);

            // Creating another empty set
            Set<String>
                    set2 = new HashSet<String>();

            // Use add() method to
            // add elements in the set
            set2.add("0");
            set2.add("1");
            set2.add("3");
            set2.add("4");
           // set2.add("20");
            fun(set2);
            // prints the set
            System.out.println("Set 2: "
                    + set2);

            // Check if the set
            // contains same elements
            System.out.println("\nDoes set 1 contains set 2?: "
                    + set.containsAll(set2));
        }


        public static void fun(Set<String> set){
            ArrayList<String> list = new ArrayList<String>();
            list.add("hello");
            for (String s : list){
                set.add(s);
            }
        }


}
