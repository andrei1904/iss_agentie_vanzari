package salesAgency.persistence;

import salesAgency.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;

public class ProductRepository implements Serializable {
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

    public Product getOneById(Integer id) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Product product = session.createQuery("FROM Product WHERE id = :id", Product.class)
                        .setParameter("id", id)
                        .getSingleResult();
                transaction.commit();
                return product;
            }
            catch (Exception exception) {
                if (transaction != null) transaction.rollback();
            }

            return null;
        }
    }

    public void updateQuantity(int productId, int quantity) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Query q = session.createQuery("UPDATE Product SET quantity = :quantity WHERE id = :id");

                q.setParameter("quantity", quantity);
                q.setParameter("id", productId);
                q.executeUpdate();

                transaction.commit();
            }
            catch (Exception exception) {
                if (transaction != null) transaction.rollback();
            }
        }
    }

}
