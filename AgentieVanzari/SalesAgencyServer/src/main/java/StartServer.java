import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import salesAgency.persistence.HibernateUtil;

public class StartServer {
    public static void main(String[] args) {
        ApplicationContext factory = new ClassPathXmlApplicationContext(
                "classpath:salesagencyserver.xml");
    }
}
