## XML설정파일에서 JAVA CONFIG로의 변경 ISSUE


### 아래의 XML 코드를 예로 들 경우

## XML
~~~
	<welcome-file-list>
		<welcome-file>index.html</welcome-file>
		<welcome-file>index.do</welcome-file>
		<welcome-file>index.jsp</welcome-file>
	</welcome-file-list>
~~~

#### stackoverflow에서 발췌한 개발자들의 의견
##### 문제 web.xml에 존재하는 welcom-file-list를 JAVA code로 변경하려면 어떻게 해야 되나요?

 * 실제로 아무 것도 할 필요가 없으며, spring은 자동으로 index.html파일을 찾는다.
   index.html파일을 만들고 루트(src/main/webapp) 아래에 넣기 만하면 된다.
 * Servlets 3.0으로 이동한다고해서 반드시 web.xml을 제거해야하는 것은 아니다.
 * web.xml이 서블릿 버전 3.0을 사용하는 한 컨테이너는 web.xml과 webservlet 부트 스트 래퍼를 모두로드한다.

#### 한가지 방법이 있다면
## JAVA
~~~
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.myapp.controllers" })
public class ApplicationConfig extends WebMvcConfigurerAdapter {

  @Bean
  public InternalResourceViewResolver getViewResolver() {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setPrefix("/WEB-INF/view/");
    viewResolver.setSuffix(".jsp");
    return viewResolver;
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("home");
  }

}
~~~

 > 위와 같이 **WebMvcConfigurerAdapter** 클래스를 상속받아서 **addViewControllers 메소드를 재정의** 해주면 된다.  

 > 하지만 **spring 5.0**에서는  
 > **WebMvcConfiurer** 를 제공하지 않는다.  
 
 > 이것은 **Java 8**이 WebMvcConfigurerAdapater클래스 의 기능을 다루는 인터페이스에 기본 메소드를 도입했기 때문이다.   
 > 만약 그래도 사용하고자 한다면 Spring 5부터는 WebMvcConfigurer 인터페이스를 구현할 필요가 있다.  

 > 아래문서를 참고하면 WebMvcConfigurerAdapater클래스는 spring5.0에서는 더이상 사용되지 않는다고 나와있음.  
 > 버전UP시 문제의 소지가 있음.
[Spring docs WebMvcConfigurerAdapater클래스](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/WebMvcConfigurerAdapter.html)


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
    
