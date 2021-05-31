package salesAgency.client.gui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import salesAgency.model.OrderStatus;
import salesAgency.model.Product;
import salesAgency.model.SalesAgent;
import salesAgency.services.ISalesAgencyServices;

public class OrderController {
    private ISalesAgencyServices server;
    private Product product;
    private SalesAgent salesAgent;
    private Scene scene;

    @FXML
    private TextField textFieldProduct;

    @FXML
    private TextField textFieldQuantity;

    public void set(ISalesAgencyServices server, Product product, SalesAgent salesAgent, Scene scene) {
        this.server = server;
        this.product = product;
        this.salesAgent = salesAgent;
        this.scene = scene;

        textFieldProduct.setText(product.getName());
    }


    public void handleConfirmOrder() {
        int quantity = -1;

        if (textFieldQuantity.getText() != null) {
            try {
                quantity = Integer.parseInt(textFieldQuantity.getText());
            } catch (Error e) {
                MessageAlert.showErrorMessage(null, "Enter a number!");
            }
        } else {
            MessageAlert.showErrorMessage(null, "Add quantity!");
            return;
        }

        if (quantity > product.getQuantity()) {
            MessageAlert.showErrorMessage(null, "We don't have this quantity!");
            return;
        }

        if (quantity <= 0) {
            MessageAlert.showErrorMessage(null, "Enter a positive value!");
            return;
        }

        int totalPrice = quantity * product.getPrice();
        if (server.addOrder(quantity, OrderStatus.ON_HOLD, totalPrice,
                product.getId(), salesAgent.getId()) == null) {
            MessageAlert.showErrorMessage(null, "Error");
        }

        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
}
