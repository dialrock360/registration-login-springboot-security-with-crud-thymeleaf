package com.exam.demo.web.controller;

import com.exam.demo.model.Card;
import com.exam.demo.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class MainController {

    @GetMapping("/")
    public String root(Model model) {

        model.addAttribute("cards", repository.findAll());

        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }


    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }



    /// CARD MAPPING

    @Autowired
    private CardRepository repository;

    @GetMapping("/card/card_list")
    private String getAll(Model model){
        model.addAttribute("cards", repository.findAll());
        return "index";
    }

    @GetMapping("/card/add")
    public String cardadd(Model model) {
        model.addAttribute("card", new Card());
        return "card";
    }
    @GetMapping("/card/edit/{id}")
    public String cardedit(@PathVariable("id") Optional<Long> id, Model model) {
        model.addAttribute("card", repository.findById(id.get()));
        return "card";
    }
    @PostMapping("/card/addEdit")
    private String insertOrUpdate(Card card){
        if(card.getId() == null){
            repository.save(card);
        }else{
            Optional<Card> cardOptional = repository.findById(card.getId());
            if(cardOptional.isPresent()){
                Card editCard = cardOptional.get();
                editCard.setEmail(card.getEmail());
                editCard.setName(card.getName());
                editCard.setTelephone(card.getTelephone());
                editCard.setCompanyname(card.getCompanyname());
                repository.save(editCard);
            }
        }
        return "redirect:/index";
    }

    @GetMapping("/card/delete/{id}")
    private String deleteMovie(@PathVariable("id") Long id){
        Optional<Card> card = repository.findById(id);
        if(card.isPresent()){
            repository.delete(card.get());
        }else{
            System.err.println("not found");
        }
        return "redirect:/index";
    }

}
