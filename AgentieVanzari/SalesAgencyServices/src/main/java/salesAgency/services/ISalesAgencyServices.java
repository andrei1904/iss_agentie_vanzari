package salesAgency.services;

import salesAgency.model.*;

import java.util.List;

public interface ISalesAgencyServices {
    SalesAgent loginSalesAgent(String username, String password, ISalesAgencyObserver agent) throws SalesAgencyException;

    List<Product> getAllProducts();

    List<OrderDto> getOrdersBySalesAgent(Integer id);

    List<OrderDto> getOrdersByOtherAgents(Integer id);

    Order addOrder(int quantity, OrderStatus onHold, int totalPrice, Integer id, Integer id1);

    void logoutSalesAgent(SalesAgent salesAgent, ISalesAgencyObserver agent) throws SalesAgencyException;

    void cancelOrder(Integer id, Integer productId, Integer quantity);

    void chengeOrderStatus(Integer id);
}
