package sections.section03_04.repository;

import java.util.*;
import java.util.function.*;
import sections.section03_04.domain.*;
import sections.section03_04.exception.*;

public class ShippingDetailRepositoryMap implements
        ShippingDetailRepository {

    private static final Map<Long, ShippingDetail> shippingDetailMap = new HashMap<>();
    private static long ID_COUNT = 1;

    private final ProductRepository productRepo;
    private final ShippingSummaryRepository shippingSummaryRepo;

    public ShippingDetailRepositoryMap(
            ProductRepository productRepo,
            ShippingSummaryRepository shippingSummaryRepo
    ) {
        shippingDetailMap.clear();
        ID_COUNT = 1;
        this.productRepo = productRepo;
        this.shippingSummaryRepo = shippingSummaryRepo;
    }

    @Override
    public Optional<ShippingDetail> findById(Long id) {
        return Optional.ofNullable(
                shippingDetailMap.get(id)
        );
    }

    @Override
    public ShippingDetail save(ShippingDetail shippingDetail) {
        if (!shippingDetail.isValidWithoutId()) {
            throw new InvalidShippingDetailException();
        }

        this.checkEntityExist(
                shippingDetail.getProductId(), new ProductNotFoundException(),
                productRepo::findById
        );
        this.checkEntityExist(
                shippingDetail.getShippingSummaryId(), new ShippingSummaryNotFoundException(),
                shippingSummaryRepo::findById
        );

        shippingDetail.setId(ID_COUNT);
        shippingDetailMap.put(ID_COUNT++, shippingDetail);

        return shippingDetail;
    }

    @Override
    public ShippingDetail update(ShippingDetail shippingDetail) {
        if (!shippingDetail.isValid()) {
            throw new InvalidShippingDetailException();
        }

        Long id = shippingDetail.getId();
        this.checkEntityExist(
                id, new ShippingDetailNotFoundException(),
                this::findById
        );

        this.checkEntityExist(
                shippingDetail.getProductId(), new ProductNotFoundException(),
                productRepo::findById
        );
        this.checkEntityExist(
                shippingDetail.getShippingSummaryId(), new ShippingSummaryNotFoundException(),
                shippingSummaryRepo::findById
        );

        shippingDetailMap.put(id, shippingDetail);

        return shippingDetail;
    }

    @Override
    public void deleteById(Long id) {
        this.checkEntityExist(
                id, new ShippingDetailNotFoundException(),
                this::findById
        );

        shippingDetailMap.remove(id);
    }

    @Override
    public void deleteAllByShippingSummaryId(Long shippingSummaryId) {

        shippingDetailMap.entrySet()
                .removeIf(
                        entry -> entry.getValue().getShippingSummaryId().equals(shippingSummaryId));

    }

    @Override
    public List<ShippingDetail> findAllByShippingSummaryId(Long shippingSummaryId) {
        return shippingDetailMap.values()
                .stream()
                .filter(s -> s.getShippingSummaryId().equals(shippingSummaryId))
                .toList();
    }

    @Override
    public List<ShippingDetail> findAll() {
        return shippingDetailMap.values().stream().toList();
    }

    private void checkEntityExist(Long id, RuntimeException e,
            Function<Long, Optional<?>> findById) {

        if (findById.apply(id).isEmpty()) {
            throw e;
        }
    }
}
