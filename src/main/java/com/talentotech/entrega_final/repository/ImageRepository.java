package com.talentotech.entrega_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentotech.entrega_final.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    // ...
}
