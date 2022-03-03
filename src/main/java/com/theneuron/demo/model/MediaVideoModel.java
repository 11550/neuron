package com.theneuron.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.theneuron.demo.list.Type;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaVideoModel {
    private String id;
    private Type type;
    private String url;
    private Integer duration;
}
