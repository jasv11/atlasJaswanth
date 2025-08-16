package Module03OOAD.designPatterns.StructuralDP.BridgeMethodDP.demo.src.main.java.com.example;
// Abstraction
abstract public class Shape {
    protected ExcalidrawAPI excalidrawAPI;

    protected Shape(ExcalidrawAPI excalidrawAPI) {
        this.excalidrawAPI = excalidrawAPI;
    }
    abstract void draw();
}
