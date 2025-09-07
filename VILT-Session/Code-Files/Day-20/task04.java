class task04 {
    public static void main(String[] args) {
        Employee employee = new Employee("Jaswanth", "jaswanth@gmail.com", 50000.0);

        PdfReportGenerator reportGenerator = new PdfReportGenerator();
        EmailService emailService = new EmailService();

        reportGenerator.generatePdfReport(employee);

        emailService.sendEmail(employee);
    }
}

class Employee {
    private String name;
    private String email;
    private double salary;

    public Employee(String name, String email, double salary) {
        this.name = name;
        this.email = email;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getSalary() {
        return salary;
    }
}

class PdfReportGenerator {
    public void generatePdfReport(Employee employee) {
        System.out.println("Generating PDF report for employee: " + employee.getName());
    }
}

class EmailService {
    public void sendEmail(Employee employee) {
        System.out.println("Sending email to: " + employee.getEmail());
    }
}