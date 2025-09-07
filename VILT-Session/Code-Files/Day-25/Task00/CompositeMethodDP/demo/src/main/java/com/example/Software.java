package Module03OOAD.designPatterns.StructuralDP.CompositeMethodDP.demo.src.main.java.com.example;
//leaf component]
public class Software implements Company{
    private int id;
    private String Name;

    public void displayName() {
        System.out.println(getClass().getSimpleName());
    }
    //Constructor 
    //getters
    //setters
}
