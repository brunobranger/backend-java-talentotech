package com.talentotech.backend_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentotech.backend_java.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {}
