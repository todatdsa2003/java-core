
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    //
    public static void displayMenu() {
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
    }

    // Add Product Menu
    public static void addProductMenu(Scanner scanner, ProductManager productManager) {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price;
        while (true) {
            try {
                price = Double.parseDouble(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.print("Invalid. Enter price again: ");
            }
        }
        scanner.nextLine();
        System.out.print("Enter product quantity: ");
        int quantity;
        while (true) {
            try {
                quantity = Integer.parseInt(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.print("Invalid. Enter quantity again: ");
            }
        }
        Product product = new Product(name, price, quantity);
        productManager.addProduct(product);
        System.out.println("Product added successfully.");
    }

    //
    public static void removeProductMenu(Scanner scanner, ProductManager productManager) {
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
    }

    //
    public static void findProductByNameMenu(Scanner scanner, ProductManager productManager) {
        System.out.print("Enter product name to find: ");
        String searchName = scanner.nextLine();
        List<Product> foundProduct = productManager.findProductByName(searchName);
        if (foundProduct.isEmpty()) {
            System.out.println("Product not found.");
        } else {
            System.out.println("Product found: " + foundProduct);

        }
    }

    //
    public static void filterPriceGreaterThanMenu(Scanner scanner, ProductManager productManager) {
        System.out.print("Enter the minimum price: ");
        double minPrice = scanner.nextDouble();
        scanner.nextLine();
        List<Product> filteredProducts = productManager.filterByPriceGreaterThan(minPrice);
        filteredProducts.forEach(System.out::println);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductManager productManager = new ProductManager();

        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    // Add Product
                    addProductMenu(scanner, productManager);
                    break;
                case 2:
                    // Remove Product by ID
                    removeProductMenu(scanner, productManager);
                    break;
                case 3:
                    // Find Product by Name
                    findProductByNameMenu(scanner, productManager);
                    break;
                case 4:
                    // Show All Products
                    productManager.showAllProducts();
                    break;
                case 5:
                    // Filter Products by Price Greater Than
                    filterPriceGreaterThanMenu(scanner, productManager);
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
