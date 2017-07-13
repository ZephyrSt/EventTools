package org.tools.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 将注解修饰的方法解析为 Subscriber
 * 
 */
public class AnnotationSubscriberResolver {

	private Class<? extends Annotation> annotationClass;

	public AnnotationSubscriberResolver() {
		this.annotationClass = Subscribe.class;
	}

	public List<EventListener> getSubscribers(Object instance) {
		List<Method> methods = getAnnotatedMethods(instance.getClass(), Subscribe.class);
		List<EventListener> list = new ArrayList<EventListener>();
		for (Method m : methods) {
			Subscribe subscribe = m.getAnnotation(Subscribe.class);
			String[] destinations = subscribe.value();
			if (destinations != null) {
				for (String d : destinations) {
					list.add(new MethodSubscriber(d, instance, m));
				}
			}
		}
		return list;
	}

	/**
	 * 获取类型的方法中拥有指定注解修饰的方法集合
	 * 
	 * @param type
	 *            类型
	 * @param annotation
	 *            指定的注解
	 * @return
	 */
	protected static List<Method> getAnnotatedMethods(final Class<?> type,
			final Class<? extends Annotation> annotation) {
		final List<Method> methods = new ArrayList<Method>();
		Class<?> clazz = type;
		while (!Object.class.equals(clazz)) {
			Method[] currentClassMethods = clazz.getDeclaredMethods();
			for (final Method method : currentClassMethods) {
				if (annotation == null
						|| method.isAnnotationPresent(annotation)) {
					methods.add(method);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return methods;
	}

	protected Class<? extends Annotation> getAnnotationClass() {
		return annotationClass;
	}
}
