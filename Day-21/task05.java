class Engine {
    public void start() {
        System.out.println("Engine starting");
    }
}

class Driver {
    private String name;

    public Driver(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Wheel {
    public Wheel() {
        System.out.println("Wheel created");
    }
}

class Car {
    private final Wheel[] wheels;
    
    private Driver driver;

    public Car(Driver driver) {
        this.wheels = new Wheel[4];
        for (int i = 0; i < 4; i++) {
            this.wheels[i] = new Wheel();
        }
        
        this.driver = driver;
    }

    public void drive() {
        Engine engine = new Engine(); 
        engine.start();
        System.out.println("Car is driving with driver: " + driver.getName());
    }
}

class task05 {
    public static void main(String[] args) {
        Driver driver = new Driver("Jaswanth");

        Car car = new Car(driver);

        car.drive();
    }
}