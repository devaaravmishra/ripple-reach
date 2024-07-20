package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.constants.Messages;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

  @GetMapping("/")
  public String welcome() {
    return Messages.WELCOME_MESSAGE;
  }
}
