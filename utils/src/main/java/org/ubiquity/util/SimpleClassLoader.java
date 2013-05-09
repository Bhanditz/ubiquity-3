package org.ubiquity.util;

/**
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public final class SimpleClassLoader extends ClassLoader {

    public <T> Class<T> defineClass(String name, byte[] content) {
        return (Class<T>) this.defineClass(name, content, 0, content.length);
    }

}
