package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {
    private final CredentialService credentialService;
    private final UserService userService;
    public CredentialController(CredentialService credentialService, UserService userService) {
        System.out.println("CredentialService is created");
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @ModelAttribute("credential")
    public Credential getCredential() {
        return new Credential();
    }

    @PostMapping("/credential-add")
    public String createOrUpdateCredential(Authentication auth, Credential credential, Model model) {
        //String username = authentication.getName();
        //int userId = userService.getUserId(username);
        User user = userService.getUser(auth.getPrincipal().toString());
        credential.setUserid(user.getUserId());

        if (credential.getCredentialid() != null) {
            try{
                credentialService.editCredential(credential);
                model.addAttribute("successMessage", "Your credential was updated successful.");
                return "result";
            }catch (Exception e){
                model.addAttribute("errorMessage", "Something went wrong with the credential update. Please try again!");
                return "result";
            }
        }
        else{
            try{
                credentialService.addCredential(credential);
                model.addAttribute("successMessage", "Your credential was created successful.");
                return "result";
            }catch (Exception e){
                model.addAttribute("errorMessage", "Something went wrong with the credential creation. Please try again!");
                return "result";
            }
        }
    }

    @GetMapping("/credential-delete/{credentialId}")
    public String deleteCredential(Authentication auth, @PathVariable(value = "credentialId") Integer credentialId, Model model) {
        this.credentialService.deleteCredential(credentialId);
        User user = userService.getUser(auth.getPrincipal().toString());
        //Users userDb = userService.getUser(auth.getName());
        //model.addAttribute("credentials", this.credentialService.getCredentialByUserId(user.getUserId()));
        return "home";
    }
    @GetMapping("/credential-get/{credentialId}")
    public String getcredential(Authentication auth, @PathVariable(value = "credentialId") Integer credentialId, Model model) {
        this.credentialService.getCredentialByUserId(credentialId);
        //Users userDb = userService.getUser(auth.getName());
        User user = userService.getUser(auth.getPrincipal().toString());
        //model.addAttribute("credentials", this.credentialService.getCredentialByUserId(user.getUserId()));
        return "redirect:/home";
    }
}