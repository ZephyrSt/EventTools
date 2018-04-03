package org.tools.event;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件监听管理器
 */
public class EventManager {

	private static Logger log = LoggerFactory.getLogger(EventManager.class);
	
	//用于存储事件源对应的事件监听器
	private static Map<String, List<EventListener>> publisherMap = new ConcurrentHashMap<String, List<EventListener>>();
	
	//使用注解 {@link Subscribe}声明的监听器的解析器
	private static AnnotationSubscriberResolver subscribeResolver = new AnnotationSubscriberResolver();
	
	/**
	 * 发布事件
	 * <p>
	 * 因为此组件只是为了代码的解耦，
 	 * 所以此方法是采用了循环的方式来执行事件监听，因此与原来的方法处于同一事务（如果事务存在）。
 	 * 为了事务的完整性，对于 {@link EventListener#doEvent(EventSource)}方法的异常采取抛出到上层的处理方式
 	 * <p>
 	 * 此处可以改为使用队列存储事件，然后利用线程（等待/通知）/线程池处理，来达到分离事务的目的.
 	 * 如果使用此方式，不能保证数据的一致性。并且对于监听处理代码（EventListener#doEvent(source)）
 	 * 最好进行异常的处理,
 	 * 以防止监听器链的断裂（因为一个监听器导致后面的监听器都不执行，最后处理时搞不清哪些执行了，哪些没执行）
 	 * 
	 * @param destination 事件的唯一标识
	 * @param message
	 */
	public static void doEvent(String destination, EventSource<?> source){
		List<EventListener> listeners = publisherMap.get(destination);
		if(listeners!= null && listeners.size()>0){
			Iterator<EventListener> iterator = listeners.iterator();
			while( iterator.hasNext()) {
				if(log.isDebugEnabled()){
					log.debug("publisher destination={}, eventSource={}", destination, source.get());
				}
				EventListener listener = iterator.next();
				listener.doEvent(source);
			}
		}
	}
	
	/**
	 * 添加事件监听器
	 * @param subscriber
	 */
	public static void addListener(EventListener subscriber) {
		String destination = subscriber.getDestination();
		List<EventListener> listeners = publisherMap.get(destination);
		if(listeners == null){
			listeners = Collections.synchronizedList(new LinkedList<EventListener>());
			publisherMap.put(destination, listeners);
		}
		listeners.add(subscriber);
	}
	
	/**
	 * 添加注解修饰的监听器
	 * @param instance
	 */
	public static void addAnnotationListener(Object instance){
		List<EventListener> list = subscribeResolver.getSubscribers(instance);
		for(EventListener s: list){
			addListener(s);
		}
	}
}
