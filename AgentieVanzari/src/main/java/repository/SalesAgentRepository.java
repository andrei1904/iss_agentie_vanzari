package repository;

import model.SalesAgent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class SalesAgentRepository {
    private final SessionFactory sessionFactory;

    public SalesAgentRepository(HibernateUtil hibernateUtil) {
        this.sessionFactory = hibernateUtil.getSessionFactory();
    }

    public SalesAgent findOne(String username) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                SalesAgent salesAgent = session.createQuery("FROM SalesAgent WHERE " +
                        "username = :username", SalesAgent.class)
                        .setParameter("username", username)
                        .getSingleResult();
                transaction.commit();
                return salesAgent;
            }
            catch (Exception exception) {
                if (transaction != null) transaction.rollback();
            }

            return null;
        }
    }
}
