package org.myorg.modules.querypool.database;

import javax.persistence.EntityManager;

public class PersistenceContext {

    EntityManager em;

    public PersistenceContext(EntityManager em) {
        this.em = em;
    }
}
