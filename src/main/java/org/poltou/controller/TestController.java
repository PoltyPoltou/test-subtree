package org.poltou.controller;

import org.poltou.service.UserOpeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @Autowired
    UserOpeningService service;

    @GetMapping("/test")
    public void pog() {
        service.importChessCom("poltypoltou", "black");
    }
}
