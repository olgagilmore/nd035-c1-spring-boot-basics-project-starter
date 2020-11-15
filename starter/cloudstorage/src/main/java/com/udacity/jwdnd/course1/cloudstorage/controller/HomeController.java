package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;

    public HomeController(NoteService noteService, FileService fileService, CredentialService credentialService,
                          UserService userService) {
        this.noteService = noteService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String listUploadedFiles(Authentication auth, Model model) throws IOException {
        User user = userService.getUser(auth.getPrincipal().toString());
        Integer userId= user.getUserId();
        model.addAttribute("files", this.fileService.getAllFilesByUserId(userId));
        model.addAttribute("credentials", this.credentialService.getCredentialByUserId(userId));
        model.addAttribute("notes", this.noteService.getAllNotesForUserId(userId));
        model.addAttribute("newNote", new Note());
        model.addAttribute("newCredential", new Credential());

        model.addAttribute("credentialService", credentialService);

        /*model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));*/

        return "home";
    }

    @PostMapping("/logout")
    public String logout(){
        return "login?logout";
    }
    @GetMapping("/logout")
    public String logoutView(){
        return "redirect:/login?logout";
    }

    @PostMapping("/file-upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication auth, Model model,
                             RedirectAttributes redirectAttributes) {
        //User user = (User) auth.getDetails();
        User user = userService.getUser(auth.getPrincipal().toString());
        model.addAttribute("activeTab", "files");

        model.addAttribute("errorMessage", false);
        model.addAttribute("successMessage", false);
        if (fileUpload.getOriginalFilename().isEmpty()) {
            //
            model.addAttribute("errorMessage", "Please select a file to upload");
            //setLists(model);
            //return "result";
            return "result";
        }
        if (!fileService.isFilenameAvailable(fileUpload.getOriginalFilename(), user.getUserId())) {
            //model.addAttribute
            //redirectAttributes.
            model.addAttribute("errorMessage", "File with the same filename already exists!");
            //setLists(model);
            return "result";
            //return "result";
        }

        try {
            //String filename, String contenttype, String filesize, Integer userid, byte[] filedata
            fileService.uploadFile( new File(fileUpload.getOriginalFilename(),fileUpload.getContentType(),
                                    fileUpload.getSize() + "", user.getUserId(),fileUpload.getBytes()) );

            // reload files list
            List<File> files = fileService.getAllFilesByUserId(user.getUserId());
            //model.addAttribute("fileUploadSuccess", "File uploaded successfully!");
            model.addAttribute("files", files);
            //redirectAttributes.addFlashAttribute("successMessage", "File saved successfully");
            model.addAttribute("successMessage", "File saved successfully");
            //return "redirect:/home/result";
            //return "result";
        } catch (IOException e) {
            e.printStackTrace();
            //model.addAttribute  redirectAttributes.addFlashAttribute
            model.addAttribute("errorMessage", e.getMessage());


        }
        //setLists(model);
        //return "home";
        return "result";
    }

    @GetMapping("/file-delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Authentication authentication, Model model) {
        User user = userService.getUser(authentication.getPrincipal().toString());
        model.addAttribute("errorMessage", false);
        model.addAttribute("successMessage", false);
        try {
            fileService.deleteFile(fileId);

            // reload files list
            List<File> files = fileService.getAllFilesByUserId(user.getUserId());
            model.addAttribute("successMessage", "File is deleted successfully!");
            //model.addAttribute("files", files);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        //setLists(model);
        return "result";
    }

    @GetMapping("/file-view/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable Integer fileId, Authentication authentication)  {
        //add some code here
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype())).header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

    /*@PostMapping("/credential-delete")
    public String deleteCredential(@RequestParam Integer credentialId, Authentication auth, Model model) {
        try {
            credentialService.deleteCredential(credentialId);
            //User user = (User) auth.getDetails();
            User user = userService.getUser(auth.getPrincipal().toString());
            List<Credential> credentials = credentialService.getCredentialByUserId(user.getUserId());
            model.addAttribute("activeTab", "credentials");
            model.addAttribute("credentials", "credentials");
            model.addAttribute("credentialsMessage", "Credential deleted!");
        } catch (Exception e) {
            model.addAttribute("credentialsError", e.getMessage());
        }
        //setLists(model);
        return "home";
    }*/

}
