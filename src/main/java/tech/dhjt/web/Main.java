package tech.dhjt.web;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.LogManager;

import javax.servlet.ServletContext;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.JarScanType;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * 使用内置Tomcat的主启动类
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        // 初始化日志系统
        initLogging();

        logger.info("Starting embedded Tomcat server...");
        // 1. 创建嵌入式Tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        // 2. 设置webapp目录，获取webapp资源路径（适配JAR包内和开发环境）
        URL webappUrl = Main.class.getClassLoader().getResource("src/main/webapp");
        String webappDir;
        if (webappUrl != null && webappUrl.getProtocol().equals("jar")) {
            // JAR包内运行
            webappDir = new File(webappUrl.getFile()).getParent();
        } else {
            // 开发环境运行
            webappDir = new File("src/main/webapp").getAbsolutePath();
        }
        logger.info("Using webapp directory: " + webappDir);
        Context ctx = tomcat.addWebapp("", webappDir);
        // 创建上下文（不需要web.xml）
        //        Context ctx = tomcat.addContext("", null);
        //        ctx.addServletContainerInitializer(new JasperInitializer(), null);
        //        WebResourceRoot resources = new StandardRoot(ctx);
        //        resources.addPreResources(
        //                new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
        //        ctx.setResources(resources);
        // 确保Tomcat能发现ServletContainerInitializer
        //        ctx.addServletContainerInitializer(new MyServletContainerInitializer(), null);

        // 3. 禁用Tomcat的默认JSP解析（如果需要JSP支持则保留）
        ctx.setJarScanner(new EmptyJarScanner());
        ctx.addWelcomeFile("index.jsp");
        ctx.addWelcomeFile("index.html");

        // 7. 启动Tomcat
        tomcat.start();
        logger.info("Tomcat started on port(s): 8080 (http)");
        // 在Main类中添加更多日志
        logger.info("Tomcat base directory: " + tomcat.getServer().getCatalinaBase().getAbsolutePath());
        //        logger.info("Tomcat config file: " + tomcat.getConnector().getPort());
        logger.info("Tomcat config file: " + tomcat.getConnector().getLocalPort());
        logger.info("Tomcat connectors: " + Arrays.toString(tomcat.getService().findConnectors()));

        // 需要调用一次，否则tomcat.getServer()方法中会因为server为null导致端口是指为-1
        tomcat.getConnector();
        tomcat.getServer().await();
    }

    private static void initLogging() {
        // 移除现有的JUL handlers
        LogManager.getLogManager().reset();

        // 安装SLF4J的JUL桥接
        SLF4JBridgeHandler.install();

        // 可选：设置Tomcat内部日志级别 WARN
        System.setProperty("org.apache.catalina.level", "INFO");
        System.setProperty("org.apache.coyote.level", "INFO");
    }

    // 空Jar扫描器，禁用Tomcat默认的JSP扫描
    public static class EmptyJarScanner extends StandardJarScanner {

        @Override
        public void scan(JarScanType scanType, ServletContext context, JarScannerCallback callback) {
            //            // 不做任何扫描
            // TODO Auto-generated method stub
            //            super.scan(scanType, context, callback);
        }

    }

}
