import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.lang.Integer;

class MyConsumer implements Consumer<Integer> {
    public void accept(Integer t) {
        System.out.println("Consumer impl Value::" + t);
    }
}

public class task18 {
    public static void main(String[] args) {
        List<Integer> myList = new ArrayList<Integer>();
        for(int i=0; i<10; i++) myList.add(i);
        
        // Using Iterator
        Iterator<Integer> it = myList.iterator();
        while(it.hasNext()) {
            Integer i = it.next();
            System.out.println("Iterator Value::" + i);
        }
        
        // Using forEach with anonymous class
        myList.forEach(new Consumer<Integer>() {
            public void accept(Integer t) {
                System.out.println("forEach anonymous class Value::" + t);
            }
        });
        
        // Using forEach with Consumer implementation
        MyConsumer action = new MyConsumer();
        myList.forEach(action);
    }
}