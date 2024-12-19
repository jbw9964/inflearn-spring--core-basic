package sections.section03_04.service;

import java.util.*;
import sections.section03_04.domain.*;
import sections.section03_04.repository.*;

public class ShippingService {

    private final ShippingSummaryRepository shippingSummaryRepo;
    private final ShippingDetailRepository shippingDetailRepo;
    private final DiscountPolicy discountPolicy;

    public ShippingService(
            ShippingSummaryRepository shippingSummaryRepo,
            ShippingDetailRepository shippingDetailRepo,
            DiscountPolicy discountPolicy
    ) {
        this.shippingSummaryRepo = shippingSummaryRepo;
        this.shippingDetailRepo = shippingDetailRepo;
        this.discountPolicy = discountPolicy;
    }

    public ShippingSummary orderProducts(User user, List<Product> products) {
        ShippingSummary shippingSummary = new ShippingSummary();
        shippingSummary.setUserId(user.getId());

        long totalPrice = products.stream().mapToLong(Product::getPrice).sum();
        long discounted = discountPolicy.discount(user, totalPrice);

        shippingSummary.setTotalPrice(discounted);

        shippingSummary = shippingSummaryRepo.save(shippingSummary);

        products.stream()
                .map(p -> new ShippingDetail(p.getId(), user.getId()))
                .forEach(shippingDetailRepo::save);

        return shippingSummary;
    }

    public Optional<ShippingSummary> findShippingSummaryById(Long id) {
        return shippingSummaryRepo.findById(id);
    }

    public void deleteOrder(Long shippingSummaryId) {
        shippingDetailRepo.deleteAllByShippingSummaryId(shippingSummaryId);
        shippingSummaryRepo.deleteById(shippingSummaryId);
    }
}
