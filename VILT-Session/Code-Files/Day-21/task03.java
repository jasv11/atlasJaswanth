interface ICalcShapesArea {
    double calcArea();
    double calcVolume();   
}

class Circle implements ICalcShapesArea {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double calcArea() {
        System.out.println("Calculating Circle Area");
        return Math.PI * radius * radius;
    }

    @Override
    public double calcVolume() {
        System.out.println("Circle cannot have volume");
        return 0;
    }
}

class Sphere implements ICalcShapesArea {
    private double radius;

    public Sphere(double radius) {
        this.radius = radius;
    }

    @Override
    public double calcArea() {
        System.out.println("Calculating Sphere Surface Area");
        return 4 * Math.PI * radius * radius;
    }

    @Override
    public double calcVolume() {
        System.out.println("Calculating Sphere Volume");
        return (4.0/3.0) * Math.PI * radius * radius * radius;
    }
}

class task03 {
    public static void main(String[] args) {
        ICalcShapesArea circle = new Circle(5);
        ICalcShapesArea sphere = new Sphere(5);

        circle.calcArea();   
        circle.calcVolume(); 
        sphere.calcArea();   
        sphere.calcVolume();
    }
}