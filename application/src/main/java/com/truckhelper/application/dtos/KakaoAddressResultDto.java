package com.truckhelper.application.dtos;

import java.util.List;

import lombok.Getter;

@Getter
public class KakaoAddressResultDto {
    private List<KakaoAddressDocumentDto> documents;
}
