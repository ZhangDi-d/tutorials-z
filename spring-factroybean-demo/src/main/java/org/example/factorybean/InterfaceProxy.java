package org.example.factorybean;


import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author dizhang
 * @date 2021-04-16
 * jdk 动态代理
 */
public class InterfaceProxy  { //implements InvocationHandler
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println("ObjectProxy execute:" + method.getName());
//        return method.invoke(proxy, args);
//    }

    /**
     * classLoader –定义代理类的类加载器
     * interfaces –代理类要实现的接口列表
     * proxy –用来处理接口方法调用的InvocationHandler实例
     */
    public static <T> T newInstance(Class<T> innerInterface) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ClassLoader classLoader = innerInterface.getClassLoader();
        Class[] interfaces = new Class[]{innerInterface};
        List<Class> classList = getAllInterfaceAchieveClass(innerInterface);
        if (CollectionUtils.isEmpty(classList)){
            throw new RuntimeException("no sub class");
        }
        Class aClass = classList.get(0);
        InterfaceInvocationHandler proxy = new InterfaceInvocationHandler(aClass.getDeclaredConstructor().newInstance());
        return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
    }

    /**
     * 获取所有接口的实现类
     * @return
     */
    public static List<Class> getAllInterfaceAchieveClass(Class clazz){
        ArrayList<Class> list = new ArrayList<>();
        //判断是否是接口
        if (clazz.isInterface()) {
            try {
                ArrayList<Class> allClass = getAllClassByPath(clazz.getPackage().getName());
                /**
                 * 循环判断路径下的所有类是否实现了指定的接口
                 * 并且排除接口类自己
                 */
                for (int i = 0; i < allClass.size(); i++) {

                    //排除抽象类
                    if(Modifier.isAbstract(allClass.get(i).getModifiers())){
                        continue;
                    }
                    //判断是不是同一个接口
                    if (clazz.isAssignableFrom(allClass.get(i))) {
                        if (!clazz.equals(allClass.get(i))) {
                            list.add(allClass.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("出现异常");
            }
        }
        return list;
    }


    /**
     * 从指定路径下获取所有类
     * @return
     */
    public static ArrayList<Class> getAllClassByPath(String packagename){
        ArrayList<Class> list = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packagename.replace('.', '/');
        try {
            ArrayList<File> fileList = new ArrayList<>();
            Enumeration<URL> enumeration = classLoader.getResources(path);
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                fileList.add(new File(url.getFile()));
            }
            for (int i = 0; i < fileList.size(); i++) {
                list.addAll(findClass(fileList.get(i),packagename));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 如果file是文件夹，则递归调用findClass方法，或者文件夹下的类
     * 如果file本身是类文件，则加入list中进行保存，并返回
     * @param file
     * @param packagename
     * @return
     */
    private static ArrayList<Class> findClass(File file,String packagename) {
        ArrayList<Class> list = new ArrayList<>();
        if (!file.exists()) {
            return list;
        }
        File[] files = file.listFiles();
        for (File file2 : files) {
            if (file2.isDirectory()) {
                assert !file2.getName().contains(".");//添加断言用于判断
                ArrayList<Class> arrayList = findClass(file2, packagename+"."+file2.getName());
                list.addAll(arrayList);
            }else if(file2.getName().endsWith(".class")){
                try {
                    //保存的类文件不需要后缀.class
                    list.add(Class.forName(packagename + '.' + file2.getName().substring(0,
                            file2.getName().length()-6)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}


