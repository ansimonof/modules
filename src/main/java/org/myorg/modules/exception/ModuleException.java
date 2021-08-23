package org.myorg.modules.exception;

public class ModuleException extends Exception {

    public ModuleException(String s) {
        super(s);
    }

    public static <T> ModuleException buildDomainObjectEmptyValueException(Class<T> clazz, String fieldName) {
        return new ModuleException("domain_object_empty_value" + clazz.getSimpleName() + " : " + fieldName);
    }

    public static ModuleException buildAdminCannotBeDisabledException() {
        return new ModuleException("admin_cannot_be_disabled");
    }

    public static <T> ModuleException buildNotUniqueDomainObjectException(Class<T> clazz, String fieldName) {
        return new ModuleException("not_unique_domain_object " + clazz.getSimpleName() + " " + fieldName);
    }

    public static <T> ModuleException buildNotFoundDomainObjectException(Class<T> clazz, long id) {
        return new ModuleException("not_found_domain_object" + clazz.getSimpleName() + " : " + id);
    }
}
