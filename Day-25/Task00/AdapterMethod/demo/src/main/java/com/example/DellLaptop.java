package Module03OOAD.designPatterns.StructuralDP.AdapterMethod.demo.src.main.java.com.example;
//Adapter class - dellLaptop 
public class DellLaptop implements ILaptopTarget {
    IChargerAdaptee chargerObj ;
    DellLaptop() {
        chargerObj = new IChargerAdaptee();            
    }
    @Override
    public void charge() {
        chargerObj.charge();
    }
    @Override
    public void removeCharge() {
        chargerObj.removeCharge();
    }

}
