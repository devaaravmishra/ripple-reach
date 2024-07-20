package com.ripplereach.ripplereach.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

  @GetMapping("/actuator/health")
  public String redirectToHealth() {
    return "redirect:/actuator/health";
  }
}
