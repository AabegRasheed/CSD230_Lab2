package csd230.lab2.controllers;

import csd230.lab2.entities.Magazine;
import csd230.lab2.respositories.MagazineRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/magazines")
public class MagazineController {

    private final MagazineRepository magazineRepository;

    public MagazineController(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }

    @GetMapping
    public String listMagazines(Model model) {
        List<Magazine> magazines = magazineRepository.findAll();
        model.addAttribute("magazines", magazines);
        return "magazines";
    }

    @GetMapping("/add-magazine")
    public String showAddMagazineForm(Model model) {
        model.addAttribute("magazine", new Magazine());
        return "add-magazine";
    }

    @PostMapping("/add-magazine")
    public String addMagazine(@ModelAttribute Magazine magazine) {
        magazineRepository.save(magazine);
        return "redirect:/magazines";
    }

    @GetMapping("/edit-magazine")
    public String showEditMagazineForm(@RequestParam("id") Long id, Model model) {
        Optional<Magazine> magazine = magazineRepository.findById(id);
        if (magazine.isPresent()) {
            model.addAttribute("magazine", magazine.get());
            return "edit-magazine";
        }
        return "redirect:/magazines";
    }

    @PostMapping("/edit-magazine")
    public String editMagazine(@ModelAttribute Magazine magazine) {
        magazineRepository.save(magazine);
        return "redirect:/magazines";
    }

    @PostMapping("/delete-magazine")
    public String deleteMagazine(@RequestParam("id") Long id) {
        magazineRepository.deleteById(id);
        return "redirect:/magazines";
    }
}
