package Module03OOAD.designPatterns.StructuralDP.AdapterMethod.demo.src.main.java.com.example;

public class powerSocketAdapter implements IChargerAdaptee {
    ChargerAdaptee cAdpteObj;

    powerSocketAdapter() {
        cAdpteObj = new ChargerAdaptee();          
    }
    @Override
    public void charge() {
        cAdpteObj.charge();
    }
    public void removeCharge() {
        cAdpteObj.removeCharge();
    }
}
