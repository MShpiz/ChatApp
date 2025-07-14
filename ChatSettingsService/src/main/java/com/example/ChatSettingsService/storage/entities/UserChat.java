package com.example.ChatSettingsService.storage.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userchatroom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChat {
    @Column(name = "userid")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "chatid")
    Chat chat;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
}
