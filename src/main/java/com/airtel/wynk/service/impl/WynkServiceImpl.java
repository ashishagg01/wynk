package com.airtel.wynk.service.impl;

import com.airtel.wynk.co.FollowCO;
import com.airtel.wynk.co.PublishCO;
import com.airtel.wynk.entity.Artist;
import com.airtel.wynk.entity.Playlist;
import com.airtel.wynk.entity.Song;
import com.airtel.wynk.entity.User;
import com.airtel.wynk.repository.ArtistRepository;
import com.airtel.wynk.repository.PlaylistRepository;
import com.airtel.wynk.repository.SongRepository;
import com.airtel.wynk.repository.UserRepository;
import com.airtel.wynk.service.WynkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Service
public class WynkServiceImpl implements WynkService {

    private static final Logger logger=LoggerFactory.getLogger(WynkServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    private static final String M1 = "%s started following %s";

    private static final String M2 = "%s stopped following %s";

    private static final String M3 = "song published against artists";

    @Override
    public String followArtist(FollowCO followCO, PlaylistRepository playlistRepository) {
        User ur = new User(followCO.getUser());
        userRepository.save(ur);
        StringJoiner sj = new StringJoiner(" and ");
        Set<Playlist> playlists = new HashSet<>();
        followCO.getArtist().stream().forEach(a->{
            Artist ar = new Artist(a);
            artistRepository.save(ar);
            List<String> songs = playlistRepository.findSongsOfAnArtist(a);
            if(songs != null && !songs.isEmpty()){
                songs.stream().forEach(s->{
                    Playlist playlist = playlistRepository.findTopByArtistAndSongAndUser(ar,new Song(s),null);
                    if(playlist==null)
                        playlists.add(new Playlist(ar,ur,new Song(s)));
                    else{
                        playlist.setUser(ur);playlists.add(playlist);}
                });
            }else{
                playlists.add(new Playlist(ar,ur,null));
            }
            sj.add(a);
        });
        updatePlaylist(playlists,playlistRepository);
        String response = String.format(M1,followCO.getUser(),sj.toString());
        logger.info(response);
        return response;
    }

    @Override
    public String unfollowArtist(FollowCO followCO, PlaylistRepository playlistRepository) {
        StringJoiner ar = new StringJoiner(" and ");
        followCO.getArtist().stream().forEach(a->ar.add(a));
        playlistRepository.deleteFromPlaylist(followCO.getUser(),followCO.getArtist());
        playlistRepository.updatePlaylist(followCO.getUser(),followCO.getArtist());
        String response = String.format(M2,followCO.getUser(),ar.toString());
        logger.info(response);
        return response;
    }

    @Override
    public String publishSong(PublishCO publishCO, PlaylistRepository playlistRepository) {
        Song so = new Song(publishCO.getSong());
        songRepository.save(so);
        Set<Playlist> playlists = new HashSet<>();
        publishCO.getArtist().stream().forEach(a->{
            Artist ar = new Artist(a);
            artistRepository.save(ar);
            List<String> users =  playlistRepository.findFollowersAnArtist(a);
            if(users != null && !users.isEmpty()){
                users.stream().forEach(u->{
                    Playlist playlist = playlistRepository.findTopByArtistAndSongAndUser(ar,null,new User(u));
                    if(playlist==null)
                        playlists.add(new Playlist(ar,new User(u),so));
                    else{
                        playlist.setSong(so);playlists.add(playlist);}
                });
            }else{
                playlists.add(new Playlist(ar,null,so));
            }
        });
        updatePlaylist(playlists, playlistRepository);
        if(logger.isInfoEnabled())
            logger.info("{} "+M3+" {}",publishCO.getSong(),publishCO.getArtist().toString());
        return M3;
    }

    private void updatePlaylist(Set<Playlist> playlists, PlaylistRepository playlistRepository) {
        playlists.stream().forEach(p->{
            if(playlistRepository.countAllByArtistAndSongAndUser(p.getArtist(),p.getSong(),p.getUser())==0)
                playlistRepository.save(p);
        });
    }
}
