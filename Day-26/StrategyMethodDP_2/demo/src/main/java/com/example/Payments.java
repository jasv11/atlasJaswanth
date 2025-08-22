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
            System.out.println("cash payment ");
        }
        if(paymentType == PaymentType.Cards) {
            System.out.println("card payment ");
        }
        if(paymentType == PaymentType.UPI) {
            System.out.println("UPI payment ");
        }
        else {
            throw new IllegalArgumentException("sorry invalid payment");
        }
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}
