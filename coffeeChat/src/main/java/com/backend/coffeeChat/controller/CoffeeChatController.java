package com.backend.coffeeChat.controller;

import com.backend.coffeeChat.dto.ApplyRequest;
import com.backend.coffeeChat.repository.ApplicationRepository;
import com.backend.coffeeChat.service.AlimtalkService;
import org.springframework.beans.factory.annotation.Value;
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

    private final String acceptBaseUrl;
    private final String adminPhone;
    private final String templateAdmin;
    private final String templateAccept;

    public CoffeeChatController(ApplicationRepository repository,
                                AlimtalkService alimtalkService,
                                @Value("${app.accept.base-url:https://your.server/accept}") String acceptBaseUrl,
                                @Value("${app.admin.phone:ADMIN_PHONE}") String adminPhone,
                                @Value("${app.template.admin:TEMPLATE_ADMIN}") String templateAdmin,
                                @Value("${app.template.accept:TEMPLATE_USER_ACCEPT}") String templateAccept) {
        this.repository = repository;
        this.alimtalkService = alimtalkService;
        this.acceptBaseUrl = acceptBaseUrl;
        this.adminPhone = adminPhone;
        this.templateAdmin = templateAdmin;
        this.templateAccept = templateAccept;
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
        vars.put("acceptUrl", acceptBaseUrl + "?id=" + saved.getId());
        alimtalkService.sendAlimtalk(adminPhone, templateAdmin, vars);
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
            alimtalkService.sendAlimtalk(request.getContact(), templateAccept, vars);
        }
        return "accept"; // a simple JSP message page (could be created)
    }
}
