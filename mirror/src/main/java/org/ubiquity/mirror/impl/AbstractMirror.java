package org.ubiquity.mirror.impl;

import org.ubiquity.mirror.Function;
import org.ubiquity.mirror.Mirror;
import org.ubiquity.mirror.Property;

import java.util.Map;

/**
 *
 */
public abstract class AbstractMirror<T> implements Mirror<T> {

    private final Map<String, Property<T, ?>> properties = buildProperties();

    @Override
    @SuppressWarnings("unchecked") // syntaxic sugar, callers should know what to expect.
    public <U> Property<T, U> getProperty(String name) {
        return (Property<T, U>) properties.get(name);
    }

    @Override
    public <U> Function<T, U> getFunction(String name) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    protected abstract Map<String, Property<T, ?>> buildProperties();

}
