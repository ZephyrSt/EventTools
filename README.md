# EventTools

基于事件机制的工具。用于重构代码时解耦各个模块间的耦合代码

示例:


public class AppTest {

	private static final String PUB_LOGIN_SUCCESS = "pub:login:succ";
	private static final String PUB_REGIST_SUCCESS = "pub:regist:succ";
	
    public static void main(String[] args){
    	
    	DemoEvents events = new DemoEvents();
    	
    	DemoService service = new DemoService();
    	service.regist();
    	service.login();
    }
    
    static class DemoEvents extends AbstractAnnotationEvents{
    	
    	@Subscribe({PUB_REGIST_SUCCESS,PUB_LOGIN_SUCCESS})
    	public void giveIntegral(EventSource<String> source){
    		String username = source.get();
    		System.out.println("赠送积分:"+username);
    	}
    	
    	@Subscribe(PUB_REGIST_SUCCESS)
    	public void giveFreeTicket(EventSource<String> source){
    		String username = source.get();
    		System.out.println("赠送体验券"+username);
    	}
    }
    
    static class DemoService{
    	
    	public void regist(){
    		System.out.println("用户注册成功");
    		//发布事件
    		EventManager.doEvent(PUB_REGIST_SUCCESS, new EventSource<String>("admin"));
    	}
    	
    	public void login(){
    		System.out.println("用户登陆成功");
    		//发布事件
    		EventManager.doEvent(PUB_LOGIN_SUCCESS, new EventSource<String>("admin"));
    	}
    }
}