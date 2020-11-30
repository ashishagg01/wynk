package com.airtel.wynk.service.impl;

import com.airtel.wynk.co.FollowCO;
import com.airtel.wynk.repository.PlaylistRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class WynkServiceImplTest {

    @InjectMocks
    private WynkServiceImpl wynkService;

    @Mock
    private PlaylistRepository playlistRepository;

    @Test
    public void unfollowArtist() {
        FollowCO co = new FollowCO();
        co.setUser("u1");
        co.setArtist(new ArrayList<>());
        co.getArtist().add("a1");
        String response = wynkService.unfollowArtist(co,playlistRepository);
        assertEquals("u1 stopped following a1",response);
    }
}
