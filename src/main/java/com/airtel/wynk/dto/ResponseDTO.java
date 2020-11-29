package com.airtel.wynk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {

    private String song;
    private String artist;
    private Long count;
    Set<String> songs;
    private String status;
    private String message;

    public ResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }


    public ResponseDTO(String artist, Long count, String song, Set<String> songs) {
        this.artist = artist;
        this.count = count;
        this.song = song;
        this.songs = songs;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Set<String> getSongs() {
        return songs;
    }

    public void setSongs(Set<String> songs) {
        this.songs = songs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
