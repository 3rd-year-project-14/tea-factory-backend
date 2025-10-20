package org.modelmapper;

import org.springframework.beans.BeanUtils;

public class ModelMapper {

    public <D> D map(Object source, Class<D> destinationType) {
        if (source == null) return null;
        try {
            D dest = destinationType.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, dest);
            return dest;
        } catch (Exception e) {
            throw new RuntimeException("ModelMapper shim failed to map objects", e);
        }
    }
}

