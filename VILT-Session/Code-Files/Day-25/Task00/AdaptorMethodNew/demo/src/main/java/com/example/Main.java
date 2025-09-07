// package com.example;
package Module03OOAD.designPatterns.StructuralDP.AdaptorMethodNew.demo.src.main.java.com.example;

// import Module03OOAD.designPatterns.StructuralDP.AdapterMethod.demo.src.main.java.com.example.DellLaptop;
// ******i have commented the Above import in line 4 and its working guys plz check
public class Main {
    public static void main(String[] args) {
        System.out.println("RE - Adaptor Method");
        // GeneralAdaptor gaobj = new GeneralAdaptor();
        // DellLaptop delllaptop = new DellLaptop(gaobj); //(new GeneralAdaptor());
        DellLaptop delllaptop = new DellLaptop(new GeneralAdaptor());
        delllaptop.plugin();
        delllaptop.unPlug();
    }
}