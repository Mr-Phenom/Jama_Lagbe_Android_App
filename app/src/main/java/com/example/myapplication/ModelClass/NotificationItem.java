package com.example.myapplication.ModelClass;

public class NotificationItem {
    private int notificationId; // ID of the notification
    private int itemId;         // ID of the borrowed item
    private int transactionId;  // ID of the transaction (newly added field)
    private String notificationText; // Text to display in the notification

    // Constructor
    public NotificationItem(int notificationId, int itemId, int transactionId, String notificationText) {
        this.notificationId = notificationId;
        this.itemId = itemId;
        this.transactionId = transactionId;
        this.notificationText = notificationText;
    }

    // Default constructor
    public NotificationItem() {}

    // Getters and setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }
}
