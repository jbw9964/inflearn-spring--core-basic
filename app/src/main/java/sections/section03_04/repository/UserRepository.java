package sections.section03_04.repository;

import java.util.*;
import sections.section03_04.domain.*;

public interface UserRepository {

    Optional<User> findById(Long id);

    User save(User user);

    User update(User user);

    void deleteById(Long id);

    List<User> findAll();
}
