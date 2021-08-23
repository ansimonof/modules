package org.myorg.modules.util;

public class BuilderField<T> {

    private T field;
    private boolean isContain;

    public void with(T field) {
        this.field = field;
        this.isContain = true;
    }

    public T getField() {
        return field;
    }

    public boolean isContainField() {
        return isContain;
    }
}
