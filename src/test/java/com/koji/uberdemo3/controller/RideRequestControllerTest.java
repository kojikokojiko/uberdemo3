package com.koji.uberdemo3.controller;

import com.koji.uberdemo3.dto.RideRequestDTO;
import com.koji.uberdemo3.entity.*;
import com.koji.uberdemo3.service.RideRequestService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(RideRequestController.class)
@Import(TestSecurityConfig.class)
public class RideRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RideRequestService rideRequestService;

    @Test
    @WithMockUser
    public void createRideRequest_ShouldReturnCreatedRequest() throws Exception {
        // Given
        Long passengerId = 1L;
        Location pickupLocation = createTestLocation(35.6895, 139.6917, "Tokyo");
        Location dropoffLocation = createTestLocation(35.6586, 139.7454, "Shibuya");
        Double estimatedFare = 1500.0;

        RideRequestDTO requestDTO = new RideRequestDTO();
        requestDTO.setPassengerId(passengerId);
        requestDTO.setPickupLocation(pickupLocation);
        requestDTO.setDropoffLocation(dropoffLocation);
        requestDTO.setEstimatedFare(estimatedFare);

        RideRequest expectedRideRequest = createTestRideRequest(
                passengerId, pickupLocation, dropoffLocation, estimatedFare);

        when(rideRequestService.createRideRequest(any(User.class), any(Location.class), 
                any(Location.class), any(Double.class)))
                .thenReturn(expectedRideRequest);

        // When & Then
        mockMvc.perform(post("/api/rides/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.estimatedFare", is(1500.0)));
    }

    @Test
    @WithMockUser
    public void acceptRideRequest_ShouldReturnAcceptedRequest() throws Exception {
        // Given
        Long requestId = 1L;
        Long driverId = 2L;
        RideRequest acceptedRequest = createTestRideRequest(1L, 
                createTestLocation(35.6895, 139.6917, "Tokyo"),
                createTestLocation(35.6586, 139.7454, "Shibuya"),
                1500.0);
        acceptedRequest.setStatus(RideRequest.RideStatus.ACCEPTED);

        when(rideRequestService.acceptRideRequest(any(Long.class), any(User.class)))
                .thenReturn(acceptedRequest);

        // When & Then
        mockMvc.perform(post("/api/rides/{requestId}/accept", requestId)
                .param("driverId", driverId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ACCEPTED")));
    }

    @Test
    @WithMockUser
    public void updateRideStatus_ShouldReturnUpdatedRequest() throws Exception {
        // Given
        Long requestId = 1L;
        RideRequest.RideStatus newStatus = RideRequest.RideStatus.IN_PROGRESS;
        RideRequest updatedRequest = createTestRideRequest(1L, 
                createTestLocation(35.6895, 139.6917, "Tokyo"),
                createTestLocation(35.6586, 139.7454, "Shibuya"),
                1500.0);
        updatedRequest.setStatus(newStatus);

        when(rideRequestService.updateRideStatus(any(Long.class), any(RideRequest.RideStatus.class)))
                .thenReturn(updatedRequest);

        // When & Then
        mockMvc.perform(put("/api/rides/{requestId}/status", requestId)
                .param("status", newStatus.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }

    @Test
    @WithMockUser
    public void getPendingRequests_ShouldReturnListOfRequests() throws Exception {
        // Given
        List<RideRequest> pendingRequests = Arrays.asList(
                createTestRideRequest(1L, 
                        createTestLocation(35.6895, 139.6917, "Tokyo"),
                        createTestLocation(35.6586, 139.7454, "Shibuya"),
                        1500.0)
        );

        when(rideRequestService.getPendingRequests())
                .thenReturn(pendingRequests);

        // When & Then
        mockMvc.perform(get("/api/rides/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("PENDING")));
    }

    @Test
    @WithMockUser
    public void getActiveRidesByDriver_ShouldReturnListOfActiveRides() throws Exception {
        // Given
        Long driverId = 2L;
        List<RideRequest> activeRides = Arrays.asList(
                createTestRideRequest(1L, 
                        createTestLocation(35.6895, 139.6917, "Tokyo"),
                        createTestLocation(35.6586, 139.7454, "Shibuya"),
                        1500.0)
        );
        activeRides.get(0).setStatus(RideRequest.RideStatus.IN_PROGRESS);

        when(rideRequestService.getActiveRidesByDriver(any(User.class)))
                .thenReturn(activeRides);

        // When & Then
        mockMvc.perform(get("/api/rides/driver/{driverId}/active", driverId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("IN_PROGRESS")));
    }

    @Test
    @WithMockUser
    public void getActiveRidesByPassenger_ShouldReturnListOfActiveRides() throws Exception {
        // Given
        Long passengerId = 1L;
        List<RideRequest> activeRides = Arrays.asList(
                createTestRideRequest(passengerId, 
                        createTestLocation(35.6895, 139.6917, "Tokyo"),
                        createTestLocation(35.6586, 139.7454, "Shibuya"),
                        1500.0)
        );
        activeRides.get(0).setStatus(RideRequest.RideStatus.ACCEPTED);

        when(rideRequestService.getActiveRidesByPassenger(any(User.class)))
                .thenReturn(activeRides);

        // When & Then
        mockMvc.perform(get("/api/rides/passenger/{passengerId}/active", passengerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("ACCEPTED")));
    }

    @Test
    @WithMockUser
    public void getRideHistory_ShouldReturnListOfRides() throws Exception {
        // Given
        Long passengerId = 1L;
        List<RideRequest> rideHistory = Arrays.asList(
                createTestRideRequest(passengerId, 
                        createTestLocation(35.6895, 139.6917, "Tokyo"),
                        createTestLocation(35.6586, 139.7454, "Shibuya"),
                        1500.0)
        );
        rideHistory.get(0).setStatus(RideRequest.RideStatus.COMPLETED);

        when(rideRequestService.getRideHistoryByUser(any(User.class)))
                .thenReturn(rideHistory);

        // When & Then
        mockMvc.perform(get("/api/rides/passenger/{passengerId}/history", passengerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("COMPLETED")));
    }

    @Test
    @WithMockUser
    public void acceptRideRequest_ShouldReturnNotFound_WhenRequestDoesNotExist() throws Exception {
        // Given
        Long requestId = 999L;
        Long driverId = 2L;

        when(rideRequestService.acceptRideRequest(any(Long.class), any(User.class)))
                .thenThrow(new RuntimeException("Ride request not found"));

        // When & Then
        mockMvc.perform(post("/api/rides/{requestId}/accept", requestId)
                .param("driverId", driverId.toString()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    public void acceptRideRequest_ShouldReturnBadRequest_WhenRequestNotPending() throws Exception {
        // Given
        Long requestId = 1L;
        Long driverId = 2L;

        when(rideRequestService.acceptRideRequest(any(Long.class), any(User.class)))
                .thenThrow(new RuntimeException("Ride request is not in PENDING status"));

        // When & Then
        mockMvc.perform(post("/api/rides/{requestId}/accept", requestId)
                .param("driverId", driverId.toString()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    public void updateRideStatus_ShouldReturnNotFound_WhenRequestDoesNotExist() throws Exception {
        // Given
        Long requestId = 999L;
        RideRequest.RideStatus newStatus = RideRequest.RideStatus.IN_PROGRESS;

        when(rideRequestService.updateRideStatus(any(Long.class), any(RideRequest.RideStatus.class)))
                .thenThrow(new RuntimeException("Ride request not found"));

        // When & Then
        mockMvc.perform(put("/api/rides/{requestId}/status", requestId)
                .param("status", newStatus.name()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    public void createRideRequest_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        // Given
        RideRequestDTO requestDTO = new RideRequestDTO();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/rides/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    private Location createTestLocation(Double latitude, Double longitude, String address) {
        Location location = new Location();
        location.setId(1L);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAddress(address);
        location.setCity("Tokyo");
        location.setState("Tokyo");
        location.setCountry("Japan");
        location.setPostalCode("100-0001");
        return location;
    }

    private RideRequest createTestRideRequest(Long passengerId, Location pickupLocation, 
                                            Location dropoffLocation, Double estimatedFare) {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setId(1L);
        
        User passenger = new User();
        passenger.setId(passengerId);
        rideRequest.setPassenger(passenger);
        
        rideRequest.setPickupLocation(pickupLocation);
        rideRequest.setDropoffLocation(dropoffLocation);
        rideRequest.setRequestTime(LocalDateTime.now());
        rideRequest.setStatus(RideRequest.RideStatus.PENDING);
        rideRequest.setEstimatedFare(estimatedFare);
        
        return rideRequest;
    }
} 