> source:https://lipanpan.github.io/2017/11/19/Spring%20%E6%89%A9%E5%B1%95%E7%82%B9%E4%B9%8BFactoryBean/





FactoryBean:
```java


public interface FactoryBean<T> {
    /**
     * 可以在org.springframework.beans.factory.config.BeanDefinition上set的属性的名称，以便在无法从工厂bean类推导出时，工厂bean可以发出信号通知其对象类型。
     * 自从：5.2
     */
	String OBJECT_TYPE_ATTRIBUTE = "factoryBeanObjectType";

    /**
     * 返回此工厂管理的对象的实例（可能是共享的或独立的）。
     * 与BeanFactory ，这允许同时支持Singleton和Prototype设计模式。
     */
	@Nullable
	T getObject() throws Exception;

    /**
     * 返回此FactoryBean创建的对象的类型；如果事先未知，则返回null 。
     * 这样就可以在不实例化对象的情况下（例如在自动装配时）检查特定类型的Bean。
     * 对于正在创建单例对象的实现，此方法应尽量避免创建单例。 它应该提前估计类型。 对于原型，建议在此处返回有意义的类型。
     * 可以在完全初始化此FactoryBean之前调用此方法。 它一定不能依赖初始化期间创建的状态。 当然，如果可用，它仍然可以使用这种状态。
     */
	@Nullable
	Class<?> getObjectType();

    /**
     * 该工厂管理的对象是单例吗？ 也就是说， getObject()总是返回相同的对象（可以缓存的引用）?
     */
	default boolean isSingleton() {
		return true;
	}

}
```


核心类：AbstractBeanFactory 继承自 FactoryBeanRegistrySupport。当从IOC容器中调用getBean方法时，底层会调用AbstractBeanFactory的getObjectForBeanInstance方法，
如果当前BeanDefinition关联的beanClass为FactoryBean类型则调用其getObject方法，如果是普通类型则直接返回该实例。

TestMain#testClass()
```
Teacher teacherProxyBean = context.getBean(Teacher.class);

```


DefaultListableBeanFactory#getBean()

```java

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
		implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {
	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		return getBean(requiredType, (Object[]) null);
	}
	
  //omit	
  }
```

AbstractBeanFactory#doGetBean()
```java
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

protected <T> T doGetBean(
			String name, @Nullable Class<T> requiredType, @Nullable Object[] args, boolean typeCheckOnly)
			throws BeansException {

		String beanName = transformedBeanName(name);
		Object beanInstance;

		// Eagerly check singleton cache for manually registered singletons.
		Object sharedInstance = getSingleton(beanName);
		if (sharedInstance != null && args == null) {
			if (logger.isTraceEnabled()) {
				if (isSingletonCurrentlyInCreation(beanName)) {
					logger.trace("Returning eagerly cached instance of singleton bean '" + beanName +
							"' that is not fully initialized yet - a consequence of a circular reference");
				}
				else {
					logger.trace("Returning cached instance of singleton bean '" + beanName + "'");
				}
			}
			beanInstance = getObjectForBeanInstance(sharedInstance, name, beanName, null);
		}
		
		//omit
		
		}
```


AbstractBeanFactory#getObjectForBeanInstance()
```java
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    protected Object getObjectForBeanInstance(
            Object beanInstance, String name, String beanName, @Nullable RootBeanDefinition mbd) {

        // Don't let calling code try to dereference the factory if the bean isn't a factory.
        if (BeanFactoryUtils.isFactoryDereference(name)) {
            if (beanInstance instanceof NullBean) {
                return beanInstance;
            }
            if (!(beanInstance instanceof FactoryBean)) {
                throw new BeanIsNotAFactoryException(beanName, beanInstance.getClass());
            }
            if (mbd != null) {
                mbd.isFactoryBean = true;
            }
            return beanInstance;
        }

        // Now we have the bean instance, which may be a normal bean or a FactoryBean.
        // If it's a FactoryBean, we use it to create a bean instance, unless the
        // caller actually wants a reference to the factory.
        //关键之处：如果是普通的bean则直接返回，如果是FactoryBean则继续向下走
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }

        Object object = null;
        if (mbd != null) {
            mbd.isFactoryBean = true;
        } else {
            object = getCachedObjectForFactoryBean(beanName);
        }
        if (object == null) {
            // Return bean instance from factory.
            FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
            // Caches object obtained from FactoryBean if it is a singleton.
            if (mbd == null && containsBeanDefinition(beanName)) {
                mbd = getMergedLocalBeanDefinition(beanName);
            }
            boolean synthetic = (mbd != null && mbd.isSynthetic());
            object = getObjectFromFactoryBean(factory, beanName, !synthetic);
        }
        return object;
    }
    //omit
}
```

FactoryBeanRegistrySupport#getObjectFromFactoryBean()
```java
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess) {
		if (factory.isSingleton() && containsSingleton(beanName)) {
			synchronized (getSingletonMutex()) {
				Object object = this.factoryBeanObjectCache.get(beanName);
				if (object == null) {
					object = doGetObjectFromFactoryBean(factory, beanName);
					// Only post-process and store if not put there already during getObject() call above
					// (e.g. because of circular reference processing triggered by custom getBean calls)
					Object alreadyThere = this.factoryBeanObjectCache.get(beanName);
					if (alreadyThere != null) {
						object = alreadyThere;
					}
					else {
						if (shouldPostProcess) {
							if (isSingletonCurrentlyInCreation(beanName)) {
								// Temporarily return non-post-processed object, not storing it yet..
								return object;
							}
							beforeSingletonCreation(beanName);
							try {
								object = postProcessObjectFromFactoryBean(object, beanName);
							}
							catch (Throwable ex) {
								throw new BeanCreationException(beanName,
										"Post-processing of FactoryBean's singleton object failed", ex);
							}
							finally {
								afterSingletonCreation(beanName);
							}
						}
						if (containsSingleton(beanName)) {
							this.factoryBeanObjectCache.put(beanName, object);
						}
					}
				}
				return object;
			}
		}
		else {
			Object object = doGetObjectFromFactoryBean(factory, beanName);
			if (shouldPostProcess) {
				try {
					object = postProcessObjectFromFactoryBean(object, beanName);
				}
				catch (Throwable ex) {
					throw new BeanCreationException(beanName, "Post-processing of FactoryBean's object failed", ex);
				}
			}
			return object;
		}
	}
	//omit
}
```

FactoryBeanRegistrySupport#doGetObjectFromFactoryBean()
```java
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {
    private Object doGetObjectFromFactoryBean(FactoryBean<?> factory, String beanName) throws BeanCreationException {
        Object object;
        try {
            if (System.getSecurityManager() != null) {
                AccessControlContext acc = getAccessControlContext();
                try {
                    // 调用factory的getObject()方法生成动态代理类
                    object = AccessController.doPrivileged((PrivilegedExceptionAction<Object>) factory::getObject, acc);
                }
                catch (PrivilegedActionException pae) {
                    throw pae.getException();
                }
            }
            else {
                // 调用factory的getObject()方法生成动态代理类
                object = factory.getObject();
            }
        }
        catch (FactoryBeanNotInitializedException ex) {
            throw new BeanCurrentlyInCreationException(beanName, ex.toString());
        }
        catch (Throwable ex) {
            throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
        }

        // Do not accept a null value for a FactoryBean that's not fully
        // initialized yet: Many FactoryBeans just return null then.
        if (object == null) {
            if (isSingletonCurrentlyInCreation(beanName)) {
                throw new BeanCurrentlyInCreationException(
                        beanName, "FactoryBean which is currently in creation returned null from getObject");
            }
            object = new NullBean();
        }
        return object;
    }	
}
```
























### JDK 动态代理 

newProxyInstance()方法帮我们执行了生成代理类----获取构造器----生成代理对象这三步:

生成代理类: Class<?> cl = getProxyClass0(loader, intfs);

获取构造器: final Constructor<?> cons = cl.getConstructor(constructorParams);

生成代理对象: cons.newInstance(new Object[]{h});

```java
public class Proxy implements java.io.Serializable {

    @CallerSensitive
    public static Object newProxyInstance(ClassLoader loader,
                                          Class<?>[] interfaces,
                                          InvocationHandler h)
            throws IllegalArgumentException {
        // 如果h为空直接抛出空指针异常，之后所有的单纯的判断null并抛异常，都是此方法
        Objects.requireNonNull(h);
        // 拷贝类实现的所有接口
        final Class<?>[] intfs = interfaces.clone();
        // 获取当前系统安全接口
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            // Reflection.getCallerClass返回调用该方法的方法的调用类;loader：接口的类加载器
            // 进行包访问权限、类加载器权限等检查
            checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
        }

        /*
         * Look up or generate the designated proxy class.
         * 译: 查找或生成指定的代理类
         */
        Class<?> cl = getProxyClass0(loader, intfs);

        /*
         * Invoke its constructor with the designated invocation handler.
         * 译: 用指定的调用处理程序调用它的构造函数。
         */
        try {
            if (sm != null) {
                checkNewProxyPermission(Reflection.getCallerClass(), cl);
            }
            /*
             * 获取代理类的构造函数对象。
             * constructorParams是类常量，作为代理类构造函数的参数类型，常量定义如下:
             * private static final Class<?>[] constructorParams = { InvocationHandler.class };
             */
            final Constructor<?> cons = cl.getConstructor(constructorParams);
            final InvocationHandler ih = h;
            if (!Modifier.isPublic(cl.getModifiers())) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    public Void run() {
                        cons.setAccessible(true);
                        return null;
                    }
                });
            }
            // 根据代理类的构造函数对象来创建需要返回的代理类对象
            return cons.newInstance(new Object[]{h});
        } catch (IllegalAccessException | InstantiationException e) {
            throw new InternalError(e.toString(), e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new InternalError(t.toString(), t);
            }
        } catch (NoSuchMethodException e) {
            throw new InternalError(e.toString(), e);
        }
    }


}
```











































### cglib 动态代理
创建代理对象
```
(T) enhancer.create();
```

源码

```java
public class Enhancer extends AbstractClassGenerator {
    public Object create() {
        classOnly = false;
        argumentTypes = null;
        return createHelper();
    }

    private Object createHelper() {
        preValidate();
        Object key = KEY_FACTORY.newInstance((superclass != null) ? superclass.getName() : null,
                ReflectUtils.getNames(interfaces),
                filter == ALL_ZERO ? null : new WeakCacheKey<CallbackFilter>(filter),
                callbackTypes,
                useFactory,
                interceptDuringConstruction,
                serialVersionUID);
        this.currentKey = key;
        Object result = super.create(key);
        return result;
    }
    //omit
}
```


```java
abstract public class AbstractClassGenerator<T> implements ClassGenerator {
    protected Object create(Object key) {
        try {
            ClassLoader loader = getClassLoader();
            Map<ClassLoader, ClassLoaderData> cache = CACHE;
            ClassLoaderData data = cache.get(loader);
            if (data == null) {
                synchronized (AbstractClassGenerator.class) {
                    cache = CACHE;
                    data = cache.get(loader);
                    if (data == null) {
                        Map<ClassLoader, ClassLoaderData> newCache = new WeakHashMap<ClassLoader, ClassLoaderData>(cache);
                        data = new ClassLoaderData(loader);
                        newCache.put(loader, data);
                        CACHE = newCache;
                    }
                }
            }
            this.key = key;
            Object obj = data.get(this, getUseCache());
            if (obj instanceof Class) {
                return firstInstance((Class) obj);
            }
            
            // ** 在子类 Enhancer中实现 
            return nextInstance(obj);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CodeGenerationException(ex);
        }
    }
    //omit
}
```

```java

public class Enhancer extends AbstractClassGenerator {
    
    
    protected Object nextInstance(Object instance) {
        EnhancerFactoryData data = (EnhancerFactoryData) instance;

        if (classOnly) {
            return data.generatedClass;
        }

        Class[] argumentTypes = this.argumentTypes;
        Object[] arguments = this.arguments;
        if (argumentTypes == null) {
            argumentTypes = Constants.EMPTY_CLASS_ARRAY;
            arguments = null;
        }
        return data.newInstance(argumentTypes, arguments, callbacks);
    }

    //omit
}
```

最后的生成方法：
```java
public class Enhancer extends AbstractClassGenerator {


    /**
     *
     * @param argumentTypes 构造函数参数类型
     * @param arguments 构造函数参数
     * @param callbacks 为新实例设置的回调
     * @return
     */
    public Object newInstance(Class[] argumentTypes, Object[] arguments, Callback[] callbacks) {
        setThreadCallbacks(callbacks);
        try {
            // Explicit reference equality is added here just in case Arrays.equals does not have one
            if (primaryConstructorArgTypes == argumentTypes ||
                    Arrays.equals(primaryConstructorArgTypes, argumentTypes)) {
                // If we have relevant Constructor instance at hand, just call it
                // This skips "get constructors" machinery
                return ReflectUtils.newInstance(primaryConstructor, arguments);
            }
            // Take a slow path if observing unexpected argument types
            return ReflectUtils.newInstance(generatedClass, argumentTypes, arguments);
        } finally {
            // clear thread callbacks to allow them to be gc'd
            setThreadCallbacks(null);
        }

    }

//omit
}
```

### more 
https://segmentfault.com/a/1190000012262244
https://segmentfault.com/a/1190000019733546
https://blog.csdn.net/yhl_jxy/article/details/80633194(cglib)
https://jpeony.blog.csdn.net/article/details/80586785(jdk)
https://www.cnblogs.com/wyq178/p/11514343.html
https://blog.csdn.net/kingtok/article/details/113987328









