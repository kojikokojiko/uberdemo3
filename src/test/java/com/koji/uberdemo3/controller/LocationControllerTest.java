package com.koji.uberdemo3.controller;

import com.koji.uberdemo3.entity.DriverLocation;
import com.koji.uberdemo3.entity.Location;
import com.koji.uberdemo3.entity.User;
import com.koji.uberdemo3.service.LocationService;
import com.koji.uberdemo3.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(LocationController.class)
@Import(TestSecurityConfig.class)
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LocationService locationService;

    @Test
    @WithMockUser
    public void updateDriverLocation_ShouldReturnUpdatedLocation() throws Exception {
        // Given
        Long driverId = 1L;
        Location location = new Location();
        location.setLatitude(35.6895);
        location.setLongitude(139.6917);
        location.setAddress("Tokyo");
        location.setCity("Tokyo");
        location.setState("Tokyo");
        location.setCountry("Japan");
        location.setPostalCode("100-0001");

        User driver = new User();
        driver.setId(driverId);

        DriverLocation driverLocation = new DriverLocation();
        driverLocation.setId(1L);
        driverLocation.setDriver(driver);
        driverLocation.setLocation(location);
        driverLocation.setTimestamp(LocalDateTime.now());
        driverLocation.setIsAvailable(true);

        when(locationService.updateDriverLocation(any(User.class), any(Location.class), any(Boolean.class)))
                .thenReturn(driverLocation);

        // When & Then
        mockMvc.perform(post("/api/locations/driver/{driverId}", driverId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(location))
                .param("isAvailable", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.isAvailable", is(true)));
    }

    @Test
    @WithMockUser
    public void findNearbyDrivers_ShouldReturnListOfDrivers() throws Exception {
        // Given
        Location location = new Location();
        location.setLatitude(35.6895);
        location.setLongitude(139.6917);

        User driver = new User();
        driver.setId(1L);

        DriverLocation driverLocation = new DriverLocation();
        driverLocation.setId(1L);
        driverLocation.setDriver(driver);
        driverLocation.setLocation(location);
        driverLocation.setTimestamp(LocalDateTime.now());
        driverLocation.setIsAvailable(true);

        List<DriverLocation> nearbyDrivers = Arrays.asList(driverLocation);

        when(locationService.findNearbyDrivers(any(Double.class), any(Double.class), any(Double.class)))
                .thenReturn(nearbyDrivers);

        // When & Then
        mockMvc.perform(get("/api/locations/nearby")
                .param("latitude", "35.6895")
                .param("longitude", "139.6917")
                .param("radius", "5000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].isAvailable", is(true)));
    }

    @Test
    @WithMockUser
    public void getAvailableDrivers_ShouldReturnListOfAvailableDrivers() throws Exception {
        // Given
        Location location = new Location();
        location.setLatitude(35.6895);
        location.setLongitude(139.6917);

        User driver = new User();
        driver.setId(1L);

        DriverLocation driverLocation = new DriverLocation();
        driverLocation.setId(1L);
        driverLocation.setDriver(driver);
        driverLocation.setLocation(location);
        driverLocation.setTimestamp(LocalDateTime.now());
        driverLocation.setIsAvailable(true);

        List<DriverLocation> availableDrivers = Arrays.asList(driverLocation);

        when(locationService.getAvailableDrivers())
                .thenReturn(availableDrivers);

        // When & Then
        mockMvc.perform(get("/api/locations/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].isAvailable", is(true)));
    }
} 