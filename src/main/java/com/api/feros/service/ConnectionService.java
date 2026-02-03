package com.api.feros.service;

import com.api.feros.entity.Connection;
import com.api.feros.entity.Client;
import com.api.feros.repository.ClientRepository;
import com.api.feros.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final ClientRepository clientRepository;

    public Page<Connection> getAllConnectionsWithSearchAndFilters(String search, String clientId, boolean activeOnly, String status, Pageable pageable) {
        return connectionRepository.findAllWithSearchAndFilters(search, clientId, activeOnly, status, pageable);
    }

    public List<Connection> getAllConnections() {
        return connectionRepository.findAllWithClient();
    }

    public List<Connection> getConnectionsByClientId(String clientId) {
        return connectionRepository.findByClientId(clientId);
    }

    public List<Connection> getActiveConnectionsByClientId(String clientId) {
        return connectionRepository.findByClientIdAndIsArchivedFalse(clientId);
    }

    public List<Connection> getConnectionsByClientIdAndStatus(String clientId, String status) {
        return connectionRepository.findByClientIdAndStatus(clientId, status);
    }

    public Optional<Connection> getConnectionById(String id) {
        return connectionRepository.findByIdWithClient(id);
    }

    @Transactional
    public Connection createConnection(Connection connection) {
        Client client = clientRepository.findById(connection.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + connection.getClient().getId()));

        if (connectionRepository.existsByClientIdAndNameIgnoreCase(client.getId(), connection.getName())) {
            throw new IllegalArgumentException("Connection with name '" + connection.getName() + "' already exists for this client");
        }

        connection.setClient(client);
        Connection saved = connectionRepository.save(connection);
        return connectionRepository.findByIdWithClient(saved.getId()).orElse(saved);
    }

    @Transactional
    public Connection updateConnection(String id, Connection connectionDetails) {
        Connection connection = connectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Connection not found with id: " + id));

        if (!connection.getName().equalsIgnoreCase(connectionDetails.getName())) {
            if (connectionRepository.existsByClientIdAndNameIgnoreCase(connection.getClient().getId(), connectionDetails.getName())) {
                throw new IllegalArgumentException("Connection with name '" + connectionDetails.getName() + "' already exists for this client");
            }
        }

        Client client = clientRepository.findById(connectionDetails.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + connectionDetails.getClient().getId()));

        connection.setClient(client);
        connection.setName(connectionDetails.getName());
        connection.setAddressText(connectionDetails.getAddressText());
        connection.setPhone(connectionDetails.getPhone());
        connection.setEmail(connectionDetails.getEmail());
        connection.setGstNumber(connectionDetails.getGstNumber());
        connection.setPan(connectionDetails.getPan());
        connection.setStatus(connectionDetails.getStatus());
        connection.setIsArchived(connectionDetails.getIsArchived());
        connection.setUpdatedBy(connectionDetails.getUpdatedBy());

        Connection saved = connectionRepository.save(connection);
        return connectionRepository.findByIdWithClient(saved.getId()).orElse(saved);
    }

    @Transactional
    public void archiveConnection(String id) {
        Connection connection = connectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Connection not found with id: " + id));
        connection.setIsArchived(true);
        connectionRepository.save(connection);
    }

    @Transactional
    public void unarchiveConnection(String id) {
        Connection connection = connectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Connection not found with id: " + id));
        connection.setIsArchived(false);
        connectionRepository.save(connection);
    }

    @Transactional
    public void deleteConnection(String id) {
        if (!connectionRepository.existsById(id)) {
            throw new IllegalArgumentException("Connection not found with id: " + id);
        }
        connectionRepository.deleteById(id);
    }
}
