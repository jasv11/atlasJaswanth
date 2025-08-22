class OuterClass {
    int x = 50;
    class InnerClass {
        public int innerMethod() {
            return x;
        }
    }
}

public class task14 {
    public static void main(String[] args) {
        OuterClass myOuter = new OuterClass();
        OuterClass.InnerClass myInner = myOuter.new InnerClass();
        System.out.println(myInner.innerMethod());
    }
}