package com.airtel.wynk.controller;

import com.airtel.wynk.co.FollowCO;
import com.airtel.wynk.co.PublishCO;
import com.airtel.wynk.dto.ResponseDTO;
import com.airtel.wynk.repository.PlaylistRepository;
import com.airtel.wynk.service.WynkService;
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

@Api(value = "wynk")
@RestController
@RequestMapping("/wynk")
public class WynkController {

    private static final Logger logger=LoggerFactory.getLogger(WynkController.class);

    @Autowired
    private WynkService wynkService;

    @Autowired
    private PlaylistRepository playlistRepository;

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
        return ResponseEntity.ok(new ResponseDTO("ok",wynkService.followArtist(followCO,playlistRepository)));
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
        return ResponseEntity.ok(new ResponseDTO("ok",wynkService.unfollowArtist(followCO,playlistRepository)));
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
        return ResponseEntity.ok(new ResponseDTO("ok",wynkService.publishSong(publishCO,playlistRepository)));
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
