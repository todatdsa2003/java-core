package dao;

import java.util.List;
import models.Product;

public interface ProductDAO {
    
    List<Product> findAll();
    Product findById(long id);
    List<Product> searchByName(String keyword);
    boolean insert(Product product);
    boolean update(Product product);
    boolean delete(long id);
}
