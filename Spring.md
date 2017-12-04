<h1> 마크다운 메모와 위키와 책 </h1>
<p> 마크다운은 다음과 같은 장점이 있습니다.</p>
<ul>
	<li> 문법이 단순하여 배우기 쉽고 쓰기도 쉽습니다. </li>
    <li> html 문서로 변환되지 않아도 그 자체로 충분히 읽을 수 있습니다. </li>
    <li> 다양한 도구들을 이용해 손쉽게 html 문서로 변환될 수 있습니다 </li>
</ul>


<h1> spring xml - java </h1>


<p>XML</p>

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

<p>JAVA</p>

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


