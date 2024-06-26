package com.danielesteban.best_travel.api.controller;

import com.danielesteban.best_travel.api.models.request.TicketRequest;
import com.danielesteban.best_travel.api.models.responses.TicketResponse;
import com.danielesteban.best_travel.infraestructure.abstract_services.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
public class TicketController {

    private final ITicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> post(@RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(ticketService.create(ticketRequest));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<TicketResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketService.read(id));
    }
    @PutMapping(path = "{id}")
    public ResponseEntity<TicketResponse> update(@PathVariable UUID id, @RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(ticketService.update(ticketRequest, id));
    }
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getFlyPrice(@RequestParam Long flyId) {
        return ResponseEntity.ok(Collections.singletonMap("flyPrice", ticketService.findPrice(flyId)));
    }
}
