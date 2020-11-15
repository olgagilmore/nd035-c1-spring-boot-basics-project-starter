package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS")
    List<Credential> getAllCredentials();

    @Select("SELECT * FROM CREDENTIALS WHERE url = #{url} and userid = #{userId}")
    List<Credential> getCredential(String url, Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{id}")
    Credential getCredentialbyCredentialId(Integer Id);

    @Select("SELECT key FROM CREDENTIALS WHERE credentialid = #{id}")
    String retrieveKeyByCredentialId(Integer Id);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getCredentialByUserId(Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insert(Credential credential);

    @Update("UPDATE CREDENTIALS set url= #{url}, username = #{username}, password = #{password} where credentialid = #{credentialid}")
    int update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    void delete(Integer credentialid);
}
