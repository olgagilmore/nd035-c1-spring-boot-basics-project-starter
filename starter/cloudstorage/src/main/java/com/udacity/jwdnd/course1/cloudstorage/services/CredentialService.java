package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService= encryptionService;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating CredentialService bean");
    }

    public void addCredential(Credential credential) {
        String key= this.encryptionService.generateKey();
        credential.setKey(key);
        credential.setPassword(this.encryptPassword(credential));
        this.credentialMapper.insert(credential);
    }

    public List<Credential> getAllCredentials() {
        return this.credentialMapper.getAllCredentials();
    }

    public List<Credential> getCredentialByUserId(Integer id) {
        return this.credentialMapper.getCredentialByUserId(id);
    }

    public Credential getCredentialByCredentialId(Integer id) {
        return this.credentialMapper.getCredentialbyCredentialId(id);
    }

    public void deleteCredential(Integer id) {
        this.credentialMapper.delete(id);
    }

    public String retrieveKeyByCredentialId(Integer id) {
        return this.credentialMapper.retrieveKeyByCredentialId(id);
    }

    public void editCredential(Credential credential) {
        //newly edited credential does not store key in it, will need to retrieve it first from DB
        String key = this.credentialMapper.retrieveKeyByCredentialId(credential.getCredentialid());
        String encPwd =this.encryptionService.encryptValue(credential.getPassword(), key);
        credential.setPassword(encPwd);
        this.credentialMapper.update(credential);
    }

    public String encryptPassword(Credential credential) {
        return this.encryptionService.encryptValue(credential.getPassword(), credential.getKey());}

    public String decryptPassword(Credential credential) {
        return this.encryptionService.decryptValue(credential.getPassword(), credential.getKey());}
}
