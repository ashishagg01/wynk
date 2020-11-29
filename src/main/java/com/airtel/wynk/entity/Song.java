package com.airtel.wynk.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "song")
public class Song {

    @Id
    @NotBlank
    private String id;

    public Song() {
    }

    public Song(@NotBlank String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
