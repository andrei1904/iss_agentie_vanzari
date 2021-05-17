package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Product;
import model.SalesAgent;
import service.AllService;

import java.util.List;

public class MainController {
    ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> tableViewProducts;

    @FXML
    private TableColumn<Integer, Product> tableColumnProductName;

    @FXML
    private TableColumn<Integer, Product> tableColumnProductPrice;

    @FXML
    private TableColumn<Integer, Product> tableColumnProductQuantity;

    private AllService service;
    private SalesAgent agent;

    public void set(AllService service, SalesAgent agent) {
        this.service = service;
        this.agent = agent;
        init();
        initProducts();
    }

    private void init() {
        tableColumnProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableColumnProductQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableViewProducts.setItems(products);
    }

    private void initProducts() {
        List<Product> productsList = service.getAllProducts();
        products.setAll(productsList);
    }
}
