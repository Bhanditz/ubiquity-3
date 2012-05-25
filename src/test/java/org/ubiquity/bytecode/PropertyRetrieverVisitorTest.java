/**
 * 
 */
package org.ubiquity.bytecode;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.ubiquity.Copier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;

/**
 * Test for class {@link PropertyRetrieverVisitor}
 * 
 * @author François LAROCHE
 */
public class PropertyRetrieverVisitorTest {

	 @Test
     public void test() throws Exception {
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/SimpleTestClass");
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(visitor, 0);
    }

    @Test
    public void testInheritance() throws Exception {
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/InheritingClass");
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(visitor, 0);
    }
	
	@Test
	public void testBeanutils() throws Exception{
		Object dummy = new SimpleTestClass();
		BeanUtils.describe(dummy);
	}

    @Test
    public void testCopySpeed() throws Exception {
        SimpleTestClass src = new SimpleTestClass();
        src.setProperty1("property1");
        src.setProperty3("property3");
        CopyContext ctx = new CopyContext();
        Copier<SimpleTestClass, SimpleTestClass> copier = new CopierGenerator().createCopier(SimpleTestClass.class, SimpleTestClass.class, ctx);
        copier.copy(src, new SimpleTestClass());
        BeanUtils.copyProperties(new SimpleTestClass(), src);
    }

    @Test
    public void testCopierCreation() throws Exception {
        CopyContext ctx = new CopyContext();
        Copier<SimpleTestClass, SimpleTestClass> copier = new CopierGenerator().createCopier(SimpleTestClass.class, SimpleTestClass.class, ctx);
        assertNotNull(copier);
        SimpleTestClass testObject = new SimpleTestClass();
        String value = "This is a test";
        testObject.setProperty1("This is a test");
        SimpleTestClass result = copier.map(testObject);
        assertNotNull(result);
        assertEquals(value, result.getProperty1());
    }

    @Test
    public void testInternalClasses() throws Exception {
        CopyContext ctx = new CopyContext();
        Copier<InheritingClass.InternalInheritingClass, InheritingClass.InternalInheritingClass> copier =
                new CopierGenerator().createCopier(InheritingClass.InternalInheritingClass.class, InheritingClass.InternalInheritingClass.class, ctx);
        assertNotNull(copier);
        InheritingClass.InternalInheritingClass testObject = new InheritingClass.InternalInheritingClass();
        testObject.setField(2);
        InheritingClass.InternalInheritingClass result = copier.map(testObject);
        assertEquals(Integer.valueOf(2), result.getField());
    }

    @Test
    public void testMultipleImplementations() throws Exception {
        CopyContext ctx = new CopyContext();
        Copier<InheritingClass, InheritingClass2> copier =
                new CopierGenerator().createCopier(InheritingClass.class, InheritingClass2.class, ctx);
        InheritingClass src = new InheritingClass();
        src.setMyProperty("My property");
        src.setProperty1("property1");
        InheritingClass parent = new InheritingClass();
        parent.setProperty1("Parent property 1");
        src.setParent(parent);

        InheritingClass2 result = copier.map(src);
        assertNotNull(result);
        assertEquals("property1", result.getProperty1());
        assertNotNull(result.getParent());
        assertEquals("Parent property 1", result.getParent().getProperty1());
    }

    @Test
    public void testObjectArrayCopy() throws Exception {
        ObjectArrayTest src = new ObjectArrayTest();
        ObjectArrayTest.ObjectArrayTest2 destination = new ObjectArrayTest.ObjectArrayTest2();
        ObjectArrayTest[] objects = new ObjectArrayTest[1];
        ObjectArrayTest obj = new ObjectArrayTest();
        objects[0] = obj;
        src.setObjects(objects);

        CopyContext ctx = new CopyContext();
        Copier<ObjectArrayTest, ObjectArrayTest.ObjectArrayTest2> copier =
                new CopierGenerator().createCopier(ObjectArrayTest.class, ObjectArrayTest.ObjectArrayTest2.class, ctx);

        copier.copy(src, destination);
        assertNotNull(destination.getObjects());
        assertEquals(1, destination.getObjects().length);
    }

    @Test
    public void testLists() throws Exception {
        ListObject src = new ListObject();
        List<SimpleTestClass> objects = new ArrayList<SimpleTestClass>();
        SimpleTestClass elem = new SimpleTestClass();
        objects.add(elem);
        src.setObjects(objects);

        CopyContext ctx = new CopyContext();
        Copier<ListObject, ListObject> copier =
                new CopierGenerator().createCopier(ListObject.class, ListObject.class, ctx);

        ListObject dest = copier.map(src);
        assertNotNull(dest);
        assertNotNull(dest.getObjects());
        assertEquals(1, dest.getObjects().size());
    }

    @Test
    public void testLists2() throws Exception {
        ListObject src = new ListObject();
        src.setObjects(null);

        CopyContext ctx = new CopyContext();
        Copier<ListObject, ListObject> copier =
                new CopierGenerator().createCopier(ListObject.class, ListObject.class, ctx);

        ListObject dest = copier.map(src);
        assertNotNull(dest);
        assertNull(dest.getObjects());
    }

    @Test
    public void testSimpleLists1() throws Exception {
        CollectionClass object = new CollectionClass();
        List<String> objects = new ArrayList<String>();
        objects.add("ubiquity");
        object.setObjects(objects);

        CopyContext ctx = new CopyContext();
        Copier<CollectionClass, CollectionClass> copier =
                new CopierGenerator().createCopier(CollectionClass.class, CollectionClass.class, ctx);

        CollectionClass dest = copier.map(object);
        assertNotNull(dest);
        assertNotNull(dest.getObjects());
        assertEquals(1, object.getObjects().size());
        assertEquals("ubiquity", object.getObjects().iterator().next());

    }

    @Test
    public void testSimpleLists2() throws Exception {
        CollectionClass object = new CollectionClass();
        object.setObjects(null);

        CopyContext ctx = new CopyContext();
        Copier<CollectionClass, CollectionClass> copier =
                new CopierGenerator().createCopier(CollectionClass.class, CollectionClass.class, ctx);

        CollectionClass dest = copier.map(object);
        assertNotNull(dest);
        assertNull(dest.getObjects());
    }

    @Test
    public void testSimpleArrays() throws Exception {
        ByteArrayClass object = new ByteArrayClass();
        object.setArray(new byte[]{0x5, 0xa});

        CopyContext ctx = new CopyContext();
        Copier<ByteArrayClass, ByteArrayClass> copier =
                new CopierGenerator().createCopier(ByteArrayClass.class, ByteArrayClass.class, ctx);

        ByteArrayClass dest = copier.map(object);
        assertNotNull(dest);
        assertEquals(2, dest.getArray().length);
        assertEquals(0x5, dest.getArray()[0]);
        assertEquals(0xa, dest.getArray()[1]);
    }

    @Test
    public void testSimpleArraysBoxing() throws Exception {
        ByteArrayClass object = new ByteArrayClass();
        object.setArray(new byte[]{0x5, 0xa});

        CopyContext ctx = new CopyContext();
        Copier<ByteArrayClass, ByteArrayClass.ByteArray2> copier =
                new CopierGenerator().createCopier(ByteArrayClass.class, ByteArrayClass.ByteArray2.class, ctx);

        ByteArrayClass.ByteArray2 dest = copier.map(object);
        assertNotNull(dest);
        assertEquals(2, dest.getArray().length);
        assertEquals(0x5, dest.getArray()[0].byteValue());
        assertEquals(0xa, dest.getArray()[1].byteValue());
    }

    @Test
    public void testAnnotationsParsing() throws IOException{
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/AnnotatedClass");
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(visitor, 0);
        Map<String, Property> properties = visitor.getProperties();
        assertFalse(properties.isEmpty());
        assertTrue(properties.containsKey("property1"));
        Property property1 = properties.get("property1");
        assertFalse(property1.getAnnotations().isEmpty());
        assertTrue(property1.getAnnotations().contains("Lorg/ubiquity/annotation/CopyRename;:property3:Lorg/ubiquity/bytecode/SimpleTestClass;"));

        Property property3 = properties.get("property3");
        assertNotNull(property3);
        assertFalse(property3.getAnnotations().isEmpty());
        assertTrue(property3.getAnnotations().contains("Lorg/ubiquity/annotation/CopyRename;:test:*"));
        assertTrue(property3.getAnnotations().contains("Lorg/ubiquity/annotation/CopyRename;:property3:Lorg/ubiquity/bytecode/SimpleTestClass;"));
    }

    @Test
    public void testSimpleRenaming() throws Exception {
        AnnotatedClass2 object = new AnnotatedClass2();
        object.setToto("toto");


        CopyContext ctx = new CopyContext();
        Copier<AnnotatedClass2, SimpleTestClass> copier =
                new CopierGenerator().createCopier(AnnotatedClass2.class, SimpleTestClass.class, ctx);

        SimpleTestClass dest = copier.map(object);
        assertNotNull(dest);
        assertEquals("toto", dest.getProperty1());
    }

}
