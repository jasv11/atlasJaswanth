interface ICalcArea {
    double calcArea();
    double calcPerimeter();
}

interface ICalcVolume {
    double calcVolume();
}

class Circle implements ICalcArea {
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
    public double calcPerimeter() {
        System.out.println("Calculating Circle Perimeter");
        return 2 * Math.PI * radius;
    }
}

class Sphere implements ICalcArea, ICalcVolume {
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
    public double calcPerimeter() {
        System.out.println("Calculating Sphere Circumference");
        return 2 * Math.PI * radius;
    }

    @Override
    public double calcVolume() {
        System.out.println("Calculating Sphere Volume");
        return (4.0/3.0) * Math.PI * radius * radius * radius;
    }
}

class task04 {
    public static void main(String[] args) {
        Circle circle = new Circle(5);
        Sphere sphere = new Sphere(5);

        circle.calcArea();
        circle.calcPerimeter();

        sphere.calcArea();
        sphere.calcPerimeter();
        sphere.calcVolume();
    }
}