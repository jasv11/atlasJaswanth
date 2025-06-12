abstract class Gadgets {
    abstract void turnOn();
    abstract void turnOff();
}

class TVRemote extends Gadgets {
    @Override
    void turnOn() {
        System.out.println("TV is turned ON.");
    }

    @Override
    void turnOff() {
        System.out.println("TV is turned OFF.");
    }
}

class ACRemote extends Gadgets {
    @Override
    void turnOn() {
        System.out.println("AC is turned ON.");
    }

    @Override
    void turnOff() {
        System.out.println("AC is turned OFF.");
    }
}

public class task42 {
    public static void main(String[] args) {
        Gadgets tvRemote = new TVRemote();
        Gadgets acRemote = new ACRemote();
        tvRemote.turnOn();   
        tvRemote.turnOff();  
        acRemote.turnOn();   
        acRemote.turnOff();  
    }
}