package com.example.a1212141_courseproject.models;

public class Reservation {

    private int id;
    private int userId;
    private int tripId;
    private String tripName;
    private int quantity;
    private String reservationDate;
    private String status;

    public Reservation(int id, int userId, int tripId, String tripName,
                       int quantity, String reservationDate, String status) {
        this.id = id;
        this.userId = userId;
        this.tripId = tripId;
        this.tripName = tripName;
        this.quantity = quantity;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    public Reservation(int id, int userId, int tripId, int quantity,
                       String reservationDate, String status) {
        this.id = id;
        this.userId = userId;
        this.tripId = tripId;
        this.quantity = quantity;
        this.reservationDate = reservationDate;
        this.status = status;
        this.tripName = "";
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getTripId() {
        return tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
}