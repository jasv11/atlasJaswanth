package com.example;

public class CashPaymentStrategy implements PaymentStrategy {
    public void process(double price) {
        System.out.println("Processing cash payment of $" + price + " - Cash received successfully!");
    }
}
