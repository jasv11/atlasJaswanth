// package com.example;
package Module03OOAD.designPatterns.StructuralDP.CompositeMethodDP.demo.src.main.java.com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Composite Method DP - Structural DP");
        Company softwareCompany = new Software(1, "software");

        CompanyHead companyHead = new CompanyHead(3, "abcCompany");
        companyHead.addDepartment(softwareCompany);
        companyHead.displayName();
    }
}