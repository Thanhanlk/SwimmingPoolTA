package com.swimmingpool.pool;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/pool")
public class AdminPoolController {

    @GetMapping
    private String index() {
        return "admin/pages/pool/index";
    }
}
