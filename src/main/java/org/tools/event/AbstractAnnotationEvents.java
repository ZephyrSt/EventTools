package org.tools.event;

/**
 * 抽象的注解事件监听
 * <p/>
 * 此类提供了实例化时将监听方法注册到EventManager的构造函数
 * @author rongniu
 *
 */
public abstract class AbstractAnnotationEvents {

	public AbstractAnnotationEvents() {
		EventManager.addAnnotationListener(this);
	}
}
