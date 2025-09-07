package Module03OOAD.designPatterns.StructuralDP.FacadeMethodDP.demo.src.main.java.com.example;
// facade
public class MallFacade {
    private StoreStaffService storeStaffService;
    private BillingCounterService billingCounterService;

    public MallFacade() {
        this.storeStaffService = new StoreStaffService();
        this.billingCounterService = new BillingCounterService();
    }
    public void getItems(String items) {
        storeStaffService.getItems(items);
    }
    public void payBill(String accountId, String billId, double amount){
        billingCounterService.payBill(accountId,billId, amount );
    }
}
