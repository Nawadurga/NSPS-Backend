package com.project.carparking.dto;
public enum NotificationScenario {
    ONBOARDING("Welcome aboard!", "You have successfully joined our community. Start exploring now!"),
    VEHICLE_ADDED("New vehicle added", "Your vehicle has been successfully added to your profile."),
    PARKING_SPACE_ALLOCATED("Parking space allocated", "Your vehicle has been assigned a parking space."),
    VEHICLE_Removed("Vehicle removed", "Your vehicle has been removed from your profile. Park with caution!"),
    PAYMENT_SUCCESS("Payment successful", "Your payment for this month has been processed successfully. Thank you for your payment!");

    private final String title;
    private final String subtitle;

    NotificationScenario(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
