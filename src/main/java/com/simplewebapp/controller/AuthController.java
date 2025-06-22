package com.simplewebapp.controller;

import com.simplewebapp.service.UserService;
import com.simplewebapp.aop.LogEntryExit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @LogEntryExit
    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        Object error = request.getSession().getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error.toString());
            request.getSession().removeAttribute("error");
        }
        return "login";
    }
    
    @LogEntryExit
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @LogEntryExit
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String email,
                              RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(username, password, email);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @LogEntryExit
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @LogEntryExit
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        boolean result = userService.initiatePasswordReset(email);
        if (result) {
            redirectAttributes.addFlashAttribute("success", "Password reset link sent to your email if it exists in our system.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No user found with this email address.");
        }
        return "redirect:/forgot-password";
    }

    @LogEntryExit
    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @LogEntryExit
    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam String token, @RequestParam String password, RedirectAttributes redirectAttributes) {
        boolean result = userService.resetPassword(token, password);
        if (result) {
            redirectAttributes.addFlashAttribute("success", "Password reset successful. Please login.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired token.");
            return "redirect:/reset-password?token=" + token;
        }
    }
} 