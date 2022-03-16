package com.theneuron.demo.service;

import com.theneuron.demo.entity.MediaVideo;
import com.theneuron.demo.list.Type;
import com.theneuron.demo.repository.MediaVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MediaVideoService {

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(4);

    @Autowired
    private MediaVideoRepository mediaVideoRepository;
    @Autowired
    private VideoDurationCalculationService durationCalculationService;

    public MediaVideo create(MediaVideo mediaVideo) {
        return mediaVideoRepository.save(mediaVideo);
    }

    public Boolean existsById(String id) {
        return mediaVideoRepository.existsById(id);
    }

    public MediaVideo getById(String id) {
        return mediaVideoRepository.getById(id);
    }

    public void save(String id, Type type, String url) {
        if (Objects.equals(type, Type.VIDEO)
                && !Objects.equals(url, null)
                && !existsById(id)) {

            THREAD_POOL.submit(() -> create(
                    new MediaVideo(id, durationCalculationService.calc())
            ));
        }
    }
}
