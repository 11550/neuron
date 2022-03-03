package com.theneuron.demo.repository;

import com.theneuron.demo.entity.MediaVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaVideoRepository extends JpaRepository<MediaVideo, String> {
}
