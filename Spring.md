# 스프링 정리

> 스프링 컨텍스트 는 루트와 자식컨텍스트로 나눠진다.  
> 전통적인 XML 기반 접근 방식  
> 웹 응용 프로그램을 작성하는 대부분의 Spring 사용자는 Spring(DispatcherServlet)을 등록해야한다.  
> WEB-INF/web.xml  

ContextLoaderListener 를 만드는 이유
ContextLoaderListener 가 하는 역할

> ContextLoaderListener 에 해당하는 부분이 root context 가 되고  
> DispatcherServlet 에 해당하는 부분이 child context 가 된다  

https://docs.spring.io/autorepo/docs/spring/4.0.3.RELEASE/javadoc-api/org/springframework/web/WebApplicationInitializer.html

## spring xml 에서 java config 로 변경 


### XML

    <context-param>
        <param-name>contextConfigLocation</param-name>
      <param-value>
           /WEB-INF/mars-ibatis.xml 
           /WEB-INF/mars-service.xml 
      </param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <servlet>
	<servlet-name>action</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/config/egovframework/springmvc/dispatcher-servlet.xml</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
	<servlet-name>action</servlet-name>
	<url-pattern>*.do</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
	<servlet-name>action</servlet-name>
	<url-pattern>*.json</url-pattern>
    </servlet-mapping>

### JAVA

    public class MyWebAppInitializer implements WebApplicationInitializer {

	private static final String ROOT_CONFIG_LOCATION = "classpath*:/egovframework/spring/context-*.xml";
	private static final String CONFIG_LOCATION = "/WEB-INF/config/egovframework/springmvc/dispatcher-servlet.xml";
	
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		XmlWebApplicationContext rootContext = getXmlRootContext();
		servletContext.addListener(new ContextLoaderListener(rootContext));
		
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet());
       
		dispatcher.setLoadOnStartup(1);
		dispatcher.setInitParameter("contextConfigLocation", CONFIG_LOCATION);
		dispatcher.addMapping("*.do");
		dispatcher.addMapping("*.json");
	}
	
	private XmlWebApplicationContext getXmlRootContext() {
		XmlWebApplicationContext context = new XmlWebApplicationContext();
		context.setConfigLocation(ROOT_CONFIG_LOCATION);
		return context;
	}
    }
    
 > 실제위치  
 > 프로젝트명 osp_sdn  
 > src>egovframework>spring>context-***.xml  여러개있음  

### ROOT_CONFIG_LOCATION 경로 지정 방법

1. classpath:/egovframework/spring/context-*.xml
2. classpath*:/egovframework/spring/context-*.xml
3. classpath:/egovframework/spring/*.xml








