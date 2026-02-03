package com.api.feros.repository;

import com.api.feros.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    List<Client> findByStatusOrderByCompanyTradeNameAsc(String status);

    Optional<Client> findByCompanyTradeNameIgnoreCase(String companyTradeName);

    boolean existsByCompanyTradeNameIgnoreCase(String companyTradeName);

    @Query("SELECT c FROM Client c WHERE (:search IS NULL OR :search = '' OR LOWER(c.companyTradeName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.primaryContactName) LIKE LOWER(CONCAT('%', :search, '%'))) AND (:status IS NULL OR :status = '' OR c.status = :status) ORDER BY c.companyTradeName")
    Page<Client> findAllWithSearchAndStatus(@Param("search") String search, @Param("status") String status, Pageable pageable);
}
