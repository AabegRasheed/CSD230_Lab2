package csd230.lab2.controllers;

import csd230.lab2.entities.DiscMag;
import csd230.lab2.respositories.DiscMagRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/discMags")
public class DiscMagController {

    private final DiscMagRepository discMagRepository;

    public DiscMagController(DiscMagRepository discMagRepository) {
        this.discMagRepository = discMagRepository;
    }

    @GetMapping
    public String listDiscMags(Model model) {
        List<DiscMag> discMags = discMagRepository.findAll();
        model.addAttribute("discMags", discMags);
        return "discmags";
    }

    @GetMapping("/add")
    public String showAddDiscMagForm(Model model) {
        model.addAttribute("discMag", new DiscMag());
        return "add-discmag";
    }

    @PostMapping("/add")
    public String addDiscMag(@ModelAttribute DiscMag discMag) {
        discMagRepository.save(discMag);
        return "redirect:/discmags";
    }

    @GetMapping("/edit")
    public String showEditDiscMagForm(@RequestParam("id") Long id, Model model) {
        Optional<DiscMag> discMag = discMagRepository.findById(id);
        if (discMag.isPresent()) {
            model.addAttribute("discMag", discMag.get());
            return "edit-discmag";
        }
        return "redirect:/discmags";
    }

    @PostMapping("/edit")
    public String editDiscMag(@ModelAttribute DiscMag discMag) {
        discMagRepository.save(discMag);
        return "redirect:/discmags";
    }

    @PostMapping("/delete")
    public String deleteDiscMag(@RequestParam("id") Long id) {
        discMagRepository.deleteById(id);
        return "redirect:/discmags";
    }
}
