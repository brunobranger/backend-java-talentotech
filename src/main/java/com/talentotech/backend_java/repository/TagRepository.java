package com.talentotech.backend_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentotech.backend_java.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {}
