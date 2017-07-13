package org.tools.event;

/**
 * 事件源
 * @author rongniu
 *
 * @param <T>
 */
public class EventSource<T> {
	
	private T object;

	public EventSource(T obj){
		this.object = obj;
	}
	
	public T get(){
		return object;
	}
}
