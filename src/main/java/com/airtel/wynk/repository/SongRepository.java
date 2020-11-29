package com.airtel.wynk.repository;

import com.airtel.wynk.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SongRepository extends JpaRepository<Song, String> {

}
