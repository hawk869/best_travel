package com.danielesteban.best_travel.infraestructure.services;

import com.danielesteban.best_travel.api.models.request.TicketRequest;
import com.danielesteban.best_travel.api.models.responses.FlyResponse;
import com.danielesteban.best_travel.api.models.responses.TicketResponse;
import com.danielesteban.best_travel.domain.entities.TicketEntity;
import com.danielesteban.best_travel.domain.repositories.CustomerRepository;
import com.danielesteban.best_travel.domain.repositories.FlyRepository;
import com.danielesteban.best_travel.domain.repositories.TicketRepository;
import com.danielesteban.best_travel.infraestructure.abstract_services.ITicketService;
import com.danielesteban.best_travel.util.BestTravelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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

        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow();
        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(BestTravelUtil.getRandomLater())
                .departureDate(BestTravelUtil.getRandomSoon())
                .build();
        var ticketPersisted = this.ticketRepository.save(ticketToPersist);
        log.info("Ticket saved with id: {}", ticketPersisted.getId());

        return this.entityToResponse(ticketPersisted);
    }

    @Override
    public TicketResponse read(UUID uuid) {
        var ticketFromDB = this.ticketRepository.findById(uuid).orElseThrow();
        return this.entityToResponse(ticketFromDB);
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID uuid) {
        var ticketToUpdate = this.ticketRepository.findById(uuid).orElseThrow();
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)));
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLater());
        var ticketPersisted = this.ticketRepository.save(ticketToUpdate);

        log.info("Ticket updated with id: {}", ticketPersisted.getId());
        return entityToResponse(ticketPersisted);
    }

    @Override
    public void delete(UUID uuid) {
        var ticketToDelete = this.ticketRepository.findById(uuid).orElseThrow();
        this.ticketRepository.delete(ticketToDelete);
    }

    @Override
    public BigDecimal findPrice(Long flyId) {
        var fly = this.flyRepository.findById(flyId).orElseThrow();
        return fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage));
    }

    private TicketResponse entityToResponse(TicketEntity ticketEntity) {
        var response = new TicketResponse();
        BeanUtils.copyProperties(ticketEntity, response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(ticketEntity.getFly(), flyResponse);
        response.setFly(flyResponse);
        return response;
    }

    private static final BigDecimal charger_price_percentage = BigDecimal.valueOf(0.25);
}
