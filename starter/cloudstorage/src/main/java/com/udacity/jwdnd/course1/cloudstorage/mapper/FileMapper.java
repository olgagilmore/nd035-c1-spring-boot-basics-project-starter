package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getAllFilesByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} and userid = #{userId}")
    File getFile(String filename, Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
                    "VALUES(#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insert(File file);

    @Update("UPDATE FILES set filename= #{filename}, contenttype = #{contenttype}, filesize= #{filesize}, userId= #{userId}, filedata= #{filedata} where fileId = #{fileId}")
    Integer update(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void delete(Integer fileId);
}
