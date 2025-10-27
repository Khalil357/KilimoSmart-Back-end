package com.khalil.kilimosmart.service;

import com.khalil.kilimosmart.model.Crop;
import com.khalil.kilimosmart.repository.CropRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CropServiceImpl implements CropService {

    private final CropRepository cropRepository;

    public CropServiceImpl(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    @Override
    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }

    @Override
    public Crop saveCrop(Crop crop) {
        return cropRepository.save(crop);
    }
}
