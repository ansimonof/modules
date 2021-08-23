package org.myorg.modules.querypool.database;

import javax.persistence.*;

@MappedSuperclass
@SuppressWarnings("unchecked")
public class DomainObject<T> {

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public T save(PersistenceContext pc) {
        pc.em.persist(this);
        return (T) this;
    }

    public T update(PersistenceContext pc) {
        pc.em.merge(this);
        return (T) this;
    }

    public void remove(PersistenceContext pc) {
        pc.em.remove(this);
    }

    public static <T> TypedQuery<T> createNamedQuery(Class<T> clazz, String name, PersistenceContext pc) {
        return pc.em.createNamedQuery(name, clazz);
    }

    public static <T> T get(Class<T> clazz, long id, PersistenceContext pc) {
        return (T) pc.em.find(clazz, id);
    }
}
