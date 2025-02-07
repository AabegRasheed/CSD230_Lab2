package csd230.lab2.controllers;

import csd230.lab2.entities.Publication;
import csd230.lab2.respositories.PublicationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/publications")
public class PublicationController {

    private final PublicationRepository publicationRepository;

    public PublicationController(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    @GetMapping
    public String listPublications(Model model) {
        List<Publication> publications = publicationRepository.findAll();
        model.addAttribute("publications", publications);
        return "publications";
    }

    @GetMapping("/add-publication")
    public String showAddPublicationForm(Model model) {
        model.addAttribute("publication", new Publication());
        return "add-publication";
    }

    @PostMapping("/add-publication")
    public String addPublication(@ModelAttribute Publication publication) {
        publicationRepository.save(publication);
        return "redirect:/publications";
    }

    @GetMapping("/edit-publication")
    public String showEditPublicationForm(@RequestParam("id") Long id, Model model) {
        Optional<Publication> publication = publicationRepository.findById(id);
        if (publication.isPresent()) {
            model.addAttribute("publication", publication.get());
            return "edit-publication";
        }
        return "redirect:/publications";
    }

    @PostMapping("/edit-publication")
    public String editPublication(@ModelAttribute Publication publication) {
        publicationRepository.save(publication);
        return "redirect:/publications";
    }

    @PostMapping("/delete-publication")
    public String deletePublication(@RequestParam("id") Long id) {
        publicationRepository.deleteById(id);
        return "redirect:/publications";
    }
}