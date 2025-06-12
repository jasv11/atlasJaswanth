class Superclass {
    int var;
    Superclass(int var) {
        this.var = var;
    }
    public void getVar() {
        System.out.println("var value in super class is " + var);
    } 
}

public class task36 extends Superclass {
    task36(int var) {
        super(var);
    }
    public static void main(String[] args) {
        Superclass sobj = new Superclass(100);
        sobj.getVar();

        task36 tobj = new task36(200);
        tobj.getVar();
    }
}