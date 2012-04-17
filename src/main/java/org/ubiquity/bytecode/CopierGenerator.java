/**
 * 
 */
package org.ubiquity.bytecode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.*;
import org.ubiquity.Copier;
import static  org.objectweb.asm.Opcodes.*;

/**
 * @author François LAROCHE
 *
 */
public class CopierGenerator {
	
	private CopierGenerator() {}

    private static final MyClassLoader loader = new MyClassLoader();

	public static Map<String, Property> findProperties(Class<?> clazz) {
		try {
			ClassReader reader = new ClassReader(byteCodeName(clazz));
			PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
			reader.accept(visitor, 0);
			return visitor.getProperties();
		} catch (IOException e) {
			throw new IllegalStateException("Unable to parse class : ", e);
		}
	}

    private static void createConstructor(ClassWriter writer, String className) {
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, "<init>", "(Lorg/ubiquity/bytecode/CopyContext;)V", null, null);
        Label label1 = new Label();
        visitor.visitLabel(label1);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitMethodInsn(INVOKESPECIAL, "org/ubiquity/bytecode/SimpleCopier", "<init>", "(Lorg/ubiquity/bytecode/CopyContext;)V");
        Label label2 = new Label();
        visitor.visitLabel(label2);
        visitor.visitInsn(RETURN);
        Label label3 = new Label();
        visitor.visitLabel(label3);
        visitor.visitLocalVariable("this", getDescription(className), null, label1, label3, 0);
        visitor.visitLocalVariable("context", "Lorg/ubiquity/bytecode/CopyContext;", null, label1, label3, 1);
        visitor.visitMaxs(2, 2);
        visitor.visitEnd();
    }

    private static  void createNewInstance(ClassWriter writer, String srcName, String destinationName) {
        MethodVisitor visitor = writer.visitMethod(ACC_PROTECTED + ACC_VOLATILE + ACC_BRIDGE, "newInstance", "()Ljava/lang/Object;", null, null);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, "newInstance", "()" + getDescription(destinationName));
        visitor.visitInsn(ARETURN);
        visitor.visitMaxs(1, 1);
        visitor.visitEnd();

        visitor = writer.visitMethod(ACC_PROTECTED, "newInstance", "()" + getDescription(destinationName), null, null);
        visitor.visitTypeInsn(NEW, destinationName);
        visitor.visitInsn(DUP);
        visitor.visitMethodInsn(INVOKESPECIAL, destinationName, "<init>", "()V");
        visitor.visitInsn(ARETURN);
        visitor.visitMaxs(2, 1);
        visitor.visitEnd();
    }

    private static  void createCopyBridge(ClassWriter writer, String className, String srcName, String destinationName) {
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC + ACC_VOLATILE + ACC_BRIDGE, "copy", "(Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitTypeInsn(CHECKCAST, srcName);
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitTypeInsn(CHECKCAST, destinationName);
        visitor.visitMethodInsn(INVOKEVIRTUAL, className, "copy", '(' + getDescription(srcName) + getDescription(destinationName) + ")V");
        visitor.visitInsn(RETURN);
        visitor.visitMaxs(3, 3);
        visitor.visitEnd();
    }
	
	public static <T, U> Copier<T, U> createCopier(Class<T> src, Class<U> destination, CopyContext ctx)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        List<Property> properties = listCompatibelProperties(src, destination);
        String srcName = byteCodeName(src);
        String destinationName = byteCodeName(destination);
        String className = "org/ubiquity/bytecode/generated/Copier" + src.getSimpleName() + destination.getSimpleName();

        ClassWriter writer = new ClassWriter(0);
        writer.visit(V1_5, ACC_PUBLIC + ACC_FINAL, className,
                "Lorg/ubiquity/bytecode/SimpleCopier<" + getDescription(srcName) + getDescription(destinationName) + ">;",
                "org/ubiquity/bytecode/SimpleCopier", null);

        createConstructor(writer, className);
        createNewInstance(writer, className, destinationName);
        createCopyBridge(writer, className, srcName, destinationName);

        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC + ACC_FINAL, "copy", '(' + getDescription(srcName) + getDescription(destinationName) + ")V", null, null);
        for(Property p : properties) {
            visitor.visitVarInsn(ALOAD, 2);
            visitor.visitVarInsn(ALOAD, 1);
            visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.getGetter(), "()" + getDescription(p.getTypeGetter()));
            visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.getSetter(), "(" + getDescription(p.getTypeSetter()) + ")V");
        }
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitInsn(RETURN);
        visitor.visitMaxs(3,3);
        visitor.visitEnd();

        writer.visitEnd();

        Class<?> resultClass = loader.defineClass(className.replaceAll("[/]", "."), writer.toByteArray());
        @SuppressWarnings("unchecked")
        Copier<T,U> instance =  (Copier<T,U>) resultClass.getConstructor(CopyContext.class).newInstance(ctx);
        ctx.registerCopier(src, destination, instance);
        ctx.createRequiredCopiers();
        return instance;
	}

    public static <T> Copier<T, T> createCopier(Class<T> clazz, CopyContext ctx)
            throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        return createCopier(clazz, clazz, ctx);

    }

    private static List<Property> listCompatibelProperties(Class<?> source, Class<?> destination) {
        List<Property> compatibleProperties = new ArrayList<Property>();
        Map<String, Property> srcProperties = findProperties(source);
        Map<String, Property> targetProperties = findProperties(destination);

        for(String name : srcProperties.keySet()) {
            Property property = srcProperties.get(name);
            if(!property.isReadable()) {
                continue;
            }
            Property dest = targetProperties.get(name);
            if(dest != null && dest.isWritable()) {
                Property p = new Property(dest.getTypeGetter());
                p.setGetter(property.getGetter());
                p.setTypeGetter(property.getTypeGetter());
                p.setSetter(dest.getSetter());
                p.setTypeSetter(dest.getTypeSetter());
                compatibleProperties.add(p);
                continue;
            }
            /*for(Property targetProperty : targetProperties.values()) {
                if(!targetProperty.isWritable()) {
//                    if()) {
//
//                    }
                    continue;
                }
                // TODO : check compatibility
            }*/
        }

        return compatibleProperties;
    }

	private static String byteCodeName(Class<?> c) {
		return c.getName().replaceAll("[\\.]", "/");
	}

    private static class MyClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

    private static String getDescription(String className){
        if(className.indexOf('/') < 0) {
            return className;
        }
        return 'L' + className + ';';
    }
}
