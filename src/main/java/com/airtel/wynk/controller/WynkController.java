package com.airtel.wynk.controller;

import com.airtel.wynk.co.FollowCO;
import com.airtel.wynk.co.PublishCO;
import com.airtel.wynk.dto.ResponseDTO;
import com.airtel.wynk.entity.Artist;
import com.airtel.wynk.entity.Playlist;
import com.airtel.wynk.entity.Song;
import com.airtel.wynk.entity.User;
import com.airtel.wynk.repository.ArtistRepository;
import com.airtel.wynk.repository.PlaylistRepository;
import com.airtel.wynk.repository.SongRepository;
import com.airtel.wynk.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Api(value = "wynk")
@RestController
@RequestMapping("/wynk")
public class WynkController {

    private static final Logger logger=LoggerFactory.getLogger(WynkController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    private static final String M1 = "%s started following %s";

    private static final String M2 = "%s stopped following %s";

    private static final String M3 = "song published against artists";

    @ApiOperation(value = "Start follow one or more Artists",response = ResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "<user> started following <artist1> and <artist2>"),
            @ApiResponse(code = 400, message = "invalid input parameter")
    })
    @PostMapping("/follow")
    public ResponseEntity<ResponseDTO> followArtist(@RequestBody @Valid FollowCO followCO) {
        //step1 : create artist
        //step2 : create user
        //step3 : create mapping for user and artists
        User ur = new User(followCO.getUser());
        userRepository.save(ur);
        StringJoiner sj = new StringJoiner(" and ");
        Set<Playlist> playlists = new HashSet<>();
        followCO.getArtist().stream().forEach(a->{
            Artist ar = new Artist(a);
            artistRepository.save(ar);
            List<String> songs =  playlistRepository.findSongsOfAnArtist(a);
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
        updatePlaylist(playlists);
        String response = String.format(M1,followCO.getUser(),sj.toString());
        logger.info(response);
        return ResponseEntity.ok(new ResponseDTO("ok",response));
    }

    @ApiOperation(value = "Start unfollow one or more Artists",response = ResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "<user> stopped following <artist1> and <artist2>"),
            @ApiResponse(code = 400, message = "invalid input parameter")
    })
    @PostMapping("/unfollow")
    public ResponseEntity<ResponseDTO> unfollowArtist(@RequestBody @Valid FollowCO followCO) {
        //step1 : unfollow artists
        //step2 : remove exclusive songs from the playlist of the unfollowed artists
        StringJoiner ar = new StringJoiner(" and ");
        followCO.getArtist().stream().forEach(a->ar.add(a));
        playlistRepository.deleteFromPlaylist(followCO.getUser(),followCO.getArtist());
        playlistRepository.updatePlaylist(followCO.getUser(),followCO.getArtist());
        String response = String.format(M2,followCO.getUser(),ar.toString());
        logger.info(response);
        return ResponseEntity.ok(new ResponseDTO("ok",response));
    }

    @ApiOperation(value = "Publish a song for one or more Artists",response = ResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "song published against artists"),
            @ApiResponse(code = 400, message = "invalid input parameter")
    })
    @PostMapping("/publish")
    public ResponseEntity<ResponseDTO> publishSong(@RequestBody @Valid PublishCO publishCO) {
        //step1 : create artists if not exists
        //step2 : create song if not exists
        //step3 : create mapping for artist and song
        //step4 : create playlist for user
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
        updatePlaylist(playlists);
        if(logger.isInfoEnabled())
            logger.info("{} "+M3+" {}",publishCO.getSong(),publishCO.getArtist().toString());
        return ResponseEntity.ok(new ResponseDTO("ok",String.format(M3)));
    }

    private void updatePlaylist(Set<Playlist> playlists) {
        playlists.stream().forEach(p->{
            if(playlistRepository.countAllByArtistAndSongAndUser(p.getArtist(),p.getSong(),p.getUser())==0)
                playlistRepository.save(p);
        });
    }

    @ApiOperation(value = "Fetch playlist of an user",response = ResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "invalid input parameter")
    })
    @GetMapping("/playlist")
    public ResponseEntity<ResponseDTO> getPlaylist(@Valid @NotBlank @RequestParam("user") String user) {
        return ResponseEntity.ok(new ResponseDTO(null,null,null,playlistRepository.getPlaylistOfAnUser(user)));
    }

    @ApiOperation(value = "Fetch most popular song",response = ResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "invalid input parameter")
    })
    @GetMapping("/popular/song")
    public ResponseEntity<ResponseDTO> getPopularSong() {
        return ResponseEntity.ok(new ResponseDTO(null,null,playlistRepository.findMostPopularSong(),null));
    }

    @ApiOperation(value = "Fetch most popular artist",response = ResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "invalid input parameter")
    })
    @GetMapping("/popular/artist")
    public ResponseEntity<ResponseDTO> getPopularArtist() {
            return ResponseEntity.ok(new ResponseDTO(playlistRepository.findMostPopularArtist(),null,null,null));
    }

    @ApiOperation(value = "Count of followers of an artist",response = ResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "invalid input parameter")
    })
    @GetMapping("/follow/count")
    public ResponseEntity<ResponseDTO> getFollowersCount(@Valid @NotBlank @RequestParam("artist") String artist) {
        return ResponseEntity.ok(new ResponseDTO(artist,playlistRepository.countFollowers(artist),null,null));
    }
}
