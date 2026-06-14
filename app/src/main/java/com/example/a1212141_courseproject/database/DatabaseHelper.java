package com.example.a1212141_courseproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a1212141_courseproject.models.Reservation;
import com.example.a1212141_courseproject.models.Trip;
import com.example.a1212141_courseproject.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "travel_planner.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_PHONE = "phone";

    private static final String TABLE_TRIPS = "trips";
    private static final String COLUMN_TRIP_ID = "id";
    private static final String COLUMN_DESTINATION = "destination";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DURATION = "duration_days";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_IMAGE_URL = "image_url";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";

    private static final String TABLE_RESERVATIONS = "reservations";
    private static final String COLUMN_RESERVATION_ID = "id";
    private static final String COLUMN_USER_ID_FK = "user_id";
    private static final String COLUMN_TRIP_ID_FK = "trip_id";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_RESERVATION_DATE = "reservation_date";
    private static final String COLUMN_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_FIRST_NAME + " TEXT, "
                + COLUMN_LAST_NAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_PHONE + " TEXT)";
        db.execSQL(createUsersTable);

        String createTripsTable = "CREATE TABLE " + TABLE_TRIPS + "("
                + COLUMN_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DESTINATION + " TEXT, "
                + COLUMN_COUNTRY + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_DURATION + " INTEGER, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_PRICE + " REAL, "
                + COLUMN_RATING + " REAL, "
                + COLUMN_IMAGE_URL + " TEXT, "
                + COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0)";
        db.execSQL(createTripsTable);

        String createReservationsTable = "CREATE TABLE " + TABLE_RESERVATIONS + "("
                + COLUMN_RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_FK + " INTEGER, "
                + COLUMN_TRIP_ID_FK + " INTEGER, "
                + COLUMN_QUANTITY + " INTEGER, "
                + COLUMN_RESERVATION_DATE + " TEXT, "
                + COLUMN_STATUS + " TEXT)";
        db.execSQL(createReservationsTable);

        String insertAdmin = "INSERT INTO " + TABLE_USERS + "("
                + COLUMN_EMAIL + ", "
                + COLUMN_FIRST_NAME + ", "
                + COLUMN_LAST_NAME + ", "
                + COLUMN_PASSWORD + ") "
                + "VALUES('admin@admin.com', 'Admin', 'Admin', 'Admin123!')";
        db.execSQL(insertAdmin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_GENDER, user.getGender());
        values.put(COLUMN_PHONE, user.getPhone());

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null,
                null,
                null
        );

        User user = null;

        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    null
            );
        }

        cursor.close();
        db.close();

        return user;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        User user = null;

        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    null
            );
        }

        cursor.close();
        db.close();

        return user;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_GENDER, user.getGender());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            values.put(COLUMN_PASSWORD, user.getPassword());
        }

        int result = db.update(
                TABLE_USERS,
                values,
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(user.getId())}
        );

        db.close();

        return result > 0;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        null
                );

                users.add(user);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return users;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_USERS,
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)}
        );

        db.close();

        return result > 0;
    }

    public boolean insertTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DESTINATION, trip.getDestination());
        values.put(COLUMN_COUNTRY, trip.getCountry());
        values.put(COLUMN_DESCRIPTION, trip.getDescription());
        values.put(COLUMN_DURATION, trip.getDurationDays());
        values.put(COLUMN_DATE, trip.getDate());
        values.put(COLUMN_PRICE, trip.getPrice());
        values.put(COLUMN_RATING, trip.getRating());
        values.put(COLUMN_IMAGE_URL, trip.getImageUrl());
        values.put(COLUMN_IS_FAVORITE, trip.isFavorite() ? 1 : 0);

        long result = db.insert(TABLE_TRIPS, null, values);

        db.close();

        return result != -1;
    }

    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRIPS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Trip trip = new Trip(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRIP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESTINATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
                );

                trip.setFavorite(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1
                );

                trips.add(trip);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return trips;
    }

    public void deleteAllTrips() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIPS, null, null);
        db.close();
    }

    public void addToFavorites(int tripId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, 1);

        db.update(
                TABLE_TRIPS,
                values,
                COLUMN_TRIP_ID + "=?",
                new String[]{String.valueOf(tripId)}
        );

        db.close();
    }

    public void removeFromFavorites(int tripId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, 0);

        db.update(
                TABLE_TRIPS,
                values,
                COLUMN_TRIP_ID + "=?",
                new String[]{String.valueOf(tripId)}
        );

        db.close();
    }

    public boolean isFavorite(int tripId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_TRIPS,
                new String[]{COLUMN_IS_FAVORITE},
                COLUMN_TRIP_ID + "=?",
                new String[]{String.valueOf(tripId)},
                null,
                null,
                null
        );

        boolean favorite = false;

        if (cursor.moveToFirst()) {
            favorite = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1;
        }

        cursor.close();
        db.close();

        return favorite;
    }

    public List<Trip> getFavoriteTrips() {
        List<Trip> favorites = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_TRIPS,
                null,
                COLUMN_IS_FAVORITE + "=?",
                new String[]{"1"},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Trip trip = new Trip(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRIP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESTINATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
                );

                trip.setFavorite(true);
                favorites.add(trip);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return favorites;
    }

    public List<Trip> getSpecialTrips() {
        List<Trip> specialTrips = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_TRIPS,
                null,
                COLUMN_RATING + ">=?",
                new String[]{"4.7"},
                null,
                null,
                COLUMN_RATING + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                Trip trip = new Trip(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRIP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESTINATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
                );

                trip.setFavorite(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1
                );

                specialTrips.add(trip);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return specialTrips;
    }

    // Delete trip by id
    public boolean deleteTrip(int tripId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_TRIPS, COLUMN_TRIP_ID + "=?",
                new String[]{String.valueOf(tripId)});
        db.close();
        return result > 0;
    }

    // Get trip by id
    public Trip getTripById(int tripId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRIPS, null, COLUMN_TRIP_ID + "=?",
                new String[]{String.valueOf(tripId)}, null, null, null);

        Trip trip = null;
        if (cursor.moveToFirst()) {
            trip = new Trip(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRIP_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESTINATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
            );
        }
        cursor.close();
        db.close();
        return trip;
    }

    // Get all reservations
    public List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r.id, r.user_id, r.trip_id, t.destination, r.quantity, " +
                "r.reservation_date, r.status " +
                "FROM " + TABLE_RESERVATIONS + " r " +
                "INNER JOIN " + TABLE_TRIPS + " t ON r.trip_id = t.id " +
                "WHERE r.user_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );

                reservations.add(reservation);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return reservations;
    }
    public boolean addReservation(int userId, int tripId, int quantity,
                                  String reservationDate, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID_FK, userId);
        values.put(COLUMN_TRIP_ID_FK, tripId);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_RESERVATION_DATE, reservationDate);
        values.put(COLUMN_STATUS, status);

        long result = db.insert(TABLE_RESERVATIONS, null, values);
        db.close();

        return result != -1;
    }
    // Update trip
    public boolean updateTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DESTINATION, trip.getDestination());
        values.put(COLUMN_COUNTRY, trip.getCountry());
        values.put(COLUMN_DESCRIPTION, trip.getDescription());
        values.put(COLUMN_DURATION, trip.getDurationDays());
        values.put(COLUMN_DATE, trip.getDate());
        values.put(COLUMN_PRICE, trip.getPrice());
        values.put(COLUMN_RATING, trip.getRating());
        values.put(COLUMN_IMAGE_URL, trip.getImageUrl());

        int result = db.update(TABLE_TRIPS, values, COLUMN_TRIP_ID + "=?",
                new String[]{String.valueOf(trip.getId())});
        db.close();
        return result > 0;
    }
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESERVATIONS, null, null, null, null, null, COLUMN_RESERVATION_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID_FK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRIP_ID_FK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
                );
                reservations.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reservations;
    }
}