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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.objectweb.asm.Opcodes;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Utility class containing various constants
 */
public final class Constants {
    private Constants() {
        // Do not instantiate objects
    }

    /**
     * Map containing all the native types and their object equivalents
     */
    public static final Map<String, String> SIMPLE_PROPERTIES = ImmutableMap.<String, String>builder()
            .put("Z", "Ljava/lang/Boolean;")
            .put("Ljava/lang/Boolean;", "Z")
            .put("C", "Ljava/lang/Character;")
            .put("Ljava/lang/Character;", "C")
            .put("B", "Ljava/lang/Byte;")
            .put("Ljava/lang/Byte;", "B")
            .put("S", "Ljava/lang/Short;")
            .put("Ljava/lang/Short;", "S")
            .put("I", "Ljava/lang/Integer;")
            .put("Ljava/lang/Integer;", "I")
            .put("F", "Ljava/lang/Float;")
            .put("Ljava/lang/Float;", "F")
            .put("J", "Ljava/lang/Long;")
            .put("Ljava/lang/Long;", "J")
            .put("D", "Ljava/lang/Double;")
            .put("Ljava/lang/Double;", "D")
            .build();

    /**
     * Version of ASM used.
     * Changing it here will change it everywhere needed
     */
    public static final int ASM_LEVEL = Opcodes.ASM4;

    /**
     * Target java version used to write classes.
     * Changing it here will change it everywhere.
     */
    public static final int JAVA_VERSION = Opcodes.V1_6;

    /**
     * Simple splitter used to split generics signatures
     */
    public static final Pattern SEPARATOR_PATTERN = Pattern.compile(":");

    /**
     * Collection names
     */
    public static final Set<String> COLLECTIONS =
            ImmutableSet.of("Ljava/util/Collection;", "Ljava/util/List;", "Ljava/util/Set;", "Ljava/util/Map;");

}
