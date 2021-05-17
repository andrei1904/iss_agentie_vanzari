package repository;

import model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ProductRepository {
    private final SessionFactory sessionFactory;

    public ProductRepository(HibernateUtil hibernateUtil) {
        sessionFactory = hibernateUtil.getSessionFactory();
    }

    public List<Product> getAll() {

        try (Session session = sessionFactory.openSession()){
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                List<Product> products = session.createQuery("FROM Product", Product.class).list();
                transaction.commit();
                return products;
            }
            catch (Exception exception) {
                if (transaction != null) transaction.rollback();
            }

            return null;
        }
    }
}
