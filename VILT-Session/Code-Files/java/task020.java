public class task020 {
    public static void main(String[] args) {
        
        char[] Name = {'J', 'A', 'S', 'W', 'A', 'N', 'T', 'H'};
        
        System.out.println("My name array: ");
        System.out.println(Name);
        
        int n = Name.length;
        System.out.println("There are " + n + " letters in my name");
        
        System.out.println("Each letter in my name:");
        for (int i = 0; i < n; i++) {
            System.out.print(Name[i] + " ");
        }
        
        System.out.println();
        
    }
}
