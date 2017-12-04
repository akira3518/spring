# XML설정파일에서 JAVA CONFIG로의 변경

https://docs.spring.io/autorepo/docs/spring/4.0.3.RELEASE/javadoc-api/org/springframework/web/WebApplicationInitializer.html

서블릿 API 버전 3.0을 채택 함으로써 **web.xml 파일은 선택 사항**이되었으며
이제 **Java를 사용하여 DispatcherServlet을 구성** 할 수 있다 .  
**WebApplicationInitializer**를 구현하는 서블릿을 등록 할 수 있다.  

 > JAVA code로 변경하더라도 web.xml과 병행해서 사용할 수 있다.
 > 하지만 서블릿은 3.0 이상이어야 한다.

### web.xml을 이용하지 않고 자바빈 스타일을 이용함으로써 얻는 **장점**은 무엇일까?  
 * 가장 큰 이점은 서버가 구동되거나, 어플리케이션이 초기화될 때 뭔가 작업을 할 수 있다는 것이다(쉽게 말하면 어플리케이션의 Bootstrap 역할).  
 * 설정 파일을 따로 유지할 필요가 없다. 그냥 자바 클래스이다. 찾기 쉽다.
 * 보다 명료하다. 어떤 것들이 빈으로 만들어지는지 파악하기 쉽다.
 * IDE의 자동완성 기능을 사용할 수 있다. 자바 코드이기 때문이다. 그래서 작성과 수정이 빠르다.
 * 어플리케이션 로직과 설정 코드를 동일한 언어로 만들 수 있다. 한 언어만 쓰는게 간편하니 좋다.
 * 설정 코드에 break point 를 걸어서 디버깅할 수 있다.


#### 아래는 실제 XML에서 JAVA CODE로 변경된 부분만 정리함.
----
## XML
~~~
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee"
xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
id="WebApp_ID" version="3.0">

<filter>
	<filter-name>encodingFilter</filter-name>
	<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
	<init-param>
		<param-name>encoding</param-name>
		<param-value>utf-8</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>encodingFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
<filter>
	<filter-name>HTMLTagFilter</filter-name>
	<filter-class>egovframework.rte.ptl.mvc.filter.HTMLTagFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>HTMLTagFilter</filter-name>
	<url-pattern>*.do</url-pattern>
</filter-mapping>
<filter-mapping>
	<filter-name>HTMLTagFilter</filter-name>
	<url-pattern>*.json</url-pattern>
</filter-mapping>
<filter>
	<filter-name>AuthenticationFilter</filter-name>
	<filter-class>osp.sdn.common.web.filter.AuthenticationFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>AuthenticationFilter</filter-name>
	<url-pattern>*.do</url-pattern>
</filter-mapping>
<filter-mapping>
	<filter-name>AuthenticationFilter</filter-name>
	<url-pattern>*.json</url-pattern>
</filter-mapping>

<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>classpath*:egovframework/spring/context-*.xml</param-value>
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
</web-app>
~~~
## JAVA
~~~
public class MyWebAppInitializer implements WebApplicationInitializer {
 private static final String ROOT_CONFIG_LOCATION = "classpath:/egovframework/spring/*.xml";
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
  
  this.addIncodingFilter(servletContext);    
 }
 
 private XmlWebApplicationContext getXmlRootContext() {
  XmlWebApplicationContext context = new XmlWebApplicationContext();
  context.setConfigLocation(ROOT_CONFIG_LOCATION);
  return context;   
 }
 
 private void addIncodingFilter(ServletContext servletContext){  
  FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("CHARACTER_ENCODING_FILTER", CharacterEncodingFilter.class);
  encodingFilter.setInitParameter("encodig", "UTF-8");
  encodingFilter.addMappingForUrlPatterns(null, false, "/*");
  
  
  FilterRegistration.Dynamic htmlTagFilter = servletContext.addFilter("HTML_TAG_FILTER", HTMLTagFilter.class);
  htmlTagFilter.addMappingForUrlPatterns(null, false, "*.do");
  htmlTagFilter.addMappingForUrlPatterns(null, false, "*.json");
  
  FilterRegistration.Dynamic authenticationFilter = servletContext.addFilter("AUTHENTICATION_FILTER", AuthenticationFilter.class);
  authenticationFilter.addMappingForUrlPatterns(null, false, "*.do");
  authenticationFilter.addMappingForUrlPatterns(null, false, "*.json");
  
 }
}
 ~~~
    


### ROOT_CONFIG_LOCATION 경로 지정 방법
 > 실제위치  
 > src>egovframework>spring>context-***.xml
 * classpath:/egovframework/spring/context-*.xml
 * classpath*:/egovframework/spring/context-*.xml
 * classpath:/egovframework/spring/*.xml

 > JAVA code를 보게되면 MyWebAppInitializer(임의의 클래스명) 클래스를 만들어서 **WebApplicationInitializer 인터페이스**를 구현했다.  
 > 테스트한 결과 해당 클래스가 어느곳에 위치했든지 **WebApplicationInitializer 인터페이스**를 구현하게 되면   
 > TOMCAT이 구동되면서 재정의한 **onStartup** 메소드를 찾아간다.  
  
 > 위에서는 XML을 모두 걷어내지 않고 web.xml의 code만 JAVA code로 변경 했다.  
  
 > **ContextLoaderListener** 에 해당하는 xml을 모두 JAVA code로 변경하게 되면  
 > **XmlWebApplicationContext** 를 **AnnotationApplicationContext** 로 변경하고  
 > JAVA code로 변경된 설정파일을 **Annotation** 으로 지정해 주면 된다.  





