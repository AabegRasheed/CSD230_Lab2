package csd230.lab2.controllers;

import csd230.lab2.entities.Ticket;
import csd230.lab2.respositories.TicketRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;

    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public String listTickets(Model model) {
        List<Ticket> tickets = ticketRepository.findAll();
        model.addAttribute("tickets", tickets);
        return "tickets";
    }

    @GetMapping("/add-ticket")
    public String showAddTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "add-ticket";
    }

    @PostMapping("/add-ticket")
    public String addTicket(@ModelAttribute Ticket ticket) {
        ticketRepository.save(ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/edit-ticket")
    public String showEditTicketForm(@RequestParam("id") Long id, Model model) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            model.addAttribute("ticket", ticket.get());
            return "edit-ticket";
        }
        return "redirect:/tickets";
    }

    @PostMapping("/edit-ticket")
    public String editTicket(@ModelAttribute Ticket ticket) {
        ticketRepository.save(ticket);
        return "redirect:/tickets";
    }

    @PostMapping("/delete-ticket")
    public String deleteTicket(@RequestParam("id") Long id) {
        ticketRepository.deleteById(id);
        return "redirect:/tickets";
    }
}
