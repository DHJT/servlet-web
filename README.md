# Servlet 项目
使用Servlet进行web项目开发。

项目使用Maven进行依赖管理，使用嵌入式tomcat进行启动。

### 使用
首页：http://<ip>:<port>

### 构建
```sh
mvn clean package
.mvnw clean package -DskipTests
```

### web.xml 配置说明

`metadata-complete` 是 web.xml 中的一个重要属性，它决定了容器如何处理 Servlet 3.0+ 的注解和自动发现机制。

核心作用

当设置为 true 时：

- 禁用注解扫描：Tomcat 将不会扫描类文件中的 Servlet 相关注解（如 @WebServlet, @WebFilter, @WebListener 等）
- 禁用自动发现：容器不会自动发现 ServletContainerInitializer 实现
- 仅使用传统配置：完全依赖 web.xml 中声明的配置

```xml
<web-app metadata-complete="false"> <!-- 允许使用注解和自动发现 -->
</web-app>
```
