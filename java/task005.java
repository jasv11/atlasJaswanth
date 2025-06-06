public class task005 {
   int add(int num1, int num2) {
        return num1 + num2;
    }

    int subtract(int num1, int num2) {
        return num1 - num2;
    }

    int multiply(int num1, int num2) {
        return num1 * num2;
    }
    
    int divide(int num1, int num2) {

        return num1 / num2;
    }

    public static void main(String[] args) {

        int num1 = 10;
        int num2 = 20;

        int adding = (num1 + num2);
        int subtract = (num1 - num2);
        int multiply = (num1 * num2);
        int divide = (num1 / num2);

        System.out.println(adding);
        System.out.println(subtract);
        System.out.println(multiply);
        System.out.println(divide);

    }
}
