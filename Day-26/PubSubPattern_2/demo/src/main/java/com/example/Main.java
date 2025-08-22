package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Pub sub Pattern ");

        Messaging messaging = new Messaging();
// subscribers
        User user1 = new User("Prasunamba");
        User user2 = new User("Sheerisha DC");
        User user3 = new User("Sheerisha Perapagu");
        User user4 = new User("Hari Gopal");
        User user5 = new User(" Arun Kumar");
        User user6 = new User(" Sarath");

        // subscribe these users to the Messaging App
        messaging.addSub(user1);
        messaging.addSub(user2);
        messaging.addSub(user3);
        messaging.addSub(user4);
        messaging.addSub(user5);
        messaging.addSub(user6);

        // send the message to the messaging App
        messaging.sendMsg("Good Afternoon every one "); // send msg to all the users 

        // unsubscribe a user..
        messaging.removeSub(user1);

        // send the message to the messaging App
        messaging.sendMsg("Good Luck to you all for your Milestone "); // all the users except user 1

    }
}