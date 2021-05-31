package salesAgency.model;

import java.io.Serializable;

public class Order implements Serializable {
    private Integer id;
    private Integer quantity;
    private OrderStatus status;
    private Integer totalPrice;
    private Integer productId;
    private Integer salesAgentId;

    public Order(Integer quantity, OrderStatus status, Integer totalPrice, Integer salesAgentId, Integer productId) {
        this.quantity = quantity;
        this.status = status;
        this.totalPrice = totalPrice;
        this.productId = productId;
        this.salesAgentId = salesAgentId;
    }

    public Order(Integer quantity, OrderStatus status, Integer totalPrice) {
        this.quantity = quantity;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public Order() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getSalesAgentId() {
        return salesAgentId;
    }

    public void setSalesAgentId(Integer salesAgentId) {
        this.salesAgentId = salesAgentId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
