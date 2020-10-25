package com.exam.demo.web.controller;

import com.exam.demo.model.Card;
import com.exam.demo.model.User;
import com.exam.demo.repository.*;
import com.exam.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {


    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @GetMapping("/")
    public String root(Model model) {
        List<User> users = userRepository.findAll() ;
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println(this.curentuser().toString());
        System.out.println(this.getCurrentUserId());
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(users.toString());

        model.addAttribute("users", users);
        model.addAttribute("cards", cardRepository.findAll());

        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }


    @GetMapping("/user")
    public String userIndex() {
        return "userindex";
    }

   ////
   @GetMapping("/user/partage/{id}")
   public String cardpartagecard(@PathVariable("id") Optional<Long> id, Model model) {
       model.addAttribute("cards", cardRepository.findAll());
       model.addAttribute("users", userRepository.findAll());
       model.addAttribute("card", cardRepository.findById(id.get()));
       return "card";
   }

    /// CARD MAPPING

    @GetMapping("/card/card_list")
    private String getAll(Model model){
        model.addAttribute("cards", cardRepository.findAll());
        return "index";
    }

    @GetMapping("/card/add")
    public String cardadd(Model model) {
        model.addAttribute("cards", cardRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("card", new Card());
        return "card";
    }
    @GetMapping("/card/edit/{id}")
    public String cardedit(@PathVariable("id") Optional<Long> id, Model model) {
        model.addAttribute("cards", cardRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("card", cardRepository.findById(id.get()));
        return "card";
    }
    @PostMapping("/card/addEdit")
    private String insertOrUpdate(Card card, Model model){
        if(card.getId() == null){
           // User u =
            cardRepository.save(card);
        }else{
            Optional<Card> cardOptional = cardRepository.findById(card.getId());
            if(cardOptional.isPresent()){
                Card editCard = cardOptional.get();
                editCard.setCompanyname(card.getCompanyname());
                editCard.setName(card.getName());
                editCard.setEmail(card.getEmail());
                editCard.setTelephone(card.getTelephone());
                cardRepository.save(editCard);
            }
        }

        List<User> users = userRepository.findAll() ;
        System.out.println(users.toString());

        model.addAttribute("users", users);
        model.addAttribute("cards", cardRepository.findAll());

        return "index";
    }

    @GetMapping("/card/delete/{id}")
    private String deleteMovie(@PathVariable("id") Long id ,Model model){
        Optional<Card> card = cardRepository.findById(id);
        if(card.isPresent()){
            cardRepository.delete(card.get());
        }else{
            System.err.println("not found");
        }

        List<User> users = userRepository.findAll() ;
        System.out.println(users.toString());

        model.addAttribute("users", users);
        model.addAttribute("cards", cardRepository.findAll());

        return "index";
    }
     private Object curentuser(){
         Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

         if (principal instanceof UserDetails) {
             String username = ((UserDetails)principal).getUsername();
         } else {
             String username = principal.toString();
         }
         return   principal;
     }

    private Long getCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String id = null;
        if (authentication != null)
            if (authentication.getPrincipal() instanceof UserDetails)
                id = ((UserDetails) authentication.getPrincipal()).getUsername();
            else if (authentication.getPrincipal() instanceof String)
                id = (String) authentication.getPrincipal();
        try {
            return Long.valueOf(id != null ? id : "1"); //anonymoususer
        } catch (NumberFormatException e) {
            return 1L;
        }
    }
}
