package sections.section03_04.repository;

import java.util.*;
import java.util.function.*;
import sections.section03_04.domain.*;
import sections.section03_04.exception.*;

public class ShippingSummaryRepositoryMap implements
        ShippingSummaryRepository {

    private static final Map<Long, ShippingSummary> shippingSummaryMap = new HashMap<>();
    private static long ID_COUNT = 1;

    private final UserRepository userRepo;

    public ShippingSummaryRepositoryMap(UserRepository userRepo) {
        shippingSummaryMap.clear();
        ID_COUNT = 1;
        this.userRepo = userRepo;
    }

    @Override
    public Optional<ShippingSummary> findById(Long id) {
        return Optional.ofNullable(
                shippingSummaryMap.get(id)
        );
    }

    @Override
    public ShippingSummary save(ShippingSummary shippingSummary) {

        if (!shippingSummary.isValidWithoutId()) {
            throw new InvalidShippingSummaryException();
        }

        this.checkEntityExist(
                shippingSummary.getUserId(), new UserNotFoundException(),
                userRepo::findById
        );

        shippingSummary.setId(ID_COUNT);
        shippingSummaryMap.put(ID_COUNT++, shippingSummary);

        return shippingSummary;
    }

    @Override
    public ShippingSummary update(ShippingSummary shippingSummary) {

        if (!shippingSummary.isValid()) {
            throw new InvalidShippingSummaryException();
        }

        Long id = shippingSummary.getId();
        this.checkEntityExist(
                id, new ShippingSummaryNotFoundException(),
                this::findById
        );

        this.checkEntityExist(
                shippingSummary.getUserId(), new UserNotFoundException(),
                userRepo::findById
        );

        shippingSummaryMap.put(id, shippingSummary);

        return shippingSummary;
    }

    @Override
    public void deleteById(Long id) {
        this.checkEntityExist(
                id, new ShippingSummaryNotFoundException(),
                this::findById
        );

        shippingSummaryMap.remove(id);
    }

    @Override
    public List<ShippingSummary> findAll() {
        return shippingSummaryMap.values().stream().toList();
    }

    private void checkEntityExist(Long id, RuntimeException e,
            Function<Long, Optional<?>> findById) {

        if (findById.apply(id).isEmpty()) {
            throw e;
        }
    }
}
