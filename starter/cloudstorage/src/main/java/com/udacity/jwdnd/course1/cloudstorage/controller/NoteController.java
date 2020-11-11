package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;
    public NoteController (NoteService noteService, UserService userService) {
        System.out.println("NoteService is created");
        this.noteService = noteService;
        this.userService = userService;
    }
    /*@PostMapping("/add-note1")
    public String postView(Authentication auth, @RequestParam("noteId") Integer noteId,
                           @RequestParam("noteTitle") String noteTitle,
                           @RequestParam("noteDescription") String noteDescription, Model model)   {
        Users userDb = userService.getUser(auth.getName());
        System.out.println("NOTE ID:" + noteId);
        System.out.println("NOTE TITLE:" + noteTitle);
        System.out.println("NOTE DESCRIPTION:" + noteDescription);
        System.out.println("user id:" + userDb.getUserId());
        if(noteId == 0){
            noteService.uploadNote(noteTitle, noteDescription, userDb.getUserId());
        } else{
            noteService.updateNote(noteTitle, noteDescription, noteId);
        }
        model.addAttribute("notes", this.noteService.getNotes(userDb.getUserId()));
        return "redirect:/home";
    }*/

    @PostMapping("/add-note")
    public String createOrUpdateNote(Authentication auth, Note note, RedirectAttributes redirectAttributes) {
        //String username = authentication.getName();
        //int userId = userService.getUserId(username);
        User user = userService.getUser(auth.getPrincipal().toString());
        note.setUserId(user.getUserId());

        if (note.getNoteId() != null) {
            try{
                noteService.editNote(note);
                redirectAttributes.addFlashAttribute("successMessage", "Your note was updated successful.");
                return "redirect:/home/result";
            }catch (Exception e){
                redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the note update. Please try again!");
                return "redirect:/home/result";
            }
        }
        else{
            try{
                noteService.createNote(note);
                redirectAttributes.addFlashAttribute("successMessage", "Your note was created successful.");
                return "redirect:/home/result";
            }catch (Exception e){
                redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the note creation. Please try again!");
                return "redirect:/home/result";
            }
        }
    }

    @GetMapping("/delete-note/{noteId}")
    public String deleteNote(Authentication auth, @PathVariable(value = "noteId") Integer noteId, Model model) {
        this.noteService.deleteNote(noteId);
        User user = userService.getUser(auth.getPrincipal().toString());
        //Users userDb = userService.getUser(auth.getName());
        model.addAttribute("notes", this.noteService.getAllNotesForUserId(user.getUserId()));
        return "redirect:/home";
    }
    @GetMapping("/get-note/{noteId}")
    public String getNote(Authentication auth, @PathVariable(value = "noteId") Integer noteId, Model model) {
        this.noteService.getNoteByNoteId(noteId);
        //Users userDb = userService.getUser(auth.getName());
        User user = userService.getUser(auth.getPrincipal().toString());
        model.addAttribute("notes", this.noteService.getAllNotesForUserId(user.getUserId()));
        return "redirect:/home";
    }
}