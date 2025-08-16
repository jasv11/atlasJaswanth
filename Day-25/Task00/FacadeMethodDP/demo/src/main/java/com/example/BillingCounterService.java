package Module03OOAD.designPatterns.StructuralDP.FacadeMethodDP.demo.src.main.java.com.example;

public class BillingCounterService {
    public void payBill(String accountId, String billId, double amount) {
        System.out.println("paying for Mangoes "+ amount +"for billId "+ billId + "from account" +accountId);
    }
}
