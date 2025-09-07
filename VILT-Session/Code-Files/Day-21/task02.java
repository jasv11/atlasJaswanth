abstract class BirdsThatFly {
    abstract void fly();
}

abstract class BirdsThatDontFly {
    abstract void speciality();
}

class Eagle extends BirdsThatFly {
    @Override
    public void fly() {
        System.out.println("Eagles fly");
    }
}

class Ostrich extends BirdsThatDontFly {
    @Override
    public void speciality() {
        System.out.println("It lays big egg");
    }
}

class task02 {
    public static void main(String[] args) {
        BirdsThatFly eagle = new Eagle();
        BirdsThatDontFly ostrich = new Ostrich();

        eagle.fly();
        ostrich.speciality();
    }
}