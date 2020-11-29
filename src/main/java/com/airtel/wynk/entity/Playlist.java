package com.airtel.wynk.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    private Artist artist;

    @ManyToOne(fetch=FetchType.LAZY)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    private Song song;

    public Playlist() {
    }

    public Playlist(Artist artist, User user, Song song) {
        this.artist = artist;
        this.user = user;
        this.song = song;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(artist, playlist.artist) &&
                Objects.equals(user, playlist.user) &&
                Objects.equals(song, playlist.song);
    }

    @Override
    public int hashCode() {

        return Objects.hash(artist, user, song);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
