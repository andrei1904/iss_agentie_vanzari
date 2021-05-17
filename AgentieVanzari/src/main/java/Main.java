import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.AllService;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Login");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginView.fxml"));
        Pane myPane = loader.load();

        LoginController ctrl = loader.getController();
        ctrl.set(getAllService(), primaryStage);

        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);

        primaryStage.show();
    }

    static AllService getAllService() {
        ApplicationContext context = new ClassPathXmlApplicationContext("SalesAgencyApp.xml");
        return context.getBean(AllService.class);
    }
}
