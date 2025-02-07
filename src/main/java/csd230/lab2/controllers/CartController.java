package csd230.lab2.controllers;

import csd230.lab2.entities.Cart;
import csd230.lab2.entities.CartItem;
import csd230.lab2.entities.Book;
import csd230.lab2.respositories.CartItemRepository;
import csd230.lab2.respositories.CartRepository;
import csd230.lab2.respositories.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    public CartController(CartRepository cartRepository, CartItemRepository cartItemRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public String cart(Model model) {
        List<CartItem> cartItems = cartItemRepository.findAll();
        model.addAttribute("cartItems", cartItems);
        return "bu/cart";
    }


    @PostMapping("/add")
    public String addToCart(@RequestParam(value = "selectedBooks", required = false) List<Long> bookIds) {
        if (bookIds == null || bookIds.isEmpty()) {
            return "redirect:/books";
        }

        for (Long bookId : bookIds) {
            Optional<Book> book = bookRepository.findById(bookId);
            if (book.isPresent()) {
                Book selectedBook = book.get();

                boolean exists = cartItemRepository.findAll().stream()
                        .anyMatch(item -> item.getDescription().equals("Book: " + selectedBook.getTitle()));

                if (!exists) {
                    CartItem cartItem = new CartItem(
                            selectedBook.getPrice(),
                            1,
                            "Book: " + selectedBook.getTitle()
                    );
                    cartItemRepository.save(cartItem);
                }
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam("cartItemIds") List<Long> cartItemIds,
                             @RequestParam("quantities") List<Integer> quantities) {
        for (int i = 0; i < cartItemIds.size(); i++) {
            Long itemId = cartItemIds.get(i);
            int newQuantity = quantities.get(i);

            Optional<CartItem> cartItemOptional = cartItemRepository.findById(itemId);
            if (cartItemOptional.isPresent()) {
                CartItem cartItem = cartItemOptional.get();
                cartItem.setQuantity(newQuantity);
                cartItemRepository.save(cartItem);
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove-cart")
    public String removeCartItem(@RequestParam("cartItemId") Long id) {
        cartItemRepository.deleteById(id);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        List<CartItem> purchasedItems = cartItemRepository.findAll();
        double totalAmount = purchasedItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("purchasedItems", purchasedItems);
        model.addAttribute("totalAmount", totalAmount);

        return "bu/checkout";
    }

    @PostMapping("/clear")
    public String clearCart() {
        cartItemRepository.deleteAll();
        return "redirect:/cart";
    }

}
