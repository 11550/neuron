package com.theneuron.demo.repository;

import com.theneuron.demo.entity.Media;
import com.theneuron.demo.list.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, String> {
    Boolean existsByIdAndType(String id, Type type);
}
