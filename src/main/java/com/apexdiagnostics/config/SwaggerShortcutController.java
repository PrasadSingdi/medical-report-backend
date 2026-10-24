package com.apexdiagnostics.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SwaggerShortcutController {
    @GetMapping("/swagger-ui.index.html")
    public RedirectView swaggerUi() {
        return new RedirectView("/apex-diagnostics/swagger-ui/index.html");
    }
}
