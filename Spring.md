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

### DispatcherServlet
기본적으로 **DispatcherServlet** 은 모든 Spring MVC 애플리케이션 의 진입 점이다 . 
이것의 **목적**은 HTTP 요청 을 가로 챈 다음 HTTP 요청을 처리하는 방법을 알 수있는 올바른 구성 요소에 요청을 전달하는 것이다.
레거시 Spring 프로젝트 를 다루는 경우 XML 구성 을 찾는 것이 일반적 이며 
Spring 3.1 까지는 DispatcherServlet 을 구성하는 유일한 방법  은 WEB-INF / web.xml 파일 을 사용하는 것이었다.

서블릿 API 버전 3.0을 채택 함으로써 **web.xml 파일은 선택 사항**이되었으며 이제 Java를 사용하여 DispatcherServlet 을 구성 할 수 있다 .
**WebApplicationInitializer**를 구현하는 서블릿을 등록 할 수 있습니다 

이렇게 web.xml을 이용하지 않고 자바빈 스타일을 이용함으로써 얻는 **장점**은 무엇일까?
가장 큰 이점은 서버가 구동되거나, 어플리케이션이 초기화될 때 뭔가 작업을 할 수 있다는 것이다(쉽게 말하면 어플리케이션의 Bootstrap 역할).


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








