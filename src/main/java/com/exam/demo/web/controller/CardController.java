package com.exam.demo.web.controller;

import com.exam.demo.model.Card;
import com.exam.demo.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/card")
public class CardController {
    @Autowired
    private CardRepository repository;

    @GetMapping("/index")
    private String getAll(Model model){
        model.addAttribute("cards", repository.findAll());
        return "index";
    }

    @GetMapping(path = {"/add", "edit/{id}"})
    private String addForm(@PathVariable("id") Optional<Long> id, Model model){
        if(id.isPresent()){
            model.addAttribute("card", repository.findById(id.get()));
        }else{
            model.addAttribute("card", new Card());
        }
        return "card";
    }

    @PostMapping("/addEdit")
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
        return "redirect:/card/index";
    }

    @GetMapping("/delete/{id}")
    private String deleteMovie(@PathVariable("id") Long id){
        Optional<Card> card = repository.findById(id);
        if(card.isPresent()){
            repository.delete(card.get());
        }else{
            System.err.println("not found");
        }
        return "redirect:/card/index";
    }

}