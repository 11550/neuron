package com.theneuron.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theneuron.demo.entity.Media;
import com.theneuron.demo.entity.MediaVideo;
import com.theneuron.demo.list.Type;
import com.theneuron.demo.model.MediaVideoModel;
import com.theneuron.demo.service.MediaService;
import com.theneuron.demo.service.MediaVideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api/v1/")
@Validated
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final MediaService mediaService;
    private final MediaVideoService mediaVideoService;
    private final ObjectMapper objectMapper;

    @PostMapping("createMedia")
    public ResponseEntity<String> createMedia(
            @RequestParam @NotNull @Size(max = 36) String id,
            @RequestParam Type type,
            @RequestParam(required = false) @Size(max = 254) String url) {

        try {
            mediaService.create(new Media(id, type, url));
        } catch (Exception e) {
            if (e instanceof PersistenceException && e.getCause().getCause().getMessage().contains(id)) {
                String message = String.format("Duplicated Media! id: %s", id);
                return new ResponseEntity<>(message, HttpStatus.CONFLICT);
            }
            throw e;
        }

        mediaVideoService.save(id, type, url);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getMediaInfo(@PathVariable("id") String mediaId) throws JsonProcessingException {
        Media media = mediaService.getById(mediaId);

        if (media == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        MediaVideo mediaVideo = mediaVideoService.getById(mediaId);

        MediaVideoModel model = MediaVideoModel.builder()
                .id(mediaId)
                .type(media.getType())
                .url(media.getUrl())
                .duration(mediaVideo != null ? mediaVideo.getDuration() : null)
                .build();
        String result = objectMapper.writeValueAsString(model);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String mediaId) {
        mediaService.delete(mediaId);
    }
}


