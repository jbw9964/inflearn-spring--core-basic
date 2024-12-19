package sections.section03_04.domain;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class User {

    private Long id;
    private UserRole role;

    public boolean isValid() {
        return this.id != null && this.id > 0 && this.role != null;
    }

    public boolean isValidWithoutId() {
        return this.role != null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
