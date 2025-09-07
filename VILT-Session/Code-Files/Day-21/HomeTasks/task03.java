import java.util.ArrayList;
import java.util.List;

class Animal {
    @Override
    public String toString() {
        return "This is an Animal";
    }
}

class Cat extends Animal {
    @Override
    public String toString() {
        return "This is a Cat";
    }
}

class task03 {
    static void printList(List<?> list) {
        for(Object element: list) {
            System.out.println(element);
        }
    }

    public static void main(String[] args) {
        List<Cat> catList = new ArrayList<>();
        catList.add(new Cat());
        printList(catList);

        List<Animal> animalList = new ArrayList<>();
        animalList.add(new Animal());
        printList(animalList);

        List<String> stringList = new ArrayList<>();
        stringList.add("Hello");
        stringList.add("Hi");
        printList(stringList);
    }
}