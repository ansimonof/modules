package org.myorg.modules.access.service;

import org.myorg.modules.access.PrivilegeAccessOpPair;
import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.querypool.database.PersistenceContext;

import java.util.List;


public interface AccessPrivilegeService {

    List<PrivilegeAccessOpPair> getPrivilegeAccessOpPairs(long userId, PersistenceContext pc) throws ModuleException;
}
