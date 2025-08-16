// package com.example;
package Module03OOAD.designPatterns.StructuralDP.ProxyMethodDP.demo.src.main.java.com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Proxy Method Design Pattern - Structural design pattern");
        DatabaseExecute emp1 = new ProxyDataBase("ADMIN");
        emp1.executeQuery("Delete");
        emp1.executeQuery("Write");
        emp1.executeQuery("Read");

        DatabaseExecute emp2 = new ProxyDataBase("HR");
        emp2.executeQuery("Delete");
        emp2.executeQuery("Write");
        emp2.executeQuery("Read");

        DatabaseExecute emp3 = new ProxyDataBase("EMPLOYEE");
        emp3.executeQuery("Delete");
        emp3.executeQuery("Write");
        emp3.executeQuery("Read");
    }
}