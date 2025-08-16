package com.example;
//client code for Adapter mdethod DP
// connect adapter with the client

import Module03OOAD.designPatterns.StructuralDP.AdapterMethod.demo.src.main.java.com.example.IChargerAdaptee;

public class Main {
    public static void main(String[] args) {
        System.out.println("Adaptor Method Design pattern");
        DellLaptop dellobj = new DellLaptop(new powerSocketAdapter());
        dellobj.charge();
        dellobj.removeCharge();
    }
}