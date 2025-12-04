
import java.util.List;
import java.util.Scanner;
import java.util.UUID;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductManager productManager = new ProductManager();

        while (true) {
            System.out.println("===== PRODUCT MANAGER =====");
            System.out.println("1. Add Product");
            System.out.println("2. Remove product");
            System.out.println("3. Find product by name");
            System.out.println("4. Show all products");
            System.out.println("5. Filter price > 100k");
            System.out.println("6. Show product names");
            System.out.println("7. Sort by price (ascending)");
            System.out.println("8. Exit");
            System.out.println("============================");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // Add Product
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter product price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine(); 
                    System.out.print("Enter product quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    Product product = new Product(name, price, quantity);
                    productManager.addProduct(product);
                    System.out.println("Product added successfully.");
                    System.out.println("==============================");
                    break;
                case 2:
                    // Remove Product by ID
                    System.out.print("Enter product ID to remove: ");
                    String idStr = scanner.nextLine();
                    try {
                        UUID id = UUID.fromString(idStr);
                        boolean removed = productManager.removeProductById(id);
                        if (removed) {
                            System.out.println("Product removed successfully.");
                            System.out.println("==============================");
                        } else {
                            System.out.println("Product not found.");
                            System.out.println("==============================");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid UUID format.");
                        System.out.println("==============================");
                    }
                    break;
                case 3:
                    // Find Product by Name
                    System.out.print("Enter product name to find: ");
                    String searchName = scanner.nextLine();
                    List<Product> foundProduct = productManager.findProductByName(searchName);
                    if (foundProduct != null) {
                        System.out.println("Product found: " + foundProduct);
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;
                case 4:
                    // Show All Products
                    productManager.showAllProducts();
                    break;
                case 5:
                    // Filter Products by Price Greater Than
                    List<Product> filteredProducts = productManager.filterByPriceGreaterThan(100000);
                    filteredProducts.forEach(System.out::println);
                    break;
                case 6:
                    // Get Product Names
                    List<String> productNames = productManager.getProductNames();
                    productNames.forEach(System.out::println);
                    break;
                case 7:
                    // Sort Products by Price Ascending
                    List<Product> sortedProducts = productManager.sortByPriceAscending();
                    sortedProducts.forEach(System.out::println);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
