package com.khalil.kilimosmart.repository;

import com.khalil.kilimosmart.model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Long> {
}
