package org.zyy.campusdemobackend.Campus.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_id")
    private Integer friendshipId;

    @Column(name = "user1_id", nullable = false)
    private Integer user1Id;

    @Column(name = "user2_id", nullable = false)
    private Integer user2Id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "action_user_id", nullable = false)
    private Integer actionUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "relationship_type")
    private String relationshipType;

    // Getters and Setters
}