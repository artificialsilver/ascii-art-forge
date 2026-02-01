package com.ascii.ascii_forge.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AsciiArt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    @Builder
    public AsciiArt(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}