package com.theneuron.demo.entity;

import com.theneuron.demo.list.Type;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media")
public class Media {

    @Id
    @Column(name = "id",
            nullable = false,
            length = 36
    )
    private String id;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private Type type;

    @Column(name = "url")
    private String url;
}
