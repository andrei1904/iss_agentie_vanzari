package salesAgency.client.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import salesAgency.model.OrderDto;
import salesAgency.model.OrderStatus;
import salesAgency.model.Product;
import salesAgency.model.SalesAgent;
import salesAgency.services.ISalesAgencyObserver;
import salesAgency.services.ISalesAgencyServices;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MainController extends UnicastRemoteObject implements ISalesAgencyObserver, Serializable {
    ObservableList<Product> products = FXCollections.observableArrayList();
    ObservableList<OrderDto> myOrders = FXCollections.observableArrayList();
    ObservableList<OrderDto> otherOrders = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> tableViewProducts;

    @FXML
    private TableColumn<Integer, Product> tableColumnProductName;

    @FXML
    private TableColumn<Integer, Product> tableColumnProductPrice;

    @FXML
    private TableColumn<Integer, Product> tableColumnProductQuantity;

    @FXML
    private TableView<OrderDto> tableViewMyOrders;

    @FXML
    private TableColumn<Integer, OrderDto> tableColumnOrderName;

    @FXML
    private TableColumn<Integer, OrderDto> tableColumnOrderQuantity;

    @FXML
    private TableColumn<Integer, OrderDto> tableColumnOrderStatus;

    @FXML
    private TableView<OrderDto> tableViewOtherOrders;

    @FXML
    private TableColumn<Integer, OrderDto> tableColumnOtherOrderName;

    @FXML
    private TableColumn<Integer, OrderDto> tableColumnOtherOrderQuantity;

    @FXML
    private TableColumn<Integer, OrderDto> tableColumnOtherOrderAgentName;

    private ISalesAgencyServices server;
    private SalesAgent agent;

    public MainController() throws RemoteException {
        super();
    }

    public MainController(ISalesAgencyServices server) throws RemoteException {
        super();
        this.server = server;
        init();
    }

    public void set(ISalesAgencyServices server) {
        this.server = server;
        init();
    }

    public void setSalesAgent(SalesAgent salesAgent) {
        agent = salesAgent;
    }

    public void initData() {
        initProducts();
        initMyOrders();
        initOtherOrders();
    }


    private void init() {
        tableColumnProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableColumnProductQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableViewProducts.setItems(products);

        tableColumnOrderName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tableColumnOrderQuantity.setCellValueFactory(new PropertyValueFactory<>("orderedQuantity"));
        tableColumnOrderStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewMyOrders.setItems(myOrders);

        tableColumnOtherOrderName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tableColumnOtherOrderQuantity.setCellValueFactory(new PropertyValueFactory<>("orderedQuantity"));
        tableColumnOtherOrderAgentName.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        tableViewOtherOrders.setItems(otherOrders);
    }

    private void initProducts() {
        List<Product> productsList = server.getAllProducts();
        products.setAll(productsList);
    }

    private void initMyOrders() {
        List<OrderDto> myOrdesrsList = server.getOrdersBySalesAgent(agent.getId());
        myOrders.setAll(myOrdesrsList);
    }

    private void initOtherOrders() {
        List<OrderDto> otherOrdersList = server.getOrdersByOtherAgents(agent.getId());
        otherOrders.setAll(otherOrdersList);
    }

    public void handleAddOrder() {
        Product product = tableViewProducts.getSelectionModel().getSelectedItem();

        if (product == null) {
            MessageAlert.showErrorMessage(null, "Select a product!");
            return;
        }

        showOrderView(product);
    }

    @Override
    public void orderChanged() {
        initMyOrders();
        initOtherOrders();
        initProducts();
    }

    private void showOrderView(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/orderView.fxml"));

            AnchorPane root = loader.load();

            Stage salesAgentStage = new Stage();
            salesAgentStage.setTitle("New order");
            salesAgentStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            salesAgentStage.setScene(scene);

            OrderController orderController = loader.getController();
            orderController.set(server, product, agent, scene);

            salesAgentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCancelOrder() {
        OrderDto order = tableViewMyOrders.getSelectionModel().getSelectedItem();

        if (order == null) {
            MessageAlert.showErrorMessage(null, "Select an order!");
            return;
        }

        server.cancelOrder(order.getId(), order.getProductId(), order.getOrderedQuantity());

    }

    public void setAgent(SalesAgent salesAgent) {
        this.agent = salesAgent;
    }

    public void handleCompleteOrder() {
        OrderDto order = tableViewMyOrders.getSelectionModel().getSelectedItem();

        if (order == null) {
            MessageAlert.showErrorMessage(null, "Select an order!");
            return;
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            MessageAlert.showErrorMessage(null, "Comanda are deja statusul: \"Completed\"!");
            return;
        }

        server.chengeOrderStatus(order.getId());
    }
}
