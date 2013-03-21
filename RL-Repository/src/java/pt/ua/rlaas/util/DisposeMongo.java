package pt.ua.rlaas.util;

import pt.ua.rlaas.data.mongodb.MongoPersistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Disposes the mongodb client.
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class DisposeMongo implements ServletContextListener {

    ServletContext context;

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        context = contextEvent.getServletContext();
        System.out.println("Context destroyed: disposing of mongodb client.");
        MongoPersistence.instance().dispose();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        context = sce.getServletContext();
    }
}
