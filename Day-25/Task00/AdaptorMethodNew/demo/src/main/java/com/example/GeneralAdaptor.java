package Module03OOAD.designPatterns.StructuralDP.AdaptorMethodNew.demo.src.main.java.com.example;

// import javax.swing.DefaultButtonModel;

public class GeneralAdaptor implements Charger{
    DellCharger dellCharger;
    
    GeneralAdaptor() {
        dellCharger = new DellCharger();
    }

    @Override
    public void charge() {
        dellCharger.charge();
    }
}
