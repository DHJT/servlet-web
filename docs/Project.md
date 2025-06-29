# Servlet
1. Servlet容器启动会扫描，当前应用里面每一个jar包的`ServletContainerInitializer`的实现
2. 提供`ServletContainerInitializer`的实现类；

    必须绑定在，`META-INF/services/javax.servlet.ServletContainerInitializer`
    文件的内容就是`ServletContainerInitializer`实现类的全类名；

总结：容器在启动应用的时候，会扫描当前应用每一个jar包里面的文件：`META-INF/services/javax.servlet.ServletContainerInitializer`
