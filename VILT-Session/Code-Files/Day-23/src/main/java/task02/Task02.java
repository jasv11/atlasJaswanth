package task02;

interface Pizza {
    void preparation();
    void baking();
    void cutting();
    void boxing();
}

class PepperoniPizza implements Pizza {
    @Override
    public void preparation() {
        System.out.println("Preparing Pepperoni Pizza");
    }

    @Override
    public void baking() {
        System.out.println("Baking Pepperoni Pizza");
    }

    @Override
    public void cutting() {
        System.out.println("Cutting Pepperoni Pizza");
    }

    @Override
    public void boxing() {
        System.out.println("Boxing Pepperoni Pizza");
    }
}

interface PizzaFactory {
    Pizza createPizza();
}

class PepperoniPizzaFactory implements PizzaFactory {
    @Override
    public Pizza createPizza() {
        return new PepperoniPizza();
    }
}

public class Task02 {
    public static void main(String[] args) {
        PizzaFactory pfobj = new PepperoniPizzaFactory();
        Pizza pobj = pfobj.createPizza();
        
        pobj.preparation();
        pobj.baking();
        pobj.cutting();
        pobj.boxing();
    }
}