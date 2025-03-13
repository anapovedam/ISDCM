import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        String propertiesPath = context.getInitParameter("messages");

        Properties properties = new Properties();
        try (InputStream input = context.getResourceAsStream(propertiesPath)) {
            properties.load(input);
            context.setAttribute("messages", properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // No es necesario limpiar nada aquí
    }
}
