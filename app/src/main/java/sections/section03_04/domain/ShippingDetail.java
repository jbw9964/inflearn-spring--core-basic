package sections.section03_04.domain;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class ShippingDetail {

    private Long id;
    private Long productId;
    private Long shippingSummaryId;

    public boolean isValid() {
        return id != null && id > 0 && productId != null && productId > 0
                && shippingSummaryId != null && shippingSummaryId > 0;
    }

    public boolean isValidWithoutId() {
        return productId != null && productId > 0 && shippingSummaryId != null
                && shippingSummaryId > 0;
    }

    public ShippingDetail(Long productId, Long shippingSummaryId) {
        this.productId = productId;
        this.shippingSummaryId = shippingSummaryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getShippingSummaryId() {
        return shippingSummaryId;
    }

    public void setShippingSummaryId(Long shippingSummaryId) {
        this.shippingSummaryId = shippingSummaryId;
    }
}
