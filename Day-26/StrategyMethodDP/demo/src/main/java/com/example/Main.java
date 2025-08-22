package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Strategy Method Design Pattern - Behavioral DP");
        
        System.out.println("\n1. Payment without Strategy Pattern:");
        Payments payment = new Payments();
        payment.setPaymentType(Payments.PaymentType.Cash);
        payment.process(100.0);
        
        payment.setPaymentType(Payments.PaymentType.Cards);
        payment.process(200.0);
        
        payment.setPaymentType(Payments.PaymentType.UPI);
        payment.process(300.0);
        
        System.out.println("\n2. Payment with Strategy Pattern:");
        
        PaymentUsingStrategy cashPayment = new PaymentUsingStrategy(new CashPaymentStrategy());
        cashPayment.process(100.0);
        
        PaymentUsingStrategy cardPayment = new PaymentUsingStrategy(new CardPaymentConcreteStrategy());
        cardPayment.process(200.0);
        
        PaymentUsingStrategy upiPayment = new PaymentUsingStrategy(new UPIPaymentStrategy());
        upiPayment.process(300.0);
        
        System.out.println("\n3. Runtime Strategy Switching:");
        PaymentUsingStrategy dynamicPayment = new PaymentUsingStrategy(new CashPaymentStrategy());
        System.out.print("Initial strategy - ");
        dynamicPayment.process(150.0);
        
        dynamicPayment = new PaymentUsingStrategy(new CardPaymentConcreteStrategy());
        System.out.print("Switched strategy - ");
        dynamicPayment.process(150.0);
    }
}
