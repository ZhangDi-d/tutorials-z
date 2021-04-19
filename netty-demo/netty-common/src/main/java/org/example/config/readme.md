
## 问题来由

### 1.1 什么是 RESTful API？

Restful（REpresentational State Transfer）就是一个资源定位及资源操作的风格。 不是标准也不是协议，只是**一种风格**。 基于这个风格设计的软件可以更简洁，更有层次，更易于实现缓存等机制。 功能资源：互联网所有的事物都可以被抽象为资源资源操作：使用POST、DELETE、PUT、GET，使用不同方法对资源进行操作。 一句话 ，URL定位资源，用HTTP描述操作。

https://spring.io/guides/tutorials/rest/


### 1.2 springmvc 构建RESTful风格 API

restfule 风格：

```java
@RequestMapping(path = "/get/{uid}", method = RequestMethod.GET)
@ResponseBody
public String getJsonByUid(@PathVariable Integer uid)
```
request： GET /get/1

 

非 restfule 风格：

```java
@RequestMapping(path = "/get", method = RequestMethod.GET)
@ResponseBody
public String getJsonByUid(@RequestParam Integer uid)
```
request： GET /get?uid=1



### 1.3 @PathVariable 性能问题

```java
@RestController
@RequestMapping("/rest")
public class TestRestController {

	    @RequestMapping(value = "/restful/{name}",headers = {"service_name=restful"},method =RequestMethod.GET )
    public String restful(@PathVariable String name) {
        System.out.println("r " + name);
        return name;
    }


    @RequestMapping(value = "/non-restful",method =RequestMethod.GET )
    public String non_restful(@RequestParam String name) {
        System.out.println("n " + name);
        return name;
    }

}
```

```
ab -n 10000 -c 200  -T 'application/json;charset=UTF-8' http://127.0.0.1:8080/rest/non-restful?name=lisi

ab -n 10000 -c 200 -T 'application/json;charset=UTF-8' http://127.0.0.1:8080/rest/restful/lisi
```

原始的比较：

|no.|restful qps|non-restful qps|
|:----|:---|:---|
|1|2970.17|3306.68|
|2|3017.07|3362.15|
|3|2959.32|3247.03|
|4|2948.59|3260.52|
|5|3006.16|3311.74|
|mean|2980.262|3297.624|

两者差距在10.7%  。

























## 2.问题分析

###  2.1 请求流程

![](https://raw.githubusercontent.com/ZhangDi-d/pic-repo/main/data/20210402103024.png)



### 2.2 springmvc的工作机制
在容器初始化时会建立所有url和controller的对应关系,保存到Map<url,controller>中.服务器（tomcat等）启动时会通知spring初始化容器(加载bean的定义信息和初始化所有单例bean),然后springmvc会遍历容器中的bean,获取每一个controller中的所有方法访问的url,然后将url和controller保存到一个Map中;

这样就可以根据request快速定位到controller,因为最终处理request的是controller中的方法,Map中只保留了url和controller中的对应关系,所以要根据request的url进一步确认controller中的method,这一步工作的原理就是拼接controller的url(controller上@RequestMapping的值)和方法的url(method上@RequestMapping的值),与request的url进行匹配,找到匹配的那个方法;

确定处理请求的method后,接下来的任务就是参数绑定,把request中参数绑定到方法的形式参数上,这一步是整个请求处理过程中最复杂的一个步骤

### 2.3  源码分析






我们根据工作机制中三部分来分析springmvc的源代码.

1. ApplicationContext初始化时建立所有url和controller类的对应关系(用Map保存);
2. 根据请求url找到对应的controller,并从controller中找到处理请求的方法;
3. request参数绑定到方法的形参,执行方法处理请求,并返回结果视图.

#### 建立Map<urls,controller>的关系

![](https://raw.githubusercontent.com/ZhangDi-d/pic-repo/main/data/20210402103953.png)



```java
public abstract class AbstractDetectingUrlHandlerMapping extends AbstractUrlHandlerMapping {

	private boolean detectHandlersInAncestorContexts = false;


	public void setDetectHandlersInAncestorContexts(boolean detectHandlersInAncestorContexts) {
		this.detectHandlersInAncestorContexts = detectHandlersInAncestorContexts;
	}


	/**
	 * Calls the {@link #detectHandlers()} method in addition to the
	 * superclass's initialization.
	 */
	@Override
	public void initApplicationContext() throws ApplicationContextException {
		super.initApplicationContext();
		detectHandlers();
	}

	//注册在当前ApplicationContext中找到的所有处理程序
	protected void detectHandlers() throws BeansException {
		ApplicationContext applicationContext = obtainApplicationContext();
		String[] beanNames = (this.detectHandlersInAncestorContexts ?
				BeanFactoryUtils.beanNamesForTypeIncludingAncestors(applicationContext, Object.class) :
				applicationContext.getBeanNamesForType(Object.class));

		// Take any bean name that we can determine URLs for.
		for (String beanName : beanNames) {
			String[] urls = determineUrlsForHandler(beanName);
			if (!ObjectUtils.isEmpty(urls)) {
				// URL paths found: Let's consider it a handler.
				registerHandler(urls, beanName);
			}
		}

		if ((logger.isDebugEnabled() && !getHandlerMap().isEmpty()) || logger.isTraceEnabled()) {
			logger.debug("Detected " + getHandlerMap().size() + " mappings in " + formatMappingName());
		}
	}


	/**
	 * Determine the URLs for the given handler bean.
	 * @param beanName the name of the candidate bean
	 * @return the URLs determined for the bean, or an empty array if none
	 */
	protected abstract String[] determineUrlsForHandler(String beanName);

}
```



```java
public class BeanNameUrlHandlerMapping extends AbstractDetectingUrlHandlerMapping {

	/**
	 * Checks name and aliases of the given bean for URLs, starting with "/".
	 */
	@Override
	protected String[] determineUrlsForHandler(String beanName) {
		List<String> urls = new ArrayList<>();
		if (beanName.startsWith("/")) {
			urls.add(beanName);
		}
		String[] aliases = obtainApplicationContext().getAliases(beanName);
		for (String alias : aliases) {
			if (alias.startsWith("/")) {
				urls.add(alias);
			}
		}
		return StringUtils.toStringArray(urls);
	}

}
```



#### 根据访问url找到对应controller中处理请求的方法


首先是作为servlet的init：

我们知道 springmvc 的关键是 DispatcherServlet。

![](https://raw.githubusercontent.com/ZhangDi-d/pic-repo/main/data/20210419115134.png)

总结一下各个Servlet的作用：

1. HttpServletBean
   主要做一些初始化的工作，将web.xml中配置的参数设置到Servlet中。比如servlet标签的子标签init-param标签中配置的参数。
2. FrameworkServlet
   将Servlet与Spring容器上下文关联。其实也就是初始化FrameworkServlet的属性webApplicationContext，这个属性代表SpringMVC上下文，它有个父类上下文，既web.xml中配置的ContextLoaderListener监听器初始化的容器上下文。
3. DispatcherServlet
   初始化各个功能的实现类。比如异常处理、视图处理、请求映射处理等。

HttpServletBean#init():
```java
@Override
	public final void init() throws ServletException {

		// Set bean properties from init parameters.
		PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
		if (!pvs.isEmpty()) {
			try {
				BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
				ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
				bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, getEnvironment()));
				initBeanWrapper(bw);
				bw.setPropertyValues(pvs, true);
			}
			catch (BeansException ex) {
				if (logger.isErrorEnabled()) {
					logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", ex);
				}
				throw ex;
			}
		}

		// Let subclasses do whatever initialization they like.
		initServletBean();
	}
```


FrameworkServlet#initServletBean():
```java
@Override
	protected final void initServletBean() throws ServletException {
		// omit log
		long startTime = System.currentTimeMillis();

		try {
			// 关键
			this.webApplicationContext = initWebApplicationContext();
			// 未作处理，供子类
			initFrameworkServlet();
		}
		catch (ServletException | RuntimeException ex) {
			logger.error("Context initialization failed", ex);
			throw ex;
		}

		// omit log
	}
```

FrameworkServlet#initWebApplicationContext()
```java

protected WebApplicationContext initWebApplicationContext() {

//获取根应用程序上下文
		WebApplicationContext rootContext =
				WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		WebApplicationContext wac = null;

		if (this.webApplicationContext != null) {
			//在构造时注入了一个上下文实例->使用它
			wac = this.webApplicationContext;
			if (wac instanceof ConfigurableWebApplicationContext) {
				ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
				if (!cwac.isActive()) {
					//上下文尚未刷新->提供诸如设置父上下文，设置应用程序上下文ID等服务
					if (cwac.getParent() == null) {
						//上下文实例是在没有显式父级->将根应用程序上下文（如果有；可能为null）设置为父级
						cwac.setParent(rootContext);
					}
					//读取配置文件或者配置类（不同的源码版本略有出入）
					configureAndRefreshWebApplicationContext(cwac);
				}
			}
		}
		if (wac == null) {
			//在构造时未注入任何上下文实例->查看是否已在servlet上下文中注册了一个实例。如果存在，则假定已经设置了父上下文（如果有），并且用户已经执行了任何初始化操作，例如设置了上下文ID
			wac = findWebApplicationContext();
		}
		if (wac == null) {
			// 没有为此Servlet定义上下文实例->创建本地实例
			wac = createWebApplicationContext(rootContext);
		}

		if (!this.refreshEventReceived) {
			//手动刷新
			synchronized (this.onRefreshMonitor) {
				onRefresh(wac);
			}
		}

		if (this.publishContext) {
			// Publish the context as a servlet context attribute.
			String attrName = getServletContextAttributeName();
			getServletContext().setAttribute(attrName, wac);
		}

		return wac;
	}
```

FrameworkServlet#onRefresh()
```java
	protected void onRefresh(ApplicationContext context) {
		// For subclasses: do nothing by default.
	}
```

DispatcherServlet # onRefresh()
```java
	@Override
	protected void onRefresh(ApplicationContext context) {
		initStrategies(context);
	}

	/**
	 * Initialize the strategy objects that this servlet uses.
	 * <p>May be overridden in subclasses in order to initialize further strategy objects.
	 */
	protected void initStrategies(ApplicationContext context) {
		initMultipartResolver(context);
		initLocaleResolver(context);
		initThemeResolver(context);
		initHandlerMappings(context);
		initHandlerAdapters(context);
		initHandlerExceptionResolvers(context);
		initRequestToViewNameTranslator(context);
		initViewResolvers(context);
		initFlashMapManager(context);
	}
```

```java
private void initHandlerMappings(ApplicationContext context) {
		this.handlerMappings = null;

		if (this.detectAllHandlerMappings) {
			// 在ApplicationContext中找到所有HandlerMappings
			Map<String, HandlerMapping> matchingBeans =
					BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
			if (!matchingBeans.isEmpty()) {
				this.handlerMappings = new ArrayList<>(matchingBeans.values());
				// 排序
			AnnotationAwareOrderComparator.sort(this.handlerMappings);
			}
		}
		else {
			try {
				HandlerMapping hm = context.getBean(HANDLER_MAPPING_BEAN_NAME, HandlerMapping.class);
				this.handlerMappings = Collections.singletonList(hm);
			}
			catch (NoSuchBeanDefinitionException ex) {
				// Ignore, we'll add a default HandlerMapping later.
			}
		}

		// 如果找不到其他映射，请通过注册默认的HandlerMapping来确保至少有一个HandlerMapping
		if (this.handlerMappings == null) {
			this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
			// 省略log
		}

		for (HandlerMapping mapping : this.handlerMappings) {
			if (mapping.usesPathPatterns()) {
				this.parseRequestPath = true;
				break;
			}
		}
	}
```

![](https://raw.githubusercontent.com/ZhangDi-d/pic-repo/main/data/20210419144818.png)


传统的Spring MVC项目启动流程如下：

- 如果在web.xml中配置了org.springframework.web.context.ContextLoaderListener，那么Tomcat在启动的时候会先加载父容器，并将其放到ServletContext中；
- 然后会加载DispatcherServlet（这块流程建议从init方法一步步往下看，流程还是很清晰的），因为DispatcherServlet实质是一个Servlet，所以会先执行它的init方法。这个init方法在HttpServletBean这个类中实现，其主要工作是做一些初始化工作，将我们在web.xml中配置的参数书设置到Servlet中，然后再触发FrameworkServlet的initServletBean()方法；
- FrameworkServlet主要作用是初始化Spring子上下文，设置其父上下文，并将其放入ServletContext中；
- FrameworkServlet在调用initServletBean()的过程中同时会触发DispatcherServlet的onRefresh()方法，这个方法会初始化Spring MVC的各个功能组件。比如异常处理器、视图处理器、请求映射处理等。












**DispatcherServlet#doDispatch()**
```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpServletRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;
		boolean multipartRequestParsed = false;

		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

		try {
			ModelAndView mv = null;
			Exception dispatchException = null;

			try {
				processedRequest = checkMultipart(request);
				multipartRequestParsed = (processedRequest != request);

				// Determine handler for the current request.
				mappedHandler = getHandler(processedRequest);
				if (mappedHandler == null) {
					noHandlerFound(processedRequest, response);
					return;
				}

				// Determine handler adapter for the current request.
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

				// Process last-modified header, if supported by the handler.
				String method = request.getMethod();
				boolean isGet = "GET".equals(method);
				if (isGet || "HEAD".equals(method)) {
					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
						return;
					}
				}

				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
					return;
				}

				// Actually invoke the handler.
				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

				if (asyncManager.isConcurrentHandlingStarted()) {
					return;
				}

				applyDefaultViewName(processedRequest, mv);
				mappedHandler.applyPostHandle(processedRequest, response, mv);
			}
			catch (Exception ex) {
				dispatchException = ex;
			}
			catch (Throwable err) {
				// As of 4.3, we're processing Errors thrown from handler methods as well,
				// making them available for @ExceptionHandler methods and other scenarios.
				dispatchException = new NestedServletException("Handler dispatch failed", err);
			}
			processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
		}
		catch (Exception ex) {
			triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
		}
		catch (Throwable err) {
			triggerAfterCompletion(processedRequest, response, mappedHandler,
					new NestedServletException("Handler processing failed", err));
		}
		finally {
			if (asyncManager.isConcurrentHandlingStarted()) {
				// Instead of postHandle and afterCompletion
				if (mappedHandler != null) {
					mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
				}
			}
			else {
				// Clean up any resources used by a multipart request.
				if (multipartRequestParsed) {
					cleanupMultipart(processedRequest);
				}
			}
		}
	}
```
有一行代码：
```java
// Determine handler for the current request.
				mappedHandler = getHandler(processedRequest);
```

DispatcherServlet#getHandler()
```java
	@Nullable
	protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		if (this.handlerMappings != null) {
			for (HandlerMapping mapping : this.handlerMappings) {
			//getHandler
            HandlerExecutionChain handler = 
			mapping.getHandler(request);
				if (handler != null) {
					return handler;
				}
			}
		}
		return null;
	}
```

AbstractHandlerMapping#getHandler():
```java

@Override
	@Nullable
	public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		Object handler = getHandlerInternal(request);
		if (handler == null) {
			handler = getDefaultHandler();
		}
		if (handler == null) {
			return null;
		}
		// Bean name or resolved handler?
		if (handler instanceof String) {
			String handlerName = (String) handler;
			handler = obtainApplicationContext().getBean(handlerName);
		}

		HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);

		// 省略log

		if (hasCorsConfigurationSource(handler) || CorsUtils.isPreFlightRequest(request)) {
			CorsConfiguration config = (this.corsConfigurationSource != null ? this.corsConfigurationSource.getCorsConfiguration(request) : null);
			CorsConfiguration handlerConfig = getCorsConfiguration(handler, request);
			config = (config != null ? config.combine(handlerConfig) : handlerConfig);
			executionChain = getCorsHandlerExecutionChain(request, executionChain, config);
		}

		return executionChain;
	}
```


AbstractHandlerMethodMapping#getHandlerInternal()
```java
	@Override
	protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
		String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
		request.setAttribute(LOOKUP_PATH, lookupPath);
		this.mappingRegistry.acquireReadLock();
		try {
			HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
			return (handlerMethod != null ? handlerMethod.createWithResolvedBean() : null);
		}
		finally {
			this.mappingRegistry.releaseReadLock();
		}
	}
```


AbstractHandlerMethodMapping#lookupHandlerMethod():
```java

@Nullable
	protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
		List<Match> matches = new ArrayList<>();
		// 通过lookupPath获取匹配到的path
		List<T> directPathMatches = this.mappingRegistry.getMappingsByUrl(lookupPath);
		if (directPathMatches != null) {
		//  非restful的接口，会匹配到并且添加到matches
			addMatchingMappings(directPathMatches, matches, request);
		}
		if (matches.isEmpty()) {
			// No choice but to go through all mappings...
	 // restful的接口，会匹配所有的URL （这就是根源所在，数量越多，性能越差）
     addMatchingMappings(this.mappingRegistry.getMappings().keySet(), matches, request);
		}

		if (!matches.isEmpty()) {
			Match bestMatch = matches.get(0);
			if (matches.size() > 1) {
				Comparator<Match> comparator = new MatchComparator(getMappingComparator(request));
				matches.sort(comparator);
				bestMatch = matches.get(0);
				if (logger.isTraceEnabled()) {
					logger.trace(matches.size() + " matching mappings: " + matches);
				}
				if (CorsUtils.isPreFlightRequest(request)) {
					return PREFLIGHT_AMBIGUOUS_MATCH;
				}
				Match secondBestMatch = matches.get(1);
				if (comparator.compare(bestMatch, secondBestMatch) == 0) {
					Method m1 = bestMatch.handlerMethod.getMethod();
					Method m2 = secondBestMatch.handlerMethod.getMethod();
					String uri = request.getRequestURI();
					throw new IllegalStateException(
							"Ambiguous handler methods mapped for '" + uri + "': {" + m1 + ", " + m2 + "}");
				}
			}
			request.setAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE, bestMatch.handlerMethod);
			handleMatch(bestMatch.mapping, lookupPath, request);
			return bestMatch.handlerMethod;
		}
		else {
			return handleNoMatch(this.mappingRegistry.getMappings().keySet(), lookupPath, request);
		}
	}

```

AbstractHandlerMethodMapping#addMatchingMappings():
```java
	private void addMatchingMappings(Collection<T> mappings, List<Match> matches, HttpServletRequest request) {
		for (T mapping : mappings) {
			T match = getMatchingMapping(mapping, request);
			if (match != null) {
				matches.add(new Match(match, this.mappingRegistry.getMappings().get(mapping)));
			}
		}
	}

```

RequestMappingInfoHandlerMapping#getMatchingMapping()
```java
	@Override
	protected RequestMappingInfo getMatchingMapping(RequestMappingInfo info, HttpServletRequest request) {
		return info.getMatchingCondition(request);
	}
```


RequestMappingInfo#RequestMappingInfo:

```java
@Override
	@Nullable
	public RequestMappingInfo getMatchingCondition(HttpServletRequest request) {
		RequestMethodsRequestCondition methods = this.methodsCondition.getMatchingCondition(request);
		if (methods == null) {
			return null;
		}
		ParamsRequestCondition params = this.paramsCondition.getMatchingCondition(request);
		if (params == null) {
			return null;
		}
		HeadersRequestCondition headers = this.headersCondition.getMatchingCondition(request);
		if (headers == null) {
			return null;
		}
		ConsumesRequestCondition consumes = this.consumesCondition.getMatchingCondition(request);
		if (consumes == null) {
			return null;
		}
		ProducesRequestCondition produces = this.producesCondition.getMatchingCondition(request);
		if (produces == null) {
			return null;
		}
		PathPatternsRequestCondition pathPatterns = null;
		if (this.pathPatternsCondition != null) {
			pathPatterns = this.pathPatternsCondition.getMatchingCondition(request);
			if (pathPatterns == null) {
				return null;
			}
		}
		PatternsRequestCondition patterns = null;
		if (this.patternsCondition != null) {
			patterns = this.patternsCondition.getMatchingCondition(request);
			if (patterns == null) {
				return null;
			}
		}
		RequestConditionHolder custom = this.customConditionHolder.getMatchingCondition(request);
		if (custom == null) {
			return null;
		}
		return new RequestMappingInfo(
				this.name, pathPatterns, patterns, methods, params, headers, consumes, produces, custom);
	}
```







## 3. 问题解决

### 3.1  restful  风格优化

网上说是 @PathVariable 影响性能，分析的也很有道理 ，我自己测试却并没有发现 qps 相差的有很多，10%左右。


TestController：
```java
@RestController
public class TestController {

    @GetMapping(value = "/test0/{name}", headers = {"serviceKey=test0"})
    public Response<String> test0(@PathVariable String name) {
        System.out.println("test0 " + name);
        return Response.ok(name);
    }

    @GetMapping(value = "/test1/{name}")
    public Response<String> test1(@PathVariable String name) {
        System.out.println("test1 " + name);
        return Response.ok(name);
    }

    @GetMapping(value = "/test2")
    public Response<String> test2(@RequestParam String name) {
        System.out.println("test2 " + name);
        return Response.ok(name);
    }

    @GetMapping(value = "/restful/{name}",headers = {"service_name=restful"}) //,headers = {"service_name=restful"}
    public String restful(@PathVariable String name) {
        System.out.println("r " + name);
        return name;
    }


    @GetMapping(value = "/non-restful")
    public String non_restful(@RequestParam String name) {
        System.out.println("n " + name);
        return name;
    }


}

```


测试 ：
```
ab -n 10000 -c 200 -T 'application/json;charset=UTF-8' http://127.0.0.1:8080/rest/restful/lisi


ab -n 10000 -c 200  -T 'application/json;charset=UTF-8' http://127.0.0.1:8080/rest/non-restful?lisi


ab -n 10000 -c 200 -H 'service_name:restful' -T 'application/json;charset=UTF-8' http://127.0.0.1:8080/rest/restful/lisi  					
```


做下面的处理解决 ：

1. 服务中注册自己的RequestMappingHandlerMapping 跳过 默认的handlerMapping的多余的处理逻辑 ，以空间换时间，达到O（1）的查找 url对象的处理方法的效果。

```java
public class RestRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    private static final Map<String, HandlerMethod> NAME_HANDLER_MAP = new HashMap<>();
    private static final Map<HandlerMethod, RequestMappingInfo> MAPPING_HANDLER_MAP = new HashMap<>();

    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        HandlerMethod handlerMethod = createHandlerMethod(handler, method);
        RequestMapping requestMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
        assert requestMapping != null;

        String[] headers = requestMapping.headers();
        for (String header : headers) {
            if (header.contains("service_name")){
                NAME_HANDLER_MAP.put(header.split("=")[1], handlerMethod);
            }
        }
        MAPPING_HANDLER_MAP.put(handlerMethod, mapping);
        System.out.println("headers: " + Arrays.toString(requestMapping.headers()) + ", handlerMethod: " + handlerMethod.toString());
        super.registerHandlerMethod(handler, method, mapping);
    }

    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        String service_name = request.getHeader("service_name");
        HandlerMethod handlerMethod = NAME_HANDLER_MAP.get(service_name);
        if (StringUtils.isNotBlank(service_name) && handlerMethod != null) {
            handleMatch(MAPPING_HANDLER_MAP.get(handlerMethod), lookupPath, request);
            return handlerMethod;
        }
        return super.lookupHandlerMethod(lookupPath, request);
    }

}

```

![](https://raw.githubusercontent.com/ZhangDi-d/pic-repo/main/data/20210419161955.png)

![](https://raw.githubusercontent.com/ZhangDi-d/pic-repo/main/data/20210419150632.png)

2. 访问时加上 header 标志 ,如
```java
$ ab -n 10000 -c 200 -H 'service_name:restful' -T 'application/json;charset=UTF-8' http://127.0.0.1:8080/rest/restful/lisi   
```







### 3.2 官方

https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-requestmapping

>@RequestMapping methods can be mapped using URL patterns. There are two alternatives:
>
>PathPattern — a pre-parsed pattern matched against the URL path also pre-parsed as PathContainer. Designed for web use, this solution deals effectively with encoding and path parameters, and matches efficiently.
>
>AntPathMatcher — match String patterns against a String path. This is the original solution also used in Spring configuration to select resources on the classpath, on the filesystem, and other locations. It is less efficient and the String path input is a challenge for dealing effectively with encoding and other issues with URLs.
>
>PathPattern is the recommended solution for web applications and it is the only choice in Spring WebFlux. Prior to version 5.3, AntPathMatcher was the only choice in Spring MVC and continues to be the default. However PathPattern can be enabled in the MVC config.


**spring-mvc:5.3.3**

RequestMappingInfo#RequestMappingInfo:

```java
@Override
	@Nullable
	public RequestMappingInfo getMatchingCondition(HttpServletRequest request) {
		RequestMethodsRequestCondition methods = this.methodsCondition.getMatchingCondition(request);
		if (methods == null) {
			return null;
		}
		ParamsRequestCondition params = this.paramsCondition.getMatchingCondition(request);
		if (params == null) {
			return null;
		}
		HeadersRequestCondition headers = this.headersCondition.getMatchingCondition(request);
		if (headers == null) {
			return null;
		}
		ConsumesRequestCondition consumes = this.consumesCondition.getMatchingCondition(request);
		if (consumes == null) {
			return null;
		}
		ProducesRequestCondition produces = this.producesCondition.getMatchingCondition(request);
		if (produces == null) {
			return null;
		}
		PathPatternsRequestCondition pathPatterns = null;
		if (this.pathPatternsCondition != null) {
			pathPatterns = this.pathPatternsCondition.getMatchingCondition(request);
			if (pathPatterns == null) {
				return null;
			}
		}
		PatternsRequestCondition patterns = null;
		if (this.patternsCondition != null) {
			patterns = this.patternsCondition.getMatchingCondition(request);
			if (patterns == null) {
				return null;
			}
		}
		RequestConditionHolder custom = this.customConditionHolder.getMatchingCondition(request);
		if (custom == null) {
			return null;
		}
		return new RequestMappingInfo(
				this.name, pathPatterns, patterns, methods, params, headers, consumes, produces, custom);
	}
```

**spring-webmvc:5.2.10 (springboot 2.3.5.RELEASE)**

RequestMappingInfo#getMatchingCondition（）
```java
/**
	 * Checks if all conditions in this request mapping info match the provided request and returns
	 * a potentially new request mapping info with conditions tailored to the current request.
	 * <p>For example the returned instance may contain the subset of URL patterns that match to
	 * the current request, sorted with best matching patterns on top.
	 * @return a new instance in case all conditions match; or {@code null} otherwise
	 */
	@Override
	@Nullable
	public RequestMappingInfo getMatchingCondition(HttpServletRequest request) {
		RequestMethodsRequestCondition methods = this.methodsCondition.getMatchingCondition(request);
		if (methods == null) {
			return null;
		}
		ParamsRequestCondition params = this.paramsCondition.getMatchingCondition(request);
		if (params == null) {
			return null;
		}
		HeadersRequestCondition headers = this.headersCondition.getMatchingCondition(request);
		if (headers == null) {
			return null;
		}
		ConsumesRequestCondition consumes = this.consumesCondition.getMatchingCondition(request);
		if (consumes == null) {
			return null;
		}
		ProducesRequestCondition produces = this.producesCondition.getMatchingCondition(request);
		if (produces == null) {
			return null;
		}
		PatternsRequestCondition patterns = this.patternsCondition.getMatchingCondition(request);
		if (patterns == null) {
			return null;
		}
		RequestConditionHolder custom = this.customConditionHolder.getMatchingCondition(request);
		if (custom == null) {
			return null;
		}

		return new RequestMappingInfo(this.name, patterns,
				methods, params, headers, consumes, produces, custom.getCondition());
	}
```


使用 PathPatternParse ：
```java

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setPatternParser(new PathPatternParser());
    }
}
```




### 3.3 测试数据





原始的比较：

|no.|restful qps|non-restful qps|
|:----|:---|:---|
|1|2970.17|3306.68|
|2|3017.07|3362.15|
|3|2959.32|3247.03|
|4|2948.59|3260.52|
|5|3006.16|3311.74|
|mean|2980.262|3297.624|

两者差距在10.7%  。





使用 RestfulRequestMappingHandlerMapping：
|no.|before restful qps|after restful qps|
|:----|:---|:---|
|1|3350.19|3263.34|
|2|3279.53|3159.88|
|3|3237.00|3160.04|
|4|3286.23|3179.22|
|5|3247.94|3172.68|

两者基本持平了。


使用 PathPatternParser：
|no.|restful qps|non-restful qps|
|:----|:---|:---|
|1|3092.92|3410.77|
|2|3264.08|3432.94|
|3|3232.33|3426.81|
|4|3347.82|3431.35|
|5|3267.00|3357.25|
|mean|3240.83|	3411.824|


两者差距在5.3%  。 PathPatternParser 能提升接口响应速度。





















































### more
http://www.360doc.com/content/17/0913/09/412471_686699410.shtml
https://www.yuque.com/abosen/blogs/bfzss2
https://www.cnblogs.com/fangjian0423/p/springMVC-dispatcherServlet.html
https://www.cnblogs.com/heavenyes/p/3905844.html
https://www.cnblogs.com/54chensongxia/p/12525418.html
https://zhuanlan.zhihu.com/p/355446586
https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-requestmapping(官方文档)
https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html#common-application-properties-web
https://github.com/spring-projects/spring-framework/issues/24945
https://codeburst.io/improve-performance-of-java-microservices-with-a-few-actionable-tips-dcacbfb061dc
https://developer.aliyun.com/article/184163
https://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html
https://medium.com/rpdstartup/rest-api-performance-tuning-getting-started-7a6efefa9e20