package com.api.feros.repository;

import com.api.feros.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.client JOIN FETCH o.connection ORDER BY o.orderDate DESC, o.createdAt DESC")
    List<Order> findAllWithClientAndConnection();

    @Query(value = "SELECT o FROM Order o JOIN FETCH o.client JOIN FETCH o.connection WHERE (:orderNumber IS NULL OR :orderNumber = '' OR LOWER(o.referenceNo) LIKE LOWER(CONCAT('%', :orderNumber, '%'))) AND (:clientId IS NULL OR :clientId = '' OR o.client.id = :clientId) AND (:status IS NULL OR :status = '' OR o.status = :status) AND (:connectionId IS NULL OR :connectionId = '' OR o.connection.id = :connectionId) AND (:fromDate IS NULL OR o.orderDate >= :fromDate) AND (:toDate IS NULL OR o.orderDate <= :toDate) ORDER BY o.orderDate DESC, o.createdAt DESC",
            countQuery = "SELECT COUNT(o) FROM Order o WHERE (:orderNumber IS NULL OR :orderNumber = '' OR LOWER(o.referenceNo) LIKE LOWER(CONCAT('%', :orderNumber, '%'))) AND (:clientId IS NULL OR :clientId = '' OR o.client.id = :clientId) AND (:status IS NULL OR :status = '' OR o.status = :status) AND (:connectionId IS NULL OR :connectionId = '' OR o.connection.id = :connectionId) AND (:fromDate IS NULL OR o.orderDate >= :fromDate) AND (:toDate IS NULL OR o.orderDate <= :toDate)")
    Page<Order> findAllWithSearchAndFilters(@Param("orderNumber") String orderNumber, @Param("clientId") String clientId, @Param("status") String status, @Param("connectionId") String connectionId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.client JOIN FETCH o.connection WHERE o.client.id = :clientId ORDER BY o.orderDate DESC, o.createdAt DESC")
    List<Order> findByClientId(@Param("clientId") String clientId);

    @Query("SELECT o FROM Order o JOIN FETCH o.client JOIN FETCH o.connection WHERE o.client.id = :clientId AND o.status = :status ORDER BY o.orderDate DESC")
    List<Order> findByClientIdAndStatus(@Param("clientId") String clientId, @Param("status") String status);

    @Query("SELECT o FROM Order o JOIN FETCH o.client JOIN FETCH o.connection WHERE o.connection.id = :connectionId ORDER BY o.orderDate DESC")
    List<Order> findByConnectionId(@Param("connectionId") String connectionId);

    @Query("SELECT o FROM Order o JOIN FETCH o.client JOIN FETCH o.connection WHERE o.id = :id")
    Optional<Order> findByIdWithClientAndConnection(@Param("id") String id);

    @Query("SELECT o FROM Order o JOIN FETCH o.client JOIN FETCH o.connection WHERE o.client.id = :clientId AND o.orderDate BETWEEN :fromDate AND :toDate ORDER BY o.orderDate DESC")
    List<Order> findByClientIdAndOrderDateBetween(@Param("clientId") String clientId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
