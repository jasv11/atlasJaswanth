class Shape {
    public double area() {
        return 0.0;
    }
}

class Square extends Shape {
    int height;

    public Square(int height) {
        this.height = height;
    }

    @Override
    public double area() {
        return height * height;
    }
}

class Circle extends Shape {
    int r;

    public Circle(int r) {
        this.r = r;
    }

    @Override
    public double area() {
        return Math.PI * r * r;
    }
}

class OpenClosedExample {
    public double compareArea(Shape shape1, Shape shape2) {
        return shape1.area() - shape2.area();
    }
}

class task05 {
    public static void main(String[] args) {
        Square square1 = new Square(5);
        Square square2 = new Square(4);
        Circle circle1 = new Circle(3);
        
        OpenClosedExample example = new OpenClosedExample();
        
        System.out.println("Square1 vs Square2: " + example.compareArea(square1, square2));
        System.out.println("Square1 vs Circle1: " + example.compareArea(square1, circle1));
    }
}