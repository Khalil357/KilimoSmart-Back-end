package com.khalil.kilimosmart.service;

import com.khalil.kilimosmart.model.Crop;

import java.util.List;

public interface CropService {
    List<Crop> getAllCrops();
    Crop saveCrop(Crop crop);
}
