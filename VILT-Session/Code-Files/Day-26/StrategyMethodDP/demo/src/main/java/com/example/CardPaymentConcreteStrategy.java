package com.example;

public class CardPaymentConcreteStrategy implements PaymentStrategy {
    public void process(double price) {
        System.out.println("Processing card payment of $" + price + " - Card charged successfully!");
    }
}
