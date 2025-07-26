public class task03 {
    public static void main(String[] args) {
        Book book = new Book("Java Book", "Me", 200);
        BookFormatter formatter = new BookFormatter();
        PriceCalculator calculator = new PriceCalculator();

        System.out.println(formatter.formatTitle(book));
        System.out.println(formatter.formatBookInfo(book));
        System.out.println("Discounted Price: $" + 
            calculator.calculateDiscountedPrice(book, 0.2)); 
    }
}

class Book {
    private String title;
    private String author;
    private double price;

    public Book(String title, String author, double price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }
}

class BookFormatter {
    public String formatTitle(Book book) {
        return "Title: " + book.getTitle().toUpperCase();
    }

    public String formatBookInfo(Book book) {
        return "Title: " + book.getTitle() + 
               "\nAuthor: " + book.getAuthor() + 
               "\nPrice: $" + book.getPrice();
    }
}

class PriceCalculator {
    public double calculateDiscountedPrice(Book book, double discountPercentage) {
        return book.getPrice() * (1 - discountPercentage);
    }
}