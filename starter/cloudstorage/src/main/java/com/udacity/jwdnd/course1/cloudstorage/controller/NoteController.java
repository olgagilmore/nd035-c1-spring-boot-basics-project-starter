package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @ModelAttribute("note")
    public Note getNote() {
        return new Note();
    }

    @PostMapping("/note-add")
    public String createOrUpdateNote(Authentication auth, @ModelAttribute(value="newNote") Note note, Model model) {
        //String username = authentication.getName();
        //int userId = userService.getUserId(username);
        User user = userService.getUser(auth.getPrincipal().toString());
        note.setUserid(user.getUserId());

        if (note.getNoteid() != null) {
            try{
                noteService.editNote(note);
                model.addAttribute("successMessage", "Your note was updated successful.");
                return "result";
            }catch (Exception e){
                model.addAttribute("errorMessage", "Something went wrong with the note update. Please try again!");
                return "result";
            }
        }
        else{
                try{
                    noteService.createNote(note);
                    model.addAttribute("successMessage", "Your note was created successful.");
                return "result";
            }catch (Exception e){
                    model.addAttribute("errorMessage", "Something went wrong with the note creation. Please try again!");
                return "result";
            }
        }
    }

    @GetMapping("/note-delete/{noteId}")
    public String deleteNote(Authentication auth, @PathVariable(value = "noteId") Integer noteId, Model model) {
        this.noteService.deleteNote(noteId);
        User user = userService.getUser(auth.getPrincipal().toString());
        //Users userDb = userService.getUser(auth.getName());
        model.addAttribute("notes", this.noteService.getAllNotesForUserId(user.getUserId()));
        return "redirect:/home";
    }
    @GetMapping("/note-get/{noteId}")
    public String getNote(Authentication auth, @PathVariable(value = "noteId") Integer noteId, Model model) {
        this.noteService.getNoteByNoteId(noteId);
        //Users userDb = userService.getUser(auth.getName());
        User user = userService.getUser(auth.getPrincipal().toString());
        model.addAttribute("notes", this.noteService.getAllNotesForUserId(user.getUserId()));
        return "redirect:/home";
    }
}