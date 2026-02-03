package com.api.feros.repository;

import com.api.feros.entity.LorryReceipt;
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
public interface LorryReceiptRepository extends JpaRepository<LorryReceipt, String> {

    @Query(value = "SELECT lr FROM LorryReceipt lr JOIN FETCH lr.client JOIN FETCH lr.assignment JOIN FETCH lr.order JOIN FETCH lr.vehicle JOIN FETCH lr.driver JOIN FETCH lr.connection WHERE (:clientId IS NULL OR :clientId = '' OR lr.client.id = :clientId) AND (:status IS NULL OR :status = '' OR lr.status = :status) AND (:orderId IS NULL OR :orderId = '' OR lr.order.id = :orderId) AND (:fromDate IS NULL OR lr.lrDate >= :fromDate) AND (:toDate IS NULL OR lr.lrDate <= :toDate) ORDER BY lr.lrDate DESC, lr.createdAt DESC",
            countQuery = "SELECT COUNT(lr) FROM LorryReceipt lr WHERE (:clientId IS NULL OR :clientId = '' OR lr.client.id = :clientId) AND (:status IS NULL OR :status = '' OR lr.status = :status) AND (:orderId IS NULL OR :orderId = '' OR lr.order.id = :orderId) AND (:fromDate IS NULL OR lr.lrDate >= :fromDate) AND (:toDate IS NULL OR lr.lrDate <= :toDate)")
    Page<LorryReceipt> findAllWithFilters(@Param("clientId") String clientId, @Param("status") String status, @Param("orderId") String orderId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query("SELECT DISTINCT lr FROM LorryReceipt lr " +
            "JOIN FETCH lr.client JOIN FETCH lr.assignment JOIN FETCH lr.order " +
            "JOIN FETCH lr.vehicle JOIN FETCH lr.driver JOIN FETCH lr.connection " +
            "ORDER BY lr.lrDate DESC, lr.createdAt DESC")
    List<LorryReceipt> findAllWithDetails();

    @Query("SELECT lr FROM LorryReceipt lr " +
            "JOIN FETCH lr.client JOIN FETCH lr.assignment JOIN FETCH lr.order " +
            "JOIN FETCH lr.vehicle JOIN FETCH lr.driver JOIN FETCH lr.connection " +
            "WHERE lr.client.id = :clientId ORDER BY lr.lrDate DESC, lr.createdAt DESC")
    List<LorryReceipt> findByClientId(@Param("clientId") String clientId);

    @Query("SELECT lr FROM LorryReceipt lr " +
            "JOIN FETCH lr.client JOIN FETCH lr.assignment JOIN FETCH lr.order " +
            "JOIN FETCH lr.vehicle JOIN FETCH lr.driver JOIN FETCH lr.connection " +
            "WHERE lr.client.id = :clientId AND lr.status = :status ORDER BY lr.lrDate DESC")
    List<LorryReceipt> findByClientIdAndStatus(@Param("clientId") String clientId, @Param("status") String status);

    @Query("SELECT lr FROM LorryReceipt lr " +
            "JOIN FETCH lr.client JOIN FETCH lr.assignment JOIN FETCH lr.order " +
            "JOIN FETCH lr.vehicle JOIN FETCH lr.driver JOIN FETCH lr.connection " +
            "WHERE lr.order.id = :orderId ORDER BY lr.lrDate DESC")
    List<LorryReceipt> findByOrderId(@Param("orderId") String orderId);

    @Query("SELECT lr FROM LorryReceipt lr " +
            "JOIN FETCH lr.client JOIN FETCH lr.assignment JOIN FETCH lr.order " +
            "JOIN FETCH lr.vehicle JOIN FETCH lr.driver JOIN FETCH lr.connection " +
            "WHERE lr.assignment.id = :assignmentId")
    Optional<LorryReceipt> findByAssignmentId(@Param("assignmentId") String assignmentId);

    @Query("SELECT lr FROM LorryReceipt lr " +
            "JOIN FETCH lr.client JOIN FETCH lr.assignment JOIN FETCH lr.order " +
            "JOIN FETCH lr.vehicle JOIN FETCH lr.driver JOIN FETCH lr.connection " +
            "WHERE lr.client.id = :clientId AND lr.lrDate BETWEEN :fromDate AND :toDate ORDER BY lr.lrDate DESC")
    List<LorryReceipt> findByClientIdAndLrDateBetween(@Param("clientId") String clientId,
                                                       @Param("fromDate") LocalDate fromDate,
                                                       @Param("toDate") LocalDate toDate);

    @Query("SELECT lr FROM LorryReceipt lr " +
            "JOIN FETCH lr.client JOIN FETCH lr.assignment JOIN FETCH lr.order " +
            "JOIN FETCH lr.vehicle JOIN FETCH lr.driver JOIN FETCH lr.connection " +
            "WHERE lr.id = :id")
    Optional<LorryReceipt> findByIdWithDetails(@Param("id") String id);
}
