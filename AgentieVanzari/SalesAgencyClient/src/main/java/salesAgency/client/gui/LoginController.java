package salesAgency.client.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import salesAgency.model.SalesAgent;
import salesAgency.services.ISalesAgencyServices;
import salesAgency.services.SalesAgencyException;


public class LoginController {
    @FXML
    public TextField textFieldUsername;
    @FXML
    public TextField textFieldPassword;

    private ISalesAgencyServices server;
    private Stage stage;
    private MainController mainController;
    private Parent parent;
    private SalesAgent salesAgent;
    private int ok = 0;

    public void set(ISalesAgencyServices server, Stage stage) {
        this.server = server;
        this.stage = stage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void handleLogin() {
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();

        if (username.equals("") || password.equals("")) {
            MessageAlert.showErrorMessage(null, "Enter username and password!");
            return;
        }

        try {
            salesAgent = server.loginSalesAgent(username, password, mainController);

            if (salesAgent != null) {
                if (ok == 0) {
                    showMainWindow(salesAgent);
                    ok = 1;
                } else {
                    showMainWindowAgain(salesAgent);
                }
                hide();
            } else {
                MessageAlert.showErrorMessage(null, "Wrong user!");
            }
        } catch (SalesAgencyException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

    private void showMainWindowAgain(SalesAgent salesAgent) {
        try {
            Stage salesAgentStage = new Stage();
            salesAgentStage.setTitle("Agent:" + salesAgent.getName());

            salesAgentStage.setScene(parent.getScene());

            salesAgentStage.initModality(Modality.WINDOW_MODAL);
            salesAgentStage.setOnHiding(event -> handleLogout());

            mainController.setAgent(salesAgent);
            mainController.initData();

            salesAgentStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleLogout() {
        try {
            server.logoutSalesAgent(salesAgent, mainController);
            show();
        } catch (SalesAgencyException ex) {
            System.out.println("Logout error" + ex);
        }
    }

    private void hide() {
        stage.hide();
    }

    private void show() {
        stage.show();
    }

    private void showMainWindow(SalesAgent salesAgent) {
        try {
            Stage salesAgentStage = new Stage();
            salesAgentStage.setTitle("Agent:" + salesAgent.getName());

            Scene scene = new Scene(parent);
            salesAgentStage.setScene(scene);

            salesAgentStage.initModality(Modality.WINDOW_MODAL);
            salesAgentStage.setOnHiding(event -> handleLogout());

            mainController.setAgent(salesAgent);
            mainController.initData();

            salesAgentStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setParent(Parent parent) {
        this.parent = parent;
    }
}
