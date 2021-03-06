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
package org.ubiquity.util;

import org.ubiquity.copy.CollectionFactory;

import java.util.*;

/**
 * Simple collection factory that should be fine for most cases.
 *
 * Date: 24/04/12
 *
 * @author François LAROCHE
 */
public enum DefaultCollectionFactory implements CollectionFactory {
    INSTANCE;

    @Override
    public <T> List<T> newList() {
        return new ArrayList<T>();
    }

    @Override
    public <T> Set<T> newSet() {
        return new HashSet<T>();
    }

    @Override
    public <K, T> Map<K, T> newMap() {
        return new HashMap<K, T>();
    }

    @Override
    public <T> Collection<T> newCollection() {
        return new ArrayList<T>();
    }
}
