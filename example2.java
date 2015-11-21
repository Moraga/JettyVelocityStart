mport org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.view.VelocityViewServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

public class Example2 {
    public static void main (String[] args) throws Exception {
        Server server = new Server(8001);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        // using velocity view servlet
        context.addServlet(new ServletHolder(new MyServlet()), "/*");
        server.start();
        server.join();
    }

    public static class MyServlet extends VelocityViewServlet {
        public void init(ServletConfig config) throws ServletException {
            super.init(config);
            // settings velocity properties
            Properties p = new Properties();
            p.setProperty("file.resource.loader.path", "/path/to/templates");
            Velocity.init(p);
        }

        protected Template getTemplate(HttpServletRequest request, HttpServletResponse response) {
            // get template by url
            String path = request.getPathInfo().substring(1);
            return Velocity.getTemplate(path);
        }
    }
}
