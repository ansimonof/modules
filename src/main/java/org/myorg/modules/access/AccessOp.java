package org.myorg.modules.access;

import org.myorg.modules.util.BaseEnum;

public enum AccessOp implements BaseEnum<AccessOp> {

    // Степень двойки
    READ(1),
    WRITE(2);

    private final int id;
    AccessOp(int id) {
        this.id = id;
    }

    @Override
    public int intValue() {
        return id;
    }

}