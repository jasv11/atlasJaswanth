abstract class Employee {
   private String name;
   private String address;
   private int number;

   public Employee(String name, String address, int number) {
      System.out.println("Constructing an Employee");
      this.name = name;
      this.address = address;
      this.number = number;
   }
   
   public double computePay() {
     System.out.println("Inside Employee computePay");
     return 0.0;
   }
   
   public void mailCheck() {
      System.out.println("Mailing a check to " + this.name + " " + this.address);
   }

   public String toString() {
      return name + " " + address + " " + number;
   }

   public String getName() {
      return name;
   }
 
   public String getAddress() {
      return address;
   }
   
   public void setAddress(String newAddress) {
      address = newAddress;
   }
 
   public int getNumber() {
      return number;
   }
}

class SalaryEmployee extends Employee {
   public SalaryEmployee(String name, String address, int number) {
      super(name, address, number);
   }
}

public class task41 {
   public static void main(String [] args) {
      SalaryEmployee e = new SalaryEmployee("George W.", "Houston, TX", 43);
      System.out.println("\nCall mailCheck using Employee reference--");
      e.mailCheck();
   }
}