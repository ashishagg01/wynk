package com.airtel.wynk.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @NotBlank
    private String id;

    public Artist() {
    }

    public Artist(@NotBlank String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
