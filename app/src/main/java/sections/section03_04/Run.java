package sections.section03_04;

import java.util.*;
import sections.section03_04.repository.*;
import sections.section03_04.service.*;

public class Run {

    public static Config init() {
        Config config = new Config();

        config.set(UserRepository.class, new UserRepositoryMap());
        config.set(ProductRepository.class, new ProductRepositoryMap());
        config.set(ShippingSummaryRepository.class, new ShippingSummaryRepositoryMap(
                config.get(UserRepository.class)
        ));
        config.set(ShippingDetailRepository.class, new ShippingDetailRepositoryMap(
                config.get(ProductRepository.class),
                config.get(ShippingSummaryRepository.class)
        ));

        config.set(DiscountPolicy.class, new TemporalDiscountPolicy());
        config.set(UserService.class, new UserService(
                config.get(UserRepository.class)
        ));
        config.set(ShippingService.class, new ShippingService(
                config.get(ShippingSummaryRepository.class),
                config.get(ShippingDetailRepository.class),
                config.get(DiscountPolicy.class)
        ));

        return config;
    }

    @SuppressWarnings("unchecked")
    public static class Config {

        Map<Class<?>, Object> config = new HashMap<>();

        public <T> T get(Class<T> clazz) {
            return (T) config.get(clazz);
        }

        public void set(Class<?> clazz, Object value) {

            config.put(clazz, value);
        }
    }
}
