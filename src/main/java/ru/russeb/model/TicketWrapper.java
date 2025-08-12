package ru.russeb.model;

import java.util.List;

public class TicketWrapper {
    private List<Ticket> tickets;

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    public List<Ticket> getTickets() {
        return tickets;
    }
}
