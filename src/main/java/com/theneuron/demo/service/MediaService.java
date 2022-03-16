package com.theneuron.demo.service;

import com.theneuron.demo.entity.Media;
import com.theneuron.demo.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private EntityManager entityManager;

    public Media getById(String id) {
        return mediaRepository.getById(id);
    }

    @Transactional
    public void create(Media media) {
        entityManager.createNativeQuery(
                "insert into media (id, type, url) values (?,?,?)")
                .setParameter(1, media.getId())
                .setParameter(2, media.getType())
                .setParameter(3, media.getUrl())
                .executeUpdate();
    }

    public void delete(String id) {
        mediaRepository.deleteById(id);
    }
}
