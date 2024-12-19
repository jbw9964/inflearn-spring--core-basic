package sections.section03_04.repository;

import java.util.*;
import sections.section03_04.domain.*;
import sections.section03_04.exception.*;

public class ProductRepositoryMap implements
        ProductRepository {

    private static final Map<Long, Product> productMap = new HashMap<>();
    private static long ID_COUNT = 1;

    public ProductRepositoryMap() {
        productMap.clear();
        ID_COUNT = 1;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(
                productMap.get(id)
        );
    }

    @Override
    public Product save(Product product) {
        if (!product.isValidWithoutId()) {
            throw new InvalidProductException();
        }

        product.setId(ID_COUNT);
        productMap.put(ID_COUNT++, product);

        return product;
    }

    @Override
    public Product update(Product product) {

        if (!product.isValid()) {
            throw new InvalidProductException();
        }

        if (this.findById(product.getId()).isEmpty()) {
            throw new ProductNotFoundException();
        }

        Long id = product.getId();
        productMap.put(id, product);

        return product;
    }

    @Override
    public void deleteById(Long id) {
        if (!productMap.containsKey(id)) {
            throw new ProductNotFoundException();
        }

        productMap.remove(id);
    }

    @Override
    public List<Product> findAll() {
        return productMap.values().stream().toList();
    }
}
