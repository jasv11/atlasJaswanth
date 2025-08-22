package com.example;
// paymets using - No strategy
public class Payments {
    private PaymentType paymentType;

    enum PaymentType {
        Cash,
        Cards,
        UPI
    }
    public void process(double price) {
        if(paymentType == PaymentType.Cash) {
            System.out.println("Processing cash payment of $" + price + " - Cash received!");
        }
        else if(paymentType == PaymentType.Cards) {
            System.out.println("Processing card payment of $" + price + " - Card charged!");
        }
        else if(paymentType == PaymentType.UPI) {
            System.out.println("Processing UPI payment of $" + price + " - UPI transaction completed!");
        }
        else {
            throw new IllegalArgumentException("Sorry, invalid payment type!");
        }
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}
