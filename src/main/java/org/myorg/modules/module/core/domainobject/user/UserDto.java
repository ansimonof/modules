package org.myorg.modules.module.core.domainobject.user;

public class UserDto {

    private Long id;
    private String login;
    private String passwordHash;
    private Boolean isEnabled;
    private Boolean isAdmin;

    private String session;

    public UserDto() {
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public String getSession() {
        return session;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
