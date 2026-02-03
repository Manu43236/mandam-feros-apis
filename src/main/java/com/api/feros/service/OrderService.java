package com.api.feros.service;

import com.api.feros.entity.Client;
import com.api.feros.entity.Connection;
import com.api.feros.entity.Order;
import com.api.feros.repository.ClientRepository;
import com.api.feros.repository.ConnectionRepository;
import com.api.feros.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ConnectionRepository connectionRepository;

    public Page<Order> getAllOrdersWithSearchAndFilters(String orderNumber, String clientId, String status, String connectionId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        return orderRepository.findAllWithSearchAndFilters(orderNumber, clientId, status, connectionId, fromDate, toDate, pageable);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllWithClientAndConnection();
    }

    public List<Order> getOrdersByClientId(String clientId) {
        return orderRepository.findByClientId(clientId);
    }

    public List<Order> getOrdersByClientIdAndStatus(String clientId, String status) {
        return orderRepository.findByClientIdAndStatus(clientId, status);
    }

    public List<Order> getOrdersByConnectionId(String connectionId) {
        return orderRepository.findByConnectionId(connectionId);
    }

    public List<Order> getOrdersByClientIdAndDateRange(String clientId, LocalDate fromDate, LocalDate toDate) {
        return orderRepository.findByClientIdAndOrderDateBetween(clientId, fromDate, toDate);
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findByIdWithClientAndConnection(id);
    }

    @Transactional
    public Order createOrder(Order order) {
        Client client = clientRepository.findById(order.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + order.getClient().getId()));
        Connection connection = connectionRepository.findByIdWithClient(order.getConnection().getId())
                .orElseThrow(() -> new IllegalArgumentException("Connection not found with id: " + order.getConnection().getId()));
        if (!connection.getClient().getId().equals(client.getId())) {
            throw new IllegalArgumentException("Connection does not belong to this client");
        }

        order.setClient(client);
        order.setConnection(connection);
        Order saved = orderRepository.save(order);
        return orderRepository.findByIdWithClientAndConnection(saved.getId()).orElse(saved);
    }

    @Transactional
    public Order updateOrder(String id, Order orderDetails) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

        Client client = clientRepository.findById(orderDetails.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + orderDetails.getClient().getId()));
        Connection connection = connectionRepository.findByIdWithClient(orderDetails.getConnection().getId())
                .orElseThrow(() -> new IllegalArgumentException("Connection not found with id: " + orderDetails.getConnection().getId()));
        if (!connection.getClient().getId().equals(client.getId())) {
            throw new IllegalArgumentException("Connection does not belong to this client");
        }

        order.setClient(client);
        order.setConnection(connection);
        order.setPickupLocation(orderDetails.getPickupLocation());
        order.setDropLocation(orderDetails.getDropLocation());
        order.setMaterial(orderDetails.getMaterial());
        order.setTotalQuantity(orderDetails.getTotalQuantity());
        order.setUnit(orderDetails.getUnit());
        order.setRateType(orderDetails.getRateType());
        order.setRateValue(orderDetails.getRateValue());
        order.setOrderDate(orderDetails.getOrderDate());
        order.setExpectedPickupDate(orderDetails.getExpectedPickupDate());
        order.setExpectedDeliveryDate(orderDetails.getExpectedDeliveryDate());
        order.setReferenceNo(orderDetails.getReferenceNo());
        order.setPickupContact(orderDetails.getPickupContact());
        order.setDropContact(orderDetails.getDropContact());
        order.setNotes(orderDetails.getNotes());
        order.setStatus(orderDetails.getStatus());
        order.setUpdatedBy(orderDetails.getUpdatedBy());

        Order saved = orderRepository.save(order);
        return orderRepository.findByIdWithClientAndConnection(saved.getId()).orElse(saved);
    }

    @Transactional
    public Order updateOrderStatus(String id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
        order.setStatus(status);
        Order saved = orderRepository.save(order);
        return orderRepository.findByIdWithClientAndConnection(saved.getId()).orElse(saved);
    }

    @Transactional
    public void deleteOrder(String id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
}
