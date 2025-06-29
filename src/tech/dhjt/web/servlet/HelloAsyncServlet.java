package tech.dhjt.web.servlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/asyncHello", asyncSupported = true)
public class HelloAsyncServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1、支持异步处理asyncSupported=true
        // 2、开启异步模式
        System.out.println("主线程开始。。。" + Thread.currentThread() + "==>" + System.currentTimeMillis());
        System.out.println("异步模式状态：" + req.isAsyncStarted());
        AsyncContext asyncContext = req.startAsync();
        System.out.println("异步模式状态：" + req.isAsyncStarted());

        // 3、业务逻辑进行异步处理;开始异步处理
        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("副线程开始。。。" + Thread.currentThread() + "==>" + System.currentTimeMillis());
                    sayHello();
                    // 4、获取响应
                    ServletResponse response = asyncContext.getResponse();
                    response.getWriter().write("hello async...");
                    System.out.println("副线程结束。。。" + Thread.currentThread() + "==>" + System.currentTimeMillis());
                    asyncContext.complete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("主线程结束。。。" + Thread.currentThread() + "==>" + System.currentTimeMillis());
    }

    public void sayHello() throws Exception {
        System.out.println(Thread.currentThread() + " processing...");
        Thread.sleep(3000);
    }
}
