package sections.section03_04.service;

import sections.section03_04.domain.*;

public class TemporalDiscountPolicy implements DiscountPolicy {

    @Override
    public long discount(User user, long price) {
        return user.getRole().equals(UserRole.VIP) ?
                Math.max(0, price - 1000) : price;
    }
}
