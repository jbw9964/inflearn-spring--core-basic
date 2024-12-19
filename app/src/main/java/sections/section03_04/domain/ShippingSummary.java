package sections.section03_04.domain;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class ShippingSummary {

    private Long id;
    private Long userId;
    private long totalPrice;

    public boolean isValid() {
        return id != null && id > 0 && userId != null && userId > 0 && totalPrice >= 0;
    }

    public boolean isValidWithoutId() {
        return userId != null && userId > 0 && totalPrice >= 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
