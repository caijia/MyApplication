package com.example.administrator.myapplication;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String s = "/Date(1492414200000)/";

        Pattern p = Pattern.compile("/Date\\((\\d+)\\)/");
        Matcher matcher = p.matcher(s);
        if (matcher.matches()) {
            int i = matcher.groupCount();
            String group = matcher.group(1);
            System.out.println(i +"group="+group);
        }

        Object a = new A();
        A a1 = new A();
        B b = new B();

        System.out.println(A.class.isAssignableFrom(a.getClass()));
        System.out.println(B.class.isAssignableFrom(a.getClass()));

        int direction = 2 < 1 != true ? -1 : 1;
        System.out.println("direction="+direction);

    }

    public static class A{
        public  int a;
    }

    public static class B extends A{
        public  int b;
    }
}