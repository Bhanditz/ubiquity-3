/*
 * Copyright 2012 ubiquity-copy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ubiquity.mirror;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 */
public final class Annotation {

    static Annotation create(Class<?> annotationClass, boolean visible, java.util.Map<String, AnnotationProperty> properties) {
        return new Annotation(annotationClass, visible, properties);
    }

    private final Class<?> annotationClass;
    private final boolean visible;
    private final Map<String, AnnotationProperty> properties;

    public Annotation(Class<?> annotationClass, boolean visible, java.util.Map<String, AnnotationProperty> properties) {
        this.annotationClass = annotationClass;
        this.visible = visible;
        this.properties = ImmutableMap.copyOf(properties);
    }

    public Class<?> getAnnotationClass() {
        return annotationClass;
    }

    public boolean isVisible() {
        return visible;
    }

    public java.util.Map<String, AnnotationProperty> getProperties() {
        return properties;
    }
}
