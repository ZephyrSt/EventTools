package org.tools.event;

/**
 * 事件监听器的抽象父类
 *
 */
public abstract class EventListener {

	/**
	 * 监听的事件的目标（的标识）
	 * 
	 */
	private String destination;
	
	/**
	 * 根据监听的标识创建一个订阅者
	 * @param destination
	 */
	public EventListener(String destination) {
		this.destination = destination;
	}

	protected String getDestination() {
		return destination;
	}

	/**
	 * 监听器需要执行的内容
	 */
	public abstract void doEvent(EventSource<?> source);
}
