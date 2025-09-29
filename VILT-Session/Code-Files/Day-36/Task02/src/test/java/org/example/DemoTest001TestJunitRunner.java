package org.example;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class DemoTest001TestJunitRunner {
   public static void main(String[] args) {
       System.out.println("running a test runner code");
       Result res = JUnitCore.runClasses(DemoTest001TestJunit.class);

       for (Failure fail: res.getFailures()) {
           System.out.println(fail.toString());
       }
       System.out.println(res.wasSuccessful());
   }
}