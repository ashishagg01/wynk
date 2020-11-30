package com.airtel.wynk.service;

import com.airtel.wynk.co.FollowCO;
import com.airtel.wynk.co.PublishCO;
import com.airtel.wynk.repository.PlaylistRepository;

public interface WynkService {
    String followArtist(FollowCO followCO, PlaylistRepository playlistRepository);

    String unfollowArtist(FollowCO followCO, PlaylistRepository playlistRepository);

    String publishSong(PublishCO publishCO, PlaylistRepository playlistRepository);
}
