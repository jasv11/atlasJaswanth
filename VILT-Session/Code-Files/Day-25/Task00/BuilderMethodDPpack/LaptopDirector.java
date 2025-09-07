package Module03OOAD.BuilderMethodDesignPattern.BuilderMethodDPpack;

//Director (optional)
public class LaptopDirector {
    private LaptopBuilder laptopBuilder;
    LaptopDirector(LaptopBuilder laptopBuilder) {
        this.laptopBuilder = laptopBuilder;
    }
    public Laptop constructLaptop() {
        return laptopBuilder
                    .buildMemory
                    .buildStorage
                    .build();
    }
}
