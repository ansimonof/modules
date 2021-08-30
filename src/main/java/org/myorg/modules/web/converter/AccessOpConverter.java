package org.myorg.modules.web.converter;

import org.myorg.modules.access.AccessOp;
import org.springframework.core.convert.converter.Converter;

public class AccessOpConverter implements Converter<String, AccessOp> {

    @Override
    public AccessOp convert(String source) {
        try {
            return AccessOp.valueOf(source);
        } catch (Exception e) {
            return null;
        }
    }
}
