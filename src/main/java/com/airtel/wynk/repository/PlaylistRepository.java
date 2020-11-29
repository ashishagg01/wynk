package com.airtel.wynk.repository;

import com.airtel.wynk.entity.Artist;
import com.airtel.wynk.entity.Playlist;
import com.airtel.wynk.entity.Song;
import com.airtel.wynk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {


    @Query(value = "select distinct song_id from playlist where artist_id = ? and song_id is not null", nativeQuery = true)
    List<String> findSongsOfAnArtist(String artist);

    @Query(value = "select distinct song_id from playlist where user_id = ? and song_id is not null", nativeQuery = true)
    Set<String> getPlaylistOfAnUser(String user);

    int countAllByArtistAndSongAndUser(Artist artist, Song song, User user);

    @Query(value = "select distinct user_id from playlist where artist_id = ? and user_id is not null", nativeQuery = true)
    List<String> findFollowersAnArtist(String artist);

    Playlist findTopByArtistAndSongAndUser(Artist artist, Song song, User user);

    @Query(value = "select artist_id from (SELECT artist_id,count(*) as count FROM playlist where user_id is not null group by artist_id) order by count desc limit 1", nativeQuery = true)
    String findMostPopularArtist();

    @Query(value = "SELECT count(distinct user_id) FROM playlist where artist_id = ? and user_id is not null", nativeQuery = true)
    long countFollowers(String artist);

    @Query(value = "select song_id from (SELECT song_id,count(*) as count FROM playlist group by song_id) order by count desc limit 1", nativeQuery = true)
    String findMostPopularSong();

    @Modifying
    @Transactional
    @Query(value = "delete from playlist where user_id = ?1 and artist_id in (?2) and song_id is null", nativeQuery = true)
    void deleteFromPlaylist(String user, List<String> artist);

    @Modifying
    @Transactional
    @Query(value = "update playlist set user_id = null where user_id = ?1 and artist_id in (?2) and song_id is not null", nativeQuery = true)
    void updatePlaylist(String user, List<String> artist);
}
