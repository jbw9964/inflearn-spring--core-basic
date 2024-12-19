package sections.section03_04.service;

import sections.section03_04.domain.*;

public interface DiscountPolicy {

    long discount(User user, long price);
}
