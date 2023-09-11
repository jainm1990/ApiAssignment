package com.api.assignment.repository;

import com.api.assignment.entity.UserDetails;
import com.api.assignment.entity.UserImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<UserImages, Long> {
}
