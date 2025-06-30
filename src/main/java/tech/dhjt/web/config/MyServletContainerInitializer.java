package tech.dhjt.web.config;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.annotation.HandlesTypes;

import tech.dhjt.web.filter.UserFilter;
import tech.dhjt.web.listener.UserListener;
import tech.dhjt.web.service.HelloService;
import tech.dhjt.web.servlet.HelloAsyncServlet;
import tech.dhjt.web.servlet.HelloServlet;
import tech.dhjt.web.servlet.UserServlet;

//容器启动的时候会将@HandlesTypes指定的这个类型下面的子类（实现类，子接口等）传递过来；
// 可指定需要处理的类（如自定义注解）
@HandlesTypes(value = {HelloService.class})
public class MyServletContainerInitializer implements ServletContainerInitializer {

    /**
     * 应用启动的时候，会运行onStartup方法；
     *
     * Set<Class<?>> arg0：感兴趣的类型的所有子类型； ServletContext
     * arg1:代表当前Web应用的ServletContext；一个Web应用一个ServletContext；
     *
     * 1）、使用ServletContext注册Web组件（Servlet、Filter、Listener）
     * 2）、使用编码的方式，在项目启动的时候给ServletContext里面添加组件； 必须在项目启动的时候来添加；
     * 1）、ServletContainerInitializer得到的ServletContext；
     * 2）、ServletContextListener得到的ServletContext；
     */
    @Override
    public void onStartup(Set<Class<?>> arg0, ServletContext sc) throws ServletException {
        System.out.println("感兴趣的类型：");
        if (arg0 != null) {
            for (Class<?> claz : arg0) {
                System.out.println(claz);
            }
        }

        // 注册组件 ServletRegistration
        ServletRegistration.Dynamic servlet = sc.addServlet("userServlet", new UserServlet());
        // 配置servlet的映射信息
        servlet.addMapping("/user");
        sc.addServlet("helloServlet", new HelloServlet()).addMapping("/hello");
        Dynamic helloAsyncServlet = sc.addServlet("helloAsyncServlet", new HelloAsyncServlet());
        helloAsyncServlet.setAsyncSupported(true);
        helloAsyncServlet.addMapping("/asyncHello");

        // 注册Listener
        sc.addListener(UserListener.class);

        // 注册Filter FilterRegistration
        FilterRegistration.Dynamic filter = sc.addFilter("userFilter", UserFilter.class);

        // 配置Filter的映射信息
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    }

}
