enum Color {
    RED, BLUE, GREEN, YELLOW    
}

enum Weekdays {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY    
}

public class task016 {
    public static void main(String[] args) {
        
        for (Color color : Color.values()) {
            System.out.println(color);
        }
        
        for (Weekdays day : Weekdays.values()) {
            System.out.println(day);
        }
        
    }
}