public class task019 {
    public static void main(String[] args) {
        for (Element element : Element.values()) {
            System.out.println("Element: " + element);
            System.out.println("  Label: " + element.label);
            System.out.println("  Atomic Number: " + element.atomicNumber);
            System.out.println("  Atomic Weight: " + element.atomicWeight);
            System.out.println();
        }
        
        Element hydrogen = Element.valueOfLabel("Hydrogen");
        System.out.println("Found: " + hydrogen + " - " + hydrogen.label);
        
        System.out.println("Hydrogen: " + Element.H.label);
        System.out.println("Helium: " + Element.HE.label);
        System.out.println("Neon: " + Element.NE.label);
    }
}