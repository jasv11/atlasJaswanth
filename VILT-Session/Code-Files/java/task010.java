public class task010 {
    public static void main(String[] args) {
        int num1 = 30;
        int num2 = 20;
        int num3 = 10;

        if (num1 > num2 && num1 > num3) {
            System.out.println("num1 is the greater");
        }
        else if (num2 > num1 && num2 > num3) {
            System.out.println("num2 is the greater");
        }
        else if (num3 > num1 && num3 > num2) {
            System.out.println("num3 is the greater");
        }
        else {
            System.out.println("Some numbers are equal");
        }
    }
}