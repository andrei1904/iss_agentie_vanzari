package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.SalesAgent;
import service.AllService;

import java.io.IOException;

public class LoginController {
    @FXML public TextField textFieldUsername;
    @FXML public TextField textFieldPassword;

    private AllService allService;
    private Stage stage;

    public void set(AllService allService, Stage stage) {
        this.allService = allService;
        this.stage = stage;
    }

    public void handleLogin() {
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();

        if (username.equals("") || password.equals("")) {
            MessageAlert.showErrorMessage(null, "Enter username and password!");
            return;
        }

        SalesAgent salesAgent = allService.loginSalesAgent(username, password);
        if (salesAgent != null) {
            showMainWindow(salesAgent);
            hide();
        } else {
            MessageAlert.showErrorMessage(null, "Wrong user!");
        }
    }

    private void hide() {
        stage.hide();
    }

    private void showMainWindow(SalesAgent salesAgent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../mainView.fxml"));

            AnchorPane root = loader.load();

            Stage salesAgentStage = new Stage();
            salesAgentStage.setTitle("Agent:" + salesAgent.getName());
            salesAgentStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            salesAgentStage.setScene(scene);

            MainController mainViewController = loader.getController();
            mainViewController.set(allService, salesAgent);

            salesAgentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
