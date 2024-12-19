package sections.section03_04.domain;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class Product {

    private Long id;
    private Long price;

    public boolean isValid() {
        return id != null && price != null && id > 0 && price > 0;
    }

    public boolean isValidWithoutId() {
        return price != null && price > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
