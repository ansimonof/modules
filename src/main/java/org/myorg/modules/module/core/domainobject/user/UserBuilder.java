package org.myorg.modules.module.core.domainobject.user;

import org.myorg.modules.util.BuilderField;

public class UserBuilder {

    private BuilderField<String> login = new BuilderField<>();
    private BuilderField<String> passwordHash = new BuilderField<>();
    private BuilderField<Boolean> isEnabled = new BuilderField<>();
    private BuilderField<Boolean> isAdmin = new BuilderField<>();

    public UserBuilder withLogin(String login) {
        this.login.with(login);
        return this;
    }

    public UserBuilder withPasswordHash(String passwordHash) {
        this.passwordHash.with(passwordHash);
        return this;
    }

    public UserBuilder withIsEnabled(Boolean isEnabled) {
        this.isEnabled.with(isEnabled);
        return this;
    }

    public UserBuilder withIsAdmin(Boolean isAdmin) {
        this.isAdmin.with(isAdmin);
        return this;
    }

    ///////////////
    public boolean isContainLogin() {
        return this.login.isContainField();
    }

    public boolean isContainPasswordHash() {
        return this.passwordHash.isContainField();
    }

    public boolean isContainEnabled() {
        return this.isEnabled.isContainField();
    }

    public boolean isContainAdmin() {
        return this.isAdmin.isContainField();
    }

    /////////////
    public String getLogin() {
        return login.getField();
    }

    public String getPasswordHash() {
        return passwordHash.getField();
    }

    public Boolean getIsEnabled() {
        return isEnabled.getField();
    }

    public Boolean getIsAdmin() {
        return isAdmin.getField();
    }

}
