package csd230.lab2.controllers;

import csd230.lab2.entities.CartItem;
import csd230.lab2.respositories.CartItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemRepository cartItemRepository;

    public CartItemController(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @GetMapping
    public String listCartItems(Model model) {
        List<CartItem> cartItems = cartItemRepository.findAll();
        model.addAttribute("cartItems", cartItems);
        return "cart-items";
    }

    @GetMapping("/add")
    public String showAddCartItemForm(Model model) {
        model.addAttribute("cartItem", new CartItem());
        return "add-cart-item";
    }

    @PostMapping("/add")
    public String addCartItem(@ModelAttribute CartItem cartItem) {
        cartItemRepository.save(cartItem);
        return "redirect:/cart-items";
    }

    @GetMapping("/edit")
    public String showEditCartItemForm(@RequestParam("id") Long id, Model model) {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if (cartItem.isPresent()) {
            model.addAttribute("cartItem", cartItem.get());
            return "edit-cart-item";
        }
        return "redirect:/cart-items";
    }

    @PostMapping("/edit")
    public String editCartItem(@ModelAttribute CartItem cartItem) {
        cartItemRepository.save(cartItem);
        return "redirect:/cart-items";
    }

    @PostMapping("/delete")
    public String deleteCartItem(@RequestParam("id") Long id) {
        cartItemRepository.deleteById(id);
        return "redirect:/cart-items";
    }
}
