package com.project.carparking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notification_token", indexes = {
        @Index(name = "push_notification_token_index", columnList = "push_notification_token")
})
public class NotificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "push_notification_token")
    private String pushNotificationToken;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Assuming the column in the Notification table is named "user_id"
    @JsonIgnore
    private User user;
}