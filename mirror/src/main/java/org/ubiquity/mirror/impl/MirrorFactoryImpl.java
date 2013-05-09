package org.ubiquity.mirror.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.ubiquity.mirror.Mirror;
import org.ubiquity.mirror.MirrorFactory;
import org.ubiquity.util.ClassDefinition;
import org.ubiquity.util.SimpleClassLoader;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public final class MirrorFactoryImpl implements MirrorFactory {

    LoadingCache<Class<?>, Mirror<?>> mirrorCache;

    final SimpleClassLoader loader;

    public MirrorFactoryImpl(final SimpleClassLoader loader) {
        this.loader = loader;
        this.mirrorCache = CacheBuilder.newBuilder().build(new CacheLoader<Class<?>, Mirror<?>>() {
            @Override
            public Mirror<?> load(Class<?> aClass) throws Exception {
                Collection<ClassDefinition> definitions = MirrorGenerator.generateMirror(aClass);
                Class<?> c = null;
                for(ClassDefinition def : definitions) {
                    c = loader.defineClass(def.getClassName(), def.getClassContent());
                }
                if(c != null) {
                    return (Mirror) c.newInstance();
                }
                return null;
            }
        });
    }

    @Override
    public <T> Mirror<T> getMirror(Class<T> requestedClass) {
        try {
            return (Mirror<T>) mirrorCache.get(requestedClass);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
