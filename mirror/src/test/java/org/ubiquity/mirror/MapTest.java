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
package org.ubiquity.mirror;

import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.ubiquity.mirror.util.Mirrors;

import java.util.Map;

/**
 */
public class MapTest {

    @Test
    public void testMaps() {
        Map<String, Object> map = Maps.newHashMap();
        Mirror mirror = Mirrors.newMirrorFactory().getMirror(map.getClass());
        Property<Map, Object> property = mirror.getProperty("property");
        property.set(map, "test");
        Assert.assertEquals("test", map.get("property"));
        Assert.assertEquals("test", property.get(map));
    }

}
