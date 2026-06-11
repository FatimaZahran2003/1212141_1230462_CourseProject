package com.example.a1212141_courseproject.models;


public class Trip {
    private int id;
    private String destination;
    private String country;
    private String description;
    private int durationDays;
    private String date;
    private double price;
    private double rating;
    private String imageUrl;
    private boolean isFavorite;

    // Constructor كامل
    public Trip(int id, String destination, String country, String description,
                int durationDays, String date, double price, double rating, String imageUrl) {
        this.id = id;
        this.destination = destination;
        this.country = country;
        this.description = description;
        this.durationDays = durationDays;
        this.date = date;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.isFavorite = false;
    }

    // Getters
    public int getId() { return id; }
    public String getDestination() { return destination; }
    public String getCountry() { return country; }
    public String getDescription() { return description; }
    public int getDurationDays() { return durationDays; }
    public String getDate() { return date; }
    public double getPrice() { return price; }
    public double getRating() { return rating; }
    public String getImageUrl() { return imageUrl; }
    public boolean isFavorite() { return isFavorite; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}