package com.api.feros.repository;

import com.api.feros.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {

    @Query("SELECT DISTINCT u FROM AppUser u JOIN FETCH u.client ORDER BY u.name")
    List<AppUser> findAllWithClient();

    @Query(value = "SELECT u FROM AppUser u JOIN FETCH u.client WHERE (:search IS NULL OR :search = '' OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR u.phone LIKE CONCAT('%', :search, '%')) AND (:clientId IS NULL OR :clientId = '' OR u.client.id = :clientId) AND (:activeOnly = false OR u.isArchived = false) ORDER BY u.name",
            countQuery = "SELECT COUNT(u) FROM AppUser u WHERE (:search IS NULL OR :search = '' OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR u.phone LIKE CONCAT('%', :search, '%')) AND (:clientId IS NULL OR :clientId = '' OR u.client.id = :clientId) AND (:activeOnly = false OR u.isArchived = false)")
    Page<AppUser> findAllWithSearchAndFilters(@Param("search") String search, @Param("clientId") String clientId, @Param("activeOnly") boolean activeOnly, Pageable pageable);

    @Query("SELECT u FROM AppUser u JOIN FETCH u.client WHERE u.client.id = :clientId ORDER BY u.name")
    List<AppUser> findByClientId(@Param("clientId") String clientId);

    @Query("SELECT u FROM AppUser u JOIN FETCH u.client WHERE u.client.id = :clientId AND u.isArchived = false ORDER BY u.name")
    List<AppUser> findByClientIdAndIsArchivedFalse(@Param("clientId") String clientId);

    @Query("SELECT u FROM AppUser u JOIN FETCH u.client WHERE u.id = :id")
    Optional<AppUser> findByIdWithClient(@Param("id") String id);

    @Query("SELECT u FROM AppUser u JOIN FETCH u.client WHERE LOWER(u.email) = LOWER(:email)")
    Optional<AppUser> findByEmailIgnoreCaseWithClient(@Param("email") String email);

    Optional<AppUser> findByEmailIgnoreCase(String email);

    @Query("SELECT u FROM AppUser u JOIN FETCH u.client WHERE LOWER(u.email) = LOWER(:email) AND u.client.id = :clientId")
    Optional<AppUser> findByEmailIgnoreCaseAndClientId(@Param("email") String email, @Param("clientId") String clientId);

    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM AppUser u WHERE LOWER(u.email) = LOWER(:email) AND u.client.id = :clientId")
    boolean existsByEmailIgnoreCaseAndClientId(@Param("email") String email, @Param("clientId") String clientId);

    @Query("SELECT u FROM AppUser u JOIN FETCH u.client WHERE u.client.id = :clientId AND u.phone = :phone")
    Optional<AppUser> findByClientIdAndPhone(@Param("clientId") String clientId, @Param("phone") String phone);

    /** Find user by phone (unique per user; one user = one client) */
    @Query("SELECT u FROM AppUser u JOIN FETCH u.client WHERE u.phone = :phone")
    Optional<AppUser> findByPhoneWithClient(@Param("phone") String phone);
}
