package com.example.a1212141_courseproject.api;

import com.example.a1212141_courseproject.models.Trip;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("trips/trips")
    Call<List<Trip>> getTrips();
}