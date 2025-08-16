package Module03OOAD.designPatterns.StructuralDP.AdaptorMethodNew.demo.src.main.java.com.example;

public class DellLaptop implements Laptop {
   Charger GeneralAdaptor;
//    public DellLaptop(Charger generalAdaptor) {
   public DellLaptop(GeneralAdaptor generalAdaptor) {
    this.GeneralAdaptor = generalAdaptor;
   };
   @Override
   public void plugin() {
        GeneralAdaptor.charge();
   }
   @Override
   public void unPlug() {
        GeneralAdaptor = null;
   }
   
}


