package com.theneuron.demo.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media_video")
public class MediaVideo {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "duration")
    private Integer duration;
}
