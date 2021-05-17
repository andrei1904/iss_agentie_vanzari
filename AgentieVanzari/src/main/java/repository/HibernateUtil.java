package repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class HibernateUtil {
    private SessionFactory sessionFactory;

    public HibernateUtil() {
        try {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception ex) {
//            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
