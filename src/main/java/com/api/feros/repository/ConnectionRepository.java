package com.api.feros.repository;

import com.api.feros.entity.Connection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, String> {

    @Query("SELECT DISTINCT c FROM Connection c JOIN FETCH c.client ORDER BY c.name")
    List<Connection> findAllWithClient();

    @Query("SELECT c FROM Connection c JOIN FETCH c.client WHERE c.client.id = :clientId ORDER BY c.name")
    List<Connection> findByClientId(@Param("clientId") String clientId);

    @Query("SELECT c FROM Connection c JOIN FETCH c.client WHERE c.client.id = :clientId AND c.isArchived = false ORDER BY c.name")
    List<Connection> findByClientIdAndIsArchivedFalse(@Param("clientId") String clientId);

    @Query("SELECT c FROM Connection c JOIN FETCH c.client WHERE c.client.id = :clientId AND c.status = :status ORDER BY c.name")
    List<Connection> findByClientIdAndStatus(@Param("clientId") String clientId, @Param("status") String status);

    @Query("SELECT c FROM Connection c JOIN FETCH c.client WHERE c.id = :id")
    Optional<Connection> findByIdWithClient(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Connection c WHERE c.client.id = :clientId AND LOWER(c.name) = LOWER(:name)")
    boolean existsByClientIdAndNameIgnoreCase(@Param("clientId") String clientId, @Param("name") String name);

    @Query(value = "SELECT c FROM Connection c JOIN FETCH c.client WHERE (:search IS NULL OR :search = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND (:clientId IS NULL OR :clientId = '' OR c.client.id = :clientId) AND (:activeOnly = false OR c.isArchived = false) AND (:status IS NULL OR :status = '' OR c.status = :status) ORDER BY c.name",
            countQuery = "SELECT COUNT(c) FROM Connection c WHERE (:search IS NULL OR :search = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND (:clientId IS NULL OR :clientId = '' OR c.client.id = :clientId) AND (:activeOnly = false OR c.isArchived = false) AND (:status IS NULL OR :status = '' OR c.status = :status)")
    Page<Connection> findAllWithSearchAndFilters(@Param("search") String search, @Param("clientId") String clientId, @Param("activeOnly") boolean activeOnly, @Param("status") String status, Pageable pageable);
}
