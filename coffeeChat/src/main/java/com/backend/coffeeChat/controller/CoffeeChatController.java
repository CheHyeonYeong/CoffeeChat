package com.backend.coffeeChat.controller;

import com.backend.coffeeChat.dto.ApplyRequest;
import com.backend.coffeeChat.repository.ApplicationRepository;
import com.backend.coffeeChat.service.AlimtalkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CoffeeChatController {

    private final ApplicationRepository repository;
    private final AlimtalkService alimtalkService;

    public CoffeeChatController(ApplicationRepository repository, AlimtalkService alimtalkService) {
        this.repository = repository;
        this.alimtalkService = alimtalkService;
    }

    @GetMapping("/apply")
    public String applyForm(Model model) {
        model.addAttribute("applyRequest", new ApplyRequest());
        return "apply";
    }

    @PostMapping("/apply")
    public String apply(@ModelAttribute ApplyRequest applyRequest) {
        ApplyRequest saved = repository.save(applyRequest);
        // Send notification to admin with accept link
        Map<String, String> vars = new HashMap<>();
        vars.put("name", saved.getName());
        vars.put("time", saved.getPreferredTime());
        vars.put("topic", saved.getTopic());
        vars.put("acceptUrl", "https://your.server/accept?id=" + saved.getId());
        alimtalkService.sendAlimtalk("ADMIN_PHONE", "TEMPLATE_ADMIN", vars);
        return "redirect:/apply";
    }

    @GetMapping("/accept")
    public String accept(@RequestParam("id") Long id) {
        ApplyRequest request = repository.findById(id);
        if (request != null) {
            Map<String, String> vars = new HashMap<>();
            vars.put("name", request.getName());
            vars.put("time", request.getPreferredTime());
            vars.put("topic", request.getTopic());
            alimtalkService.sendAlimtalk(request.getContact(), "TEMPLATE_USER_ACCEPT", vars);
        }
        return "accept"; // a simple JSP message page (could be created)
    }
}
