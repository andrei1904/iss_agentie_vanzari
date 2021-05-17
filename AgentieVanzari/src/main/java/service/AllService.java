package service;
import model.Product;
import model.SalesAgent;

import java.util.List;

public class AllService {
    private final SalesAgentService salesAgentService;
    private final ProductService productService;

    public AllService(SalesAgentService salesAgentService, ProductService productService) {
        this.salesAgentService = salesAgentService;
        this.productService = productService;
    }

    public SalesAgent loginSalesAgent(String username, String pasword) {
        return salesAgentService.login(username, pasword);
    }

    public List<Product> getAllProducts() {
        return productService.getAll();
    }
}
