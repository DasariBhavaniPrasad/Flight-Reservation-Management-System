package com.rms;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Booking {
    private int bookingId;
    private int userId;
    private int flightId;
    private String flightNumber;
    private Integer seatId; // Allow null for seatId
    private String bookingReference;
    private String bookingStatus;
    private BigDecimal totalAmount;
    private Timestamp bookingDate; // Added bookingDate
    private int numberOfSeats; // New field for number of seats

    // Default Constructor
    public Booking() {
    }

    // Parameterized Constructor (optional)
    public Booking(int bookingId, int userId, int flightId, Integer seatId, 
                   String bookingReference, String bookingStatus, 
                   BigDecimal totalAmount, Timestamp bookingDate, int numberOfSeats) { // Include numberOfSeats
        this.bookingId = bookingId;
        this.userId = userId;
        this.flightId = flightId;
        this.seatId = seatId;
        this.bookingReference = bookingReference;
        this.bookingStatus = bookingStatus;
        this.totalAmount = totalAmount;
        this.bookingDate = bookingDate;
        this.numberOfSeats = numberOfSeats; // Initialize numberOfSeats
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Timestamp getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getNumberOfSeats() { // New getter for numberOfSeats
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) { // New setter for numberOfSeats
        this.numberOfSeats = numberOfSeats;
    }

    // Optional: setTotalAmount from double value
    public void setTotalAmount(double amount) {
        this.totalAmount = BigDecimal.valueOf(amount);
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
}
