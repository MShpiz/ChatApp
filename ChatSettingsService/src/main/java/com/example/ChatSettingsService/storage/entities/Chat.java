package com.example.ChatSettingsService.storage.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chatrooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "isprivate")
    private boolean isPrivate;

    @Column(name = "owner")
    private long owner;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
}
