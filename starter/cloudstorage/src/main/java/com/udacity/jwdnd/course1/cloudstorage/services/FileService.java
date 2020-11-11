package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int uploadFile(UserFile userFile) {
        return fileMapper.insert(userFile);
    }

    public List<UserFile> getAllFilesByUserId(Integer id) {
        return this.fileMapper.getAllFilesByUserId(id);
    }

    public UserFile getFileById(Integer id) {return this.fileMapper.getFileById(id);}

    public void deleteFile(Integer id) {
        this.fileMapper.delete(id);
    }

    public boolean isFilenameAvailable(String fileName, Integer userId) {
        return (this.fileMapper.getFile(fileName, userId)==null) ;
    }
 }
