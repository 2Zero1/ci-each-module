package com.truckhelper.application.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import com.truckhelper.application.dtos.GeoPositionDto;
import com.truckhelper.application.utils.KakaoApiService;
import com.truckhelper.core.models.GeoPosition;

@RestController
@RequestMapping("geo-position")
public class GeoPositionController {
    private final KakaoApiService kakaoApiService;

    private final ModelMapper modelMapper;

    public GeoPositionController(KakaoApiService kakaoApiService,
                                 ModelMapper modelMapper) {
        this.kakaoApiService = kakaoApiService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    GeoPositionDto search(@RequestParam String address) {
        GeoPosition geoPosition = kakaoApiService.searchAddress(address);
        return geoPositionToDto(geoPosition);
    }

    private GeoPositionDto geoPositionToDto(GeoPosition geoPosition) {
        return modelMapper.map(geoPosition, GeoPositionDto.class);
    }
}
