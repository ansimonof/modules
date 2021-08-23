package org.myorg.modules.querypool;

import org.myorg.modules.querypool.database.PersistenceContext;
import org.myorg.modules.exception.ModuleException;

public interface Query<T> {

    T execute(PersistenceContext pc) throws ModuleException;
}
