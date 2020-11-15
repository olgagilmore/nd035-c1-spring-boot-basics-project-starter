package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
//import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES")
    List<Note> getAllNotes();

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note getNoteByNoteId( Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getAllNotesForUserId(Integer userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insert(Note note);

    @Update("UPDATE NOTES set notetitle= #{notetitle}, notedescription = #{notedescription}, userid= #{userid} where noteid = #{noteid}")
    int update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{id}")
    void delete(Integer id);
}
