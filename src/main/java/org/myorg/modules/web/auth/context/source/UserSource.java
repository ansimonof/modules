package org.myorg.modules.web.auth.context.source;

public class UserSource extends Source {

    private long id;
    private boolean isEnabled;
    private boolean isAdmin;

    public long getId() {
        return id;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
