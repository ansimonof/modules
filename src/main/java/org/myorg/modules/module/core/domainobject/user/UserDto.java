package org.myorg.modules.module.core.domainobject.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("login")
    private String login;

    @JsonProperty("password_hash")
    private String passwordHash;

    @JsonProperty("is_enabled")
    private Boolean isEnabled;

    @JsonProperty("is_admin")
    private Boolean isAdmin;

    @JsonProperty("session")
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

    @JsonProperty("is_enabled")
    public Boolean isEnabled() {
        return isEnabled;
    }

    @JsonProperty("is_admin")
    public Boolean isAdmin() {
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
