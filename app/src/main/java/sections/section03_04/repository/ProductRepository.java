package sections.section03_04.repository;

import java.util.*;
import sections.section03_04.domain.*;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    Product save(Product product);

    Product update(Product product);

    void deleteById(Long id);

    List<Product> findAll();
}
