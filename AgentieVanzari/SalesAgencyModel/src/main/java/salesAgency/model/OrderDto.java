package salesAgency.model;

import java.io.Serializable;

public class OrderDto implements Serializable {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer orderedQuantity;
    private OrderStatus status;
    private String agentName;

    public OrderDto(String productName, Integer orderedQuantity, OrderStatus status) {
        this.productName = productName;
        this.orderedQuantity = orderedQuantity;
        this.status = status;
    }

    public OrderDto(String productName, Integer orderedQuantity, String agentName) {
        this.productName = productName;
        this.orderedQuantity = orderedQuantity;
        this.agentName = agentName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(Integer orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
