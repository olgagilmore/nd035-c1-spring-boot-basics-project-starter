package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int uploadFile(File file) {
        return fileMapper.insert(file);
    }

    public List<File> getAllFilesByUserId(Integer id) {
        return this.fileMapper.getAllFilesByUserId(id);
    }

    public File getFileById(Integer id) {return this.fileMapper.getFileById(id);}

    public void deleteFile(Integer id) {
        this.fileMapper.delete(id);
    }

    public boolean isFilenameAvailable(String fileName, Integer userId) {
        return (this.fileMapper.getFile(fileName, userId)==null) ;
    }
 }
