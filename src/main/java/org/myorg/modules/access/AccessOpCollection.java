package org.myorg.modules.access;

import java.util.Objects;

public class AccessOpCollection {

    private int value;

    public AccessOpCollection(int value) {
        this.value = value;
    }

    public AccessOpCollection() {
        this(0);
    }

    public AccessOpCollection(AccessOp... ops) {
        setOps(ops);
    }

    public boolean contains(AccessOp op) {
        return (value & op.intValue()) != 0;
    }

    public boolean contains(AccessOp... ops) {
        for (AccessOp op : ops) {
            if (!contains(op)) {
                return false;
            }
        }
        return true;
    }

    public void setOps(AccessOp[] ops) {
        value = 0;
        addOps(ops);
    }

    public void addOps(AccessOp[] ops) {
        for (AccessOp op : ops) {
            value |= op.intValue();
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessOpCollection that = (AccessOpCollection) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}