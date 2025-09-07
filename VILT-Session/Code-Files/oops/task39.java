class Employee {
    private int pwd;
    protected int Salary;
    public int empid;
    
    Employee() {
        pwd = 0;
        Salary = 0;
        empid = 0;
    }
}

class Hr extends Employee {
    Hr() {
        //super.pwd = 1254;    // Cannot access private member
        super.Salary = 50000;  // Can access protected member
        super.empid = 10001;   // Can access public member
    }
}

public class task39 {
    public static void main(String[] args) {
        Employee emp = new Employee();
        //emp.pwd = 1254;      // Cannot access private member
        emp.Salary = 50000;    // Can access protected member
        emp.empid = 10001;     // Can access public member
        
        System.out.println("Salary: " + emp.Salary);
        System.out.println("Employee ID: " + emp.empid);
    }
}