package org.myorg.modules.exception;

import org.json.JSONObject;
import org.myorg.modules.access.AccessOp;
import org.myorg.modules.access.PrivilegeEnum;
import org.myorg.modules.querypool.database.DomainObject;
import org.myorg.modules.web.auth.context.UnauthContext;
import org.myorg.modules.web.auth.context.source.Source;

import java.util.HashMap;
import java.util.Map;

public class ModuleExceptionBuilder {

    protected static ModuleException build(String code) {
        return build(code, null, null);
    }

    protected static ModuleException build(String code, String message) {
        return build(code, message, null);
    }

    protected static ModuleException build(String code, Map<String, Object> params) {
        return build(code, null, params);
    }

    protected static ModuleException build(String code, String message, Map<String, Object> params) {
        JSONObject exceptionBody = new JSONObject();
        exceptionBody.put("error", new JSONObject() {{
            put("code", code);
            put("message", message);
            put("params", params != null ? new JSONObject(params) : null);
        }});

        return new ModuleException(exceptionBody.toString());
    }

    public static ModuleException buildAppAlreadyInitialized() {
        return build("app_is_already_initialized");
    }

    public static ModuleException buildDomainObjectEmptyValueException(Class<? extends DomainObject> clazz, String fieldName) {
        return build("domain_object_empty_value", new HashMap<String, Object>() {{
            put("class", clazz.getSimpleName());
            put("fieldName", fieldName);
        }});
    }

    public static ModuleException buildAdminCannotBeDisabledException() {
        return build("admin_cannot_be_disabled");
    }

    public static ModuleException buildNotUniqueDomainObjectException(Class<? extends DomainObject> clazz, String fieldName, String value) {
        return build("not_unique_domain_object ", new HashMap<String, Object>() {{
            put("class", clazz.getSimpleName());
            put("fieldName", fieldName);
            put("value", value);
        }});
    }

    public static ModuleException buildNotFoundDomainObjectException(Class<? extends DomainObject> clazz, long id) {
        return build("not_found_domain_object", new HashMap<String, Object>() {{
            put("class", clazz.getSimpleName());
            put("id", id);
        }});
    }

    public static ModuleException buildNotFoundDomainObjectException(Class<? extends DomainObject> clazz,
                                                                         String fieldName, String value) {
        return build("not_found_domain_object", new HashMap<String, Object>() {{
            put("class", clazz.getSimpleName());
            put("fieldName", fieldName);
            put("value", value);
        }});
    }

    public static ModuleException buildAccessDeniedException() {
        return build("access_denied");
    }

    public static ModuleException buildIncorrectCredentialsException() {
        return build("incorrect_credentials");
    }

    public static ModuleException buildAmbiguityAuthException() {
        return build("ambiguity_auth_exception");
    }

    public static ModuleException buildUserIsBannedException() {
        return build("user_is_banned");
    }

    public static ModuleException buildBadAuthContextException(Class<? extends UnauthContext> needed,
                                                               Class<? extends UnauthContext> provided) {
        return build("bad_auth_context", new HashMap<String, Object>() {{
            put("needed", needed.getSimpleName());
            put("provided", provided.getSimpleName());
        }});
    }

    public static ModuleException buildBadSourceException(Class<? extends Source> needed,
                                                          Class<? extends Source> provided) {
        return build("bad_source", new HashMap<String, Object>() {{
            put("needed", needed.getSimpleName());
            put("provided", provided.getSimpleName());
        }});
    }

    public static ModuleException buildNoSuchPrivilegeException() {
        return build("no_such_privilege");
    }

    public static ModuleException buildAccessOpCannotBeNullException() {
        return build("access_op_cannot_be_null");
    }

    public static ModuleException buildPrivilegeDoesNotHaveAccessOpException(PrivilegeEnum privilege,
                                                                    AccessOp accessOp) {
        return build("privilege_does_not_have_access_op", new HashMap<String, Object>() {{
            put("privilege", privilege.getUniqueKey());
            put("op", accessOp.toString());
        }});
    }

}
