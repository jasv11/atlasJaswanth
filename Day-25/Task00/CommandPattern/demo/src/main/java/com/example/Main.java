// package com.example;
package Module03OOAD.designPatterns.BehavioralDP.CommandPattern.demo.src.main.java.com.example;
// invoker 
public class Main {
    public static void main(String[] args) {
        System.out.println("Command Pattern - Behavioural DP");
        Task task = new Task();
        Mom remote = new Mom();

        Command onCommand = new DoTask(task);
        Command offCommand = new DontDoTask(task);

        remote.setCommand(onCommand); //ac 
        remote.executeCommand();   //button press

        remote.setCommand(offCommand); //ac
        remote.executeCommand(); //again clicking the button..

    }
}