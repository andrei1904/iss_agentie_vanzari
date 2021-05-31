package salesAgency.server;

import salesAgency.model.*;
import salesAgency.persistence.OrderRepository;
import salesAgency.persistence.ProductRepository;
import salesAgency.persistence.SalesAgentRepository;
import salesAgency.services.ISalesAgencyObserver;
import salesAgency.services.ISalesAgencyServices;
import salesAgency.services.SalesAgencyException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SalesAgencyServicesImpl implements ISalesAgencyServices {
    private final SalesAgentRepository salesAgentRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final Map<Integer, ISalesAgencyObserver> loggedAgents;

    public SalesAgencyServicesImpl(SalesAgentRepository salesAgentRepository,
                                   ProductRepository productRepository,
                                   OrderRepository orderRepository) {
        this.salesAgentRepository = salesAgentRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        loggedAgents = new ConcurrentHashMap<>();
    }


    @Override
    public synchronized SalesAgent loginSalesAgent(String username, String password, ISalesAgencyObserver agent) throws SalesAgencyException {
        SalesAgent salesAgent = salesAgentRepository.findOne(username);

        if (salesAgent != null && salesAgent.getPassword().equals(password)) {
            if (loggedAgents.get(salesAgent.getId()) != null) {
                throw new SalesAgencyException("SalesAgent already logged in!");
            }

            loggedAgents.put(salesAgent.getId(), agent);

            return salesAgent;
        } else {
            throw new SalesAgencyException("Authentification failed!");
        }

    }

    @Override
    public synchronized List<Product> getAllProducts() {
        return productRepository.getAll();
    }

    @Override
    public synchronized List<OrderDto> getOrdersBySalesAgent(Integer id) {
        List<Order> orders = orderRepository.getAllBySalesAgent(id);

        List<OrderDto> res = new ArrayList<>();
        for (Order order : orders) {
            OrderDto orderDto = new OrderDto(productRepository
                    .getOneById(order.getProductId()).getName(),
                    order.getQuantity(), order.getStatus());
            orderDto.setId(order.getId());
            orderDto.setProductId(order.getProductId());
            res.add(orderDto);
        }

        return res;
    }

    @Override
    public synchronized List<OrderDto> getOrdersByOtherAgents(Integer id) {
        List<Order> orders = orderRepository.getAllWithoutSalesAgent(id);

        List<OrderDto> res = new ArrayList<>();
        for (Order order : orders) {
            res.add(new OrderDto(productRepository.getOneById(order.getProductId()).getName(),
                    order.getQuantity(),
                    salesAgentRepository.getOneById(order.getSalesAgentId()).getName()));
        }

        return res;
    }

    @Override
    public synchronized Order addOrder(int quantity, OrderStatus orderStatus, int price,
                          Integer productId, Integer salesAgentId) {
        Order order = new Order(quantity, orderStatus, price, salesAgentId, productId);

        productRepository.updateQuantity(productId,
                productRepository.getOneById(productId).getQuantity() - quantity);

        Order res = orderRepository.save(order);
        notifyOrderChanged();
        return res;
    }

    @Override
    public synchronized void logoutSalesAgent(SalesAgent salesAgent, ISalesAgencyObserver agent) throws SalesAgencyException {
        SalesAgent res = salesAgentRepository.findOne(salesAgent.getUsername());

        if (res == null) {
            throw new SalesAgencyException("Agent " + salesAgent.getId() + " is not logged in!");
        }

        ISalesAgencyObserver resAgent = loggedAgents.remove(salesAgent.getId());

        if (resAgent == null) {
            throw new SalesAgencyException("Agent " + salesAgent.getId() + " is not logged in!");
        }
    }

    @Override
    public synchronized void cancelOrder(Integer id, Integer productId, Integer quantity) {
        orderRepository.removeOrder(id);

        productRepository.updateQuantity(productId,
                productRepository.getOneById(productId).getQuantity() + quantity);

        notifyOrderChanged();
    }

    @Override
    public synchronized void chengeOrderStatus(Integer id) {
        orderRepository.updateStatus(id);

        notifyOrderChanged();
    }

    private synchronized void notifyOrderChanged() {
        int defaultThreadsNo = 5;
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);

        for (ISalesAgencyObserver agent : loggedAgents.values()) {
            if (agent != null) {
                executor.execute(() -> {
                    try {
                        agent.orderChanged();
                    } catch (SalesAgencyException | RemoteException e) {
                        System.err.println("Error notifying clients " + e);
                    }
                });
            }
        }
        executor.shutdown();
    }
}
