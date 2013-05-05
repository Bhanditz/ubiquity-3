package org.ubiquity.mirror;

import com.google.common.collect.ImmutableMap;
import org.ubiquity.mirror.impl.AbstractMirror;
import org.ubiquity.mirror.impl.AbstractProperty;
import org.ubiquity.mirror.objects.ValueObject;

import java.util.Map;

/**
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public class GeneratedMirror extends AbstractMirror<ValueObject> {

    @Override
    protected Map<String, Property<ValueObject, ?>> buildProperties() {
        return ImmutableMap.<String, Property<ValueObject, ?>>builder().put("property1", new Property1())
                .put("property2", new Property2())
                .put("property3", new Property3()).build();
    }

    static class Property1 extends AbstractProperty<ValueObject, String> {
        public Property1() {
            super("property1", String.class);
        }

        @Override
        public String get(ValueObject object) {
            return object.getProperty1();
        }

        @Override
        public void set(ValueObject object, String value) {
            object.setProperty1(value);
        }

        @Override
        public boolean isReadable() {
            return true;
        }

        @Override
        public boolean isWritable() {
            return true;
        }
    }

    static class Property2 extends AbstractProperty<ValueObject, String> {
        public Property2() {
            super("property2", String.class);
        }

        @Override
        public String get(ValueObject object) {
            return object.getProperty2();
        }

        @Override
        public void set(ValueObject object, String value) {
            object.setProperty2(value);
        }

        @Override
        public boolean isReadable() {
            return true;
        }

        @Override
        public boolean isWritable() {
            return true;
        }
    }

    static class Property3 extends AbstractProperty<ValueObject, String> {
        public Property3() {
            super("property3", String.class);
        }

        @Override
        public String get(ValueObject object) {
            return object.getProperty3();
        }

        @Override
        public void set(ValueObject object, String value) {
            object.setProperty3(value);
        }

        @Override
        public boolean isReadable() {
            return true;
        }

        @Override
        public boolean isWritable() {
            return true;
        }
    }
}
