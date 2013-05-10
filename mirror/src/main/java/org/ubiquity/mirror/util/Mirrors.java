/**
 * Copyright 2012 ubiquity-copy

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.ubiquity.mirror.util;

import org.ubiquity.mirror.MirrorFactory;
import org.ubiquity.mirror.impl.MirrorFactoryImpl;
import org.ubiquity.util.SimpleClassLoader;

/**
 * Helper class, with static factories
 *
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public final class Mirrors {
    private Mirrors() {
        // Do not instanciate
    }

    public static MirrorFactory newMirrorFactory() {
        return newMirrorFactory(new SimpleClassLoader());
    }

    public static MirrorFactory newMirrorFactory(SimpleClassLoader loader) {
        return new MirrorFactoryImpl(loader);
    }


}
