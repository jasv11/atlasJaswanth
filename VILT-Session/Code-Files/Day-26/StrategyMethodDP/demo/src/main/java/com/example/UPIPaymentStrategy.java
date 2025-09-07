package com.example;

public class UPIPaymentStrategy implements PaymentStrategy {
    public void process(double price) {
        System.out.println("Processing UPI payment of $" + price + " - UPI transaction completed successfully!");
    }
}
