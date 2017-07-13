package org.tools.event;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 方法级的监听器
 * @author rongniu
 *
 */
public class MethodSubscriber extends EventListener {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private Object instance;
	private Method method;
	
	public MethodSubscriber(String destination, Object instance, Method method) {
		super(destination);
		this.instance = instance;
		this.method = method;
	}

	@Override
	public void doEvent(EventSource<?> source) {
		try {
			if(log.isDebugEnabled()){
				log.debug("running annotation listener = {}#{}", instance.getClass().getSimpleName() , method.getName());
			}
			method.invoke(instance, source);
		} catch (Exception e) {
            throw new IllegalStateException("Unable to invoke event handler method [" + method + "].", e);
		}
	}
	
    /**
     * 验证参数类型
     * @param method
     * @return
     * @exception IllegalArgumentException 如果方法的参数的数量不为1,或不为EventSource类型，则抛出 {@link IllegalArgumentException}
     */
    protected void checkMethodArgumentType(Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 1) {
            String msg = "订阅者的参数个数必须为一个";
            throw new IllegalArgumentException(msg);
        }
        if(!paramTypes[0].equals(EventSource.class)){
            String msg = "订阅者的参数必须为EventSource类型";
            throw new IllegalArgumentException(msg);
        }
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodSubscriber other = (MethodSubscriber) obj;
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}
    
}
