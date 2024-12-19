package sections.section03_04.repository;

import java.util.*;
import sections.section03_04.domain.*;

public interface ShippingDetailRepository {

    Optional<ShippingDetail> findById(Long id);

    ShippingDetail save(ShippingDetail shippingDetail);

    ShippingDetail update(ShippingDetail shippingDetail);

    void deleteById(Long id);

    void deleteAllByShippingSummaryId(Long shippingSummaryId);

    List<ShippingDetail> findAllByShippingSummaryId(Long shippingSummaryId);

    List<ShippingDetail> findAll();
}
