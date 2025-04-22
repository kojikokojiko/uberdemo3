package com.koji.uberdemo3.controller;

import com.koji.uberdemo3.dto.*;
import com.koji.uberdemo3.entity.Location;
import com.koji.uberdemo3.service.FareCalculationService;
import com.koji.uberdemo3.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(FareController.class)
@Import(TestSecurityConfig.class)
public class FareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FareCalculationService fareCalculationService;

    @Test
    @WithMockUser
    void calculateFare_ShouldReturnFareResponse() throws Exception {
        // Given
        Location pickupLocation = createTestLocation(35.6812, 139.7671, "Tokyo Station");
        Location dropoffLocation = createTestLocation(35.6580, 139.7016, "Shibuya Station");
        double surgeMultiplier = 1.0;
        double expectedFare = 1500.0;
        double expectedDistance = 5.2;
        double expectedDuration = 10.4;

        FareRequest request = new FareRequest();
        request.setPickupLocation(pickupLocation);
        request.setDropoffLocation(dropoffLocation);

        when(fareCalculationService.calculateFare(any(Location.class), any(Location.class), any(Double.class)))
                .thenReturn(expectedFare);
        when(fareCalculationService.calculateDistance(any(Location.class), any(Location.class)))
                .thenReturn(expectedDistance);
        when(fareCalculationService.calculateDuration(any(Location.class), any(Location.class)))
                .thenReturn(expectedDuration);

        // When & Then
        mockMvc.perform(post("/api/fare/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .param("surgeMultiplier", String.valueOf(surgeMultiplier)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fare", is(expectedFare)))
                .andExpect(jsonPath("$.distance", is(expectedDistance)))
                .andExpect(jsonPath("$.duration", is(expectedDuration)))
                .andExpect(jsonPath("$.surgeMultiplier", is(surgeMultiplier)));
    }

    @Test
    @WithMockUser
    void calculateDistance_ShouldReturnDistanceResponse() throws Exception {
        // Given
        double expectedDistance = 5.2;
        when(fareCalculationService.calculateDistance(any(Location.class), any(Location.class)))
                .thenReturn(expectedDistance);

        // When & Then
        mockMvc.perform(get("/api/fare/distance")
                .param("lat1", "35.6812")
                .param("lon1", "139.7671")
                .param("lat2", "35.6580")
                .param("lon2", "139.7016"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distance", is(expectedDistance)));
    }

    @Test
    @WithMockUser
    void calculateDuration_ShouldReturnDurationResponse() throws Exception {
        // Given
        double expectedDuration = 10.4;
        when(fareCalculationService.calculateDuration(any(Location.class), any(Location.class)))
                .thenReturn(expectedDuration);

        // When & Then
        mockMvc.perform(get("/api/fare/duration")
                .param("lat1", "35.6812")
                .param("lon1", "139.7671")
                .param("lat2", "35.6580")
                .param("lon2", "139.7016"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duration", is(expectedDuration)));
    }

    @Test
    @WithMockUser
    void calculateFare_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        // Given
        FareRequest request = new FareRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/fare/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void calculateFare_ShouldReturnBadRequest_WhenInvalidSurgeMultiplier() throws Exception {
        // Given
        Location pickupLocation = createTestLocation(35.6812, 139.7671, "Tokyo Station");
        Location dropoffLocation = createTestLocation(35.6580, 139.7016, "Shibuya Station");
        double invalidSurgeMultiplier = 0.5;

        FareRequest request = new FareRequest();
        request.setPickupLocation(pickupLocation);
        request.setDropoffLocation(dropoffLocation);

        // When & Then
        mockMvc.perform(post("/api/fare/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .param("surgeMultiplier", String.valueOf(invalidSurgeMultiplier)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void calculateDistance_ShouldReturnBadRequest_WhenInvalidCoordinates() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/fare/distance")
                .param("lat1", "91") // Invalid latitude
                .param("lon1", "139.7671")
                .param("lat2", "35.6580")
                .param("lon2", "139.7016"))
                .andExpect(status().isBadRequest());
    }

    private Location createTestLocation(double latitude, double longitude, String address) {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAddress(address);
        location.setCity("Tokyo");
        location.setState("Tokyo");
        location.setCountry("Japan");
        location.setPostalCode("100-0001");
        return location;
    }
} 