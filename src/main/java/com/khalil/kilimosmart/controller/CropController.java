package com.khalil.kilimosmart.controller;

import com.khalil.kilimosmart.model.Crop;
import com.khalil.kilimosmart.service.CropService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin(origins = "*") // Allow all origins (good for dev/testing with React Native)
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @GetMapping
    public List<Crop> getAllCrops() {
        return cropService.getAllCrops();
    }

    @PostMapping
    public Crop createCrop(@RequestBody Crop crop) {
        return cropService.saveCrop(crop);
    }
}
