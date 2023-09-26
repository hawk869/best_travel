package com.danielesteban.best_travel.infraestructure.services;

import com.danielesteban.best_travel.api.models.request.TicketRequest;
import com.danielesteban.best_travel.api.models.responses.FlyResponse;
import com.danielesteban.best_travel.api.models.responses.TicketResponse;
import com.danielesteban.best_travel.domain.entities.TicketEntity;
import com.danielesteban.best_travel.domain.repositories.CustomerRepository;
import com.danielesteban.best_travel.domain.repositories.FlyRepository;
import com.danielesteban.best_travel.domain.repositories.TicketRepository;
import com.danielesteban.best_travel.infraestructure.abstract_services.ITicketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TicketService implements ITicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;


    @Override
    public TicketResponse create(TicketRequest request) {
        return null;
    }

    @Override
    public TicketResponse read(UUID uuid) {
        return null;
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID uuid) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }
    private TicketResponse entityToResponse(TicketEntity ticketEntity) {
        var response = new TicketResponse();
        BeanUtils.copyProperties(ticketEntity, response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(ticketEntity.getFly(), flyResponse);
        response.setFly(flyResponse);
        return response;
    }
}
