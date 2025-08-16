package Module03OOAD.BuilderMethodDesignPattern.BuilderMethodDPpack;

// product Component
public class Laptop {
    private int memory;
    private int storage;
    // graphic card,processor...   
    Laptop() {
        System.out.println("laptop constructor");
    }
    public int getMemory() {
        return memory;
    }
    public int getStorage() {
        return storage; 
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }
    public void setStorage(int storage) {
        this.storage =  storage;
    }
}
