import java.util.ArrayList;
import java.util.List;

class Animal {
    void sound() {
        System.out.println("Sounds of different animals");
    }
}

class Cat extends Animal {
    @Override
    void sound() {
        System.out.println("Meow is the sound of cat");
    }
}

class task02 {
    public static void main(String[] args) {
        Animal obj = new Cat();
        obj.sound(); 

        List<Cat> catList = new ArrayList<>();

        List<? extends Animal> animalList = catList; 

        catList.add(new Cat()); 

        Animal animal = animalList.get(0); 
        animal.sound();
    }
}