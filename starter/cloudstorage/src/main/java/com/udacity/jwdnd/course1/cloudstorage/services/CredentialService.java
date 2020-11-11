package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating CredentialService bean");
    }

    public void addCredential(Credential credential) {

        this.credentialMapper.insert(credential);
    }

    public List<Credential> getAllCredentials() {
        return this.credentialMapper.getAllCredentials();
    }

    public Credential getCredentialByUserId(Integer id) {
        return this.credentialMapper.getCredentialByUserId(id);
    }

    public void deleteCredential(Integer id) {
        this.credentialMapper.delete(id);
    }

    public void editCredential(Credential credential) {
        this.credentialMapper.update(credential);
    }
}
