abstract class Bird {
    abstract void fly();
}

class Eagle extends Bird {
    @Override
    public void fly() {
        System.out.println("Eagles fly");
    }
}

class Ostrich extends Bird {
    @Override
    public void fly() { 
        System.out.println("Can't fly high but It lays big egg");
    }
}

class task01 {
    public static void main(String[] args) {
        Bird eagle = new Eagle();
        Bird ostrich = new Ostrich();

        eagle.fly();    
        ostrich.fly();  
    }
}