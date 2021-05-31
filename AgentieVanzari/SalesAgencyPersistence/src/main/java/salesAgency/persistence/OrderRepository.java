package salesAgency.persistence;

import org.hibernate.query.Query;
import salesAgency.model.Order;
import salesAgency.model.OrderStatus;
import salesAgency.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;


public class OrderRepository {
    private final SessionFactory sessionFactory;

    public OrderRepository(HibernateUtil hibernateUtil) {
        sessionFactory = hibernateUtil.getSessionFactory();
    }

    public Order save(Order order) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(order);
                transaction.commit();
                return order;
            } catch (Exception exception) {
                if (transaction != null) transaction.rollback();
            }
        }
        return null;
    }

    public List<Order> getAllBySalesAgent(int agentId) {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                List<Order> orders = session.createQuery("FROM Order WHERE salesAgentId = :agentId", Order.class)
                        .setParameter("agentId", agentId)
                        .list();
                transaction.commit();
                return orders;
            } catch (Exception exception) {
                if (transaction != null) transaction.rollback();
            }

            return null;
        }
    }

    public List<Order> getAllWithoutSalesAgent(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                List<Order> orders = session.createQuery("FROM Order WHERE salesAgentId <> :agentId", Order.class)
                        .setParameter("agentId", id)
                        .list();
                transaction.commit();
                return orders;
            } catch (Exception exception) {
                if (transaction != null) transaction.rollback();
            }

            return null;
        }
    }

    public void removeOrder(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.createQuery("DELETE FROM Order WHERE id = :id")
                        .setParameter("id", id)
                        .executeUpdate();
                transaction.commit();
            } catch (Exception exception) {
                if (transaction != null) transaction.rollback();
            }
        }
    }

    public void updateStatus(Integer id) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Query q = session.createQuery("UPDATE Order SET status = :status WHERE id = :id");

                q.setParameter("status", OrderStatus.COMPLETED);
                q.setParameter("id", id);
                q.executeUpdate();

                transaction.commit();
            }
            catch (Exception exception) {
                if (transaction != null) transaction.rollback();
            }
        }
    }
}
