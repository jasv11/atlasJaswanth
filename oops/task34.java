class Clock {
    int hours;
    int minutes;
    
    Clock() {
        hours = 0;
        minutes = 0;
    }
}

class Calculation {
    int z;
    
    public void addition(int x, int y) {
        z = x + y;
        System.out.println("The sum of the given numbers:"+z);
    }
}

public class task34 {
    public static void main(String args[]) {
        Clock c = new Clock();
        System.out.println("Multiple inheritance is not possible in Java");
        System.out.println("class MyCalculation extends Calculation, Clock - Not Allowed");
    }
}