// package com.example;
package Module03OOAD.designPatterns.StructuralDP.BridgeMethodDP.demo.src.main.java.com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bridge Method Design Pattern - Structural DP!");
        ExcalidrawAPI obj1 = new DrawingFrame();
        ExcalidrawAPI obj2 = new DrawingPicture();
        
        Shape square = new Square(5);
        
        square.draw();
    }
}