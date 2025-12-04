
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductManager {
    private List<Product> products;

    public ProductManager() {
        this.products = new ArrayList<>();
    }

    // add product
    public void addProduct(Product p) {
        if (p == null) {
            throw new IllegalArgumentException("San pham khong hop le");
        }
        products.add(p);
    }

    // remove product by id
    public boolean removeProductById(UUID id) {
        return products.removeIf(product -> product.getId().equals(id));
    }

    // find product by name
    public List<Product> findProductByName(String name) {
        if (name == null || name.isEmpty())
            return List.of();
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                result.add(product);
            }
        }
        return result;
    }

    // get all products
    public void showAllProducts() {
        for (Product product : products) {
            System.out.println(product.toString());
        }
    }

    // getPrice
    public List<Product> filterByPriceGreaterThan(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Gia san pham khong duoc am");
        }
        return products.stream()
                .filter(p -> p.getPrice() > price)
                .toList();
    }

    // GetProductsName
    public List<String> getProductNames() {
        return products.stream()
                .map(Product::getName)
                .toList();
    }

    //
    public List<Product> sortByPriceAscending() {
        return products.stream()
                .sorted((a, b) -> Double.compare(a.getPrice(), b.getPrice()))
                .toList();

    }

}
