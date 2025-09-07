package Module03OOAD.designPatterns.StructuralDP.AdapterMethod.demo.src.main.java.com.example;

public class ChargerAdaptee implements IChargerAdaptee{
    ChargerAdaptee() {};

    public void charge() {
        System.out.println("charging my laptop");
    }
    public void removeCharge(){
        System.out.println("not charging my laptop");
    }
}
