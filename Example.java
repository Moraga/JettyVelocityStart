import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

public class Example {
    static VelocityEngine ve;
    static Context context;

    public static void main(String[] args) throws Exception {
        // velocity
        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", "/path/to/template");
        ve = new VelocityEngine(p);
        ToolManager manager = new ToolManager();
        context = manager.createContext();
        // server
        Server server = new Server(8001);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(Serv.class, "/*");
        server.start();
        server.join();
    }

    public static class Serv extends HttpServlet {
        protected void doGet(
                HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {
            String path = request.getPathInfo().substring(1);
            Template template = ve.getTemplate(path);
            StringWriter w = new StringWriter();
            template.merge(context, w);
            response.setContentType("text/html; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(w);
        }
    }
}
