package salesAgency.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import salesAgency.client.gui.LoginController;
import salesAgency.client.gui.MainController;
import salesAgency.services.ISalesAgencyServices;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProperties = new Properties();

        try {
            clientProperties.load(StartClient.class.getResourceAsStream("/salesagencyclient.properties"));
            System.out.println("Client properties set. ");
            clientProperties.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find salesagencylient.properties " + e);
            return;
        }

        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:salesagencyclient.xml");
            ISalesAgencyServices server=(ISalesAgencyServices) factory.getBean("salesAgencyService");
            System.out.println("Obtained a reference to remote server");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/loginView.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.set(server, primaryStage);

            FXMLLoader mainLoader = new FXMLLoader(
                    getClass().getResource("/views/mainView.fxml"));
            Parent mroot = mainLoader.load();

            MainController mainController = mainLoader.getController();
            mainController.set(server);
            loginController.setParent(mroot);
            loginController.setMainController(mainController);


            primaryStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("SalesAgency");
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("SalesAgency Initialization  exception: " + e);
            e.printStackTrace();
        }
    }
}
