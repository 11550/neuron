package com.theneuron.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theneuron.demo.entity.Media;
import com.theneuron.demo.entity.MediaVideo;
import com.theneuron.demo.list.Type;
import com.theneuron.demo.model.MediaVideoModel;
import com.theneuron.demo.repository.MediaRepository;
import com.theneuron.demo.repository.MediaVideoRepository;
import com.theneuron.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/")
@Validated
@RequiredArgsConstructor
public class Controller {

    private final MediaRepository mediaRepository;
    private final MediaVideoRepository mediaVideoRepository;
    private final VideoDurationCalculationService videoDurationCalculationService;

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(4);
    private static final ReentrantLock LOCK = new ReentrantLock();

    @PostMapping("createMedia")
    public ResponseEntity<HttpStatus> createMedia(
            @RequestParam @NotNull @Size(max = 36) String id,
            @RequestParam Type type,
            @RequestParam(required = false) @Size(max = 254) String url) {

        LOCK.lock();
        if (mediaRepository.existsByIdAndType(id, type)) {
            LOCK.unlock();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        mediaRepository.saveAndFlush(new Media(id, type, url));
        LOCK.unlock();

        if (Objects.equals(type, Type.VIDEO)
                && !Objects.equals(url, null)
                && !mediaVideoRepository.existsById(id)) {

            THREAD_POOL.submit(() -> mediaVideoRepository.save(
                    new MediaVideo(id, videoDurationCalculationService.calc())
            ));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getMediaInfo(@PathVariable("id") String mediaId) throws JsonProcessingException {
        Media media = mediaRepository.findById(mediaId).orElse(null);
        if (media == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        MediaVideo mediaVideo = mediaVideoRepository.findById(mediaId).orElse(null);
        MediaVideoModel result = MediaVideoModel.builder()
                .id(mediaId)
                .type(media.getType())
                .url(media.getUrl())
                .duration(mediaVideo != null ? mediaVideo.getDuration() : null)
                .build();
        return new ResponseEntity<>(new ObjectMapper().writeValueAsString(result), HttpStatus.OK);
    }
}

