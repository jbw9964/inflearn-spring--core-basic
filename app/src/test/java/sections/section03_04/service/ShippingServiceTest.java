package sections.section03_04.service;

import static org.assertj.core.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import sections.section03_04.*;
import sections.section03_04.Run.*;
import sections.section03_04.domain.*;
import sections.section03_04.repository.*;

class ShippingServiceTest {

    UserRepository userRepo;
    ProductRepository productRepo;
    ShippingDetailRepository shippingDetailRepo;
    DiscountPolicy discountPolicy;

    ShippingSummaryRepository shippingSummaryRepo;

    ShippingService shippingService;

    User testUser;
    List<Product> testProducts;
    final int TEST_SIZE = 10;

    @BeforeEach
    void setUp() {
        Config config = Run.init();

        discountPolicy = config.get(DiscountPolicy.class);

        userRepo = config.get(UserRepository.class);
        productRepo = config.get(ProductRepository.class);

        shippingSummaryRepo = config.get(ShippingSummaryRepository.class);
        shippingDetailRepo = config.get(ShippingDetailRepository.class);

        shippingService = config.get(ShippingService.class);

        testUser = new User();
        testUser.setRole(UserRole.NORMAL);
        testProducts = IntStream.range(0, TEST_SIZE).boxed()
                .map(i -> new Product())
                .peek(p -> p.setPrice(100L))
                .toList();

        userRepo.save(testUser);
        testProducts = testProducts.stream()
                .map(product -> productRepo.save(product))
                .toList();
    }

    @Test
    void orderProducts() {

        this.checkOrderValid(testUser);

        User vipUser = new User();
        vipUser.setRole(UserRole.VIP);
        vipUser = userRepo.save(vipUser);

        this.checkOrderValid(vipUser);
    }

    @Test
    void findShippingSummaryById() {
        var test = shippingService.orderProducts(testUser, testProducts);

        var result = shippingService.findShippingSummaryById(test.getId()).orElseThrow();

        assertThat(result).satisfies(
                r -> assertThat(r.getId()).isEqualTo(test.getId()),
                r -> assertThat(r.getUserId()).isEqualTo(testUser.getId()),
                r -> assertThat(r.getTotalPrice()).isEqualTo(test.getTotalPrice())
        );
    }

    @Test
    void deleteOrder() {
        Long id = shippingService.orderProducts(testUser, testProducts).getId();

        shippingService.deleteOrder(id);

        assertThat(shippingSummaryRepo.findById(id)).isEmpty();
        assertThat(shippingDetailRepo.findAllByShippingSummaryId(id)).isEmpty();
    }

    private void checkOrderValid(User user) {
        long expectedPrice = discountPolicy.discount(
                user,
                testProducts.stream().mapToLong(Product::getPrice).sum()
        );

        var result = shippingService.orderProducts(user, testProducts);

        assertThat(result).isNotNull().satisfies(
                r -> assertThat(r.getId()).isNotNull(),
                r -> assertThat(r.getUserId()).isEqualTo(user.getId()),
                r -> assertThat(r.getTotalPrice()).isEqualTo(expectedPrice)
        );
    }
}