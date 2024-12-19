package sections.section03_04.repository;

import java.util.*;
import sections.section03_04.domain.*;

public interface ShippingSummaryRepository {

    Optional<ShippingSummary> findById(Long id);

    ShippingSummary save(ShippingSummary shippingSummary);

    ShippingSummary update(ShippingSummary shippingSummary);

    void deleteById(Long id);

    List<ShippingSummary> findAll();
}
