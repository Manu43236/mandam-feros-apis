package com.api.feros.service;

import com.api.feros.entity.Client;
import com.api.feros.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Page<Client> getAllClientsWithSearchAndFilters(String search, String status, Pageable pageable) {
        return clientRepository.findAllWithSearchAndStatus(search, status, pageable);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<Client> getClientsByStatus(String status) {
        return clientRepository.findByStatusOrderByCompanyTradeNameAsc(status);
    }

    public Optional<Client> getClientById(String id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public Client createClient(Client client) {
        if (clientRepository.existsByCompanyTradeNameIgnoreCase(client.getCompanyTradeName())) {
            throw new IllegalArgumentException("Client with company name '" + client.getCompanyTradeName() + "' already exists");
        }
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(String id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + id));

        if (!client.getCompanyTradeName().equalsIgnoreCase(clientDetails.getCompanyTradeName())) {
            if (clientRepository.existsByCompanyTradeNameIgnoreCase(clientDetails.getCompanyTradeName())) {
                throw new IllegalArgumentException("Client with company name '" + clientDetails.getCompanyTradeName() + "' already exists");
            }
        }

        client.setCompanyTradeName(clientDetails.getCompanyTradeName());
        client.setBusinessType(clientDetails.getBusinessType());
        client.setCompanyLogo(clientDetails.getCompanyLogo());
        client.setHqCity(clientDetails.getHqCity());
        client.setHqState(clientDetails.getHqState());
        client.setHqAddressLine(clientDetails.getHqAddressLine());
        client.setHqPincode(clientDetails.getHqPincode());
        client.setPrimaryContactName(clientDetails.getPrimaryContactName());
        client.setPrimaryPhone(clientDetails.getPrimaryPhone());
        client.setPrimaryEmail(clientDetails.getPrimaryEmail());
        client.setSecondaryPhone(clientDetails.getSecondaryPhone());
        client.setSecondaryEmail(clientDetails.getSecondaryEmail());
        client.setGstRegistered(clientDetails.getGstRegistered());
        client.setGstin(clientDetails.getGstin());
        client.setPan(clientDetails.getPan());
        client.setBillingAddress(clientDetails.getBillingAddress());
        client.setBankDetails(clientDetails.getBankDetails());
        client.setDocumentSeries(clientDetails.getDocumentSeries());
        client.setSubscription(clientDetails.getSubscription());
        client.setInvoiceFooterTerms(clientDetails.getInvoiceFooterTerms());
        client.setStatus(clientDetails.getStatus());
        client.setUpdatedBy(clientDetails.getUpdatedBy());

        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(String id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }
}
