package com.truckhelper.admin.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.admin.applications.PlaceService;
import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.admin.repositories.PlaceImageRepository;
import com.truckhelper.admin.repositories.PlaceRepository;
import com.truckhelper.admin.utils.JwtUtil;
import com.truckhelper.core.models.Admin;
import com.truckhelper.core.models.AdminId;
import com.truckhelper.core.models.Grade;
import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.PlaceId;
import com.truckhelper.core.models.PlaceImage;
import com.truckhelper.core.models.PlaceImageId;

@WebMvcTest(PlaceController.class)
@ActiveProfiles("test")
class PlaceControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PlaceService placeService;

    @MockBean
    PlaceRepository placeRepository;

    @MockBean
    PlaceImageRepository placeImageRepository;

    @MockBean
    AdminRepository adminRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("GET /places")
    void list() throws Exception {
        given(placeRepository.findAll()).willReturn(
                List.of(Place.fake("0001-ID"))
        );

        mockMvc.perform(get("/places"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"places\":["))
                );
    }

    @Test
    @DisplayName("GET /places/{id}")
    void detail() throws Exception {
        PlaceId placeId = new PlaceId("0001-ID");

        given(placeRepository.findPlaceWithNonDeletedImages(placeId)).willReturn(
                Optional.of(Place.fake("0001-ID"))
        );

        mockMvc.perform(get("/places/0001-ID"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"place\":{")
                ));
    }

    @Test
    @DisplayName("DELETE /places/image/{id}")
    void remove() throws Exception {
        PlaceImageId placeImageId = new PlaceImageId("0001-ID");

        AdminId adminId = new AdminId("authorized-id");
        String accessToken = jwtUtil.encode(adminId);

        given(adminRepository.findById(adminId))
                .willReturn(Optional.of(Admin.fake("01012345678", Grade.LV1)));

        given(placeImageRepository.findById(placeImageId))
                .willReturn(Optional.of(
                        PlaceImage.builder()
                                .placeId(new PlaceId("0001-PLACE-ID"))
                                .url("test")
                                .id(placeImageId)
                                .build()
                ));

        mockMvc.perform(delete("/places/image/0001-ID")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());

        verify(placeService).deleteImage(placeImageId);
    }

    @Test
    @DisplayName("POST /places")
    void create() throws Exception {
        String json = """
                {
                  "name": "SK엔크린 대성주유소",
                  "address1": "처인구 남사읍 경기동로 490",
                  "address2": "",   
                  "latitude": 37.1391291,
                  "longitude": 127.17664,
                  "introduction": "1) 중부대로에서 대형 진입로를 활용하여 쉽게 진입 가능\\n2) 용인GS자이 아파트에서 300미터, 도보 4분 (도보 출퇴근 가능)\\n3) CCTV 설치를 통한 보안 관리",
                  "precautions": "물세차 금지\\n쓰레기 무단 투척 금지\\n자가 정비 금지 (주차 용도로만 사용)\\n흡연은 지정된 장소 (흡연장)\\n주의사항 2회 이상 어길 시, 퇴거 조치\\n",
                  "spaces": {
                    "total": 10,
                    "free": 2
                  },
                  "plans": [
                    {
                      "price": 200000,
                      "type": "monthly"
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/places")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isCreated());

        verify(placeService).createPlace(any());
    }

    @Test
    @DisplayName("PATCH /places")
    void update() throws Exception {
        String json = """
                {
                  "id": "0001-ID",
                  "name": "LG엔크린 대성주유소",
                  "address1": "처인구 남사읍 경기동로 490",
                  "address2": "",   
                  "latitude": 37.1391291,
                  "longitude": 127.17664,
                  "introduction": "1) 중부대로에서 대형 진입로를 활용하여 쉽게 진입 가능\\n2) 용인GS자이 아파트에서 300미터, 도보 4분 (도보 출퇴근 가능)\\n3) CCTV 설치를 통한 보안 관리",
                  "precautions": "물세차 금지\\n쓰레기 무단 투척 금지\\n자가 정비 금지 (주차 용도로만 사용)\\n흡연은 지정된 장소 (흡연장)\\n주의사항 2회 이상 어길 시, 퇴거 조치\\n",
                  "spaces": {
                    "total": 10,
                    "free": 2
                  },
                  "plans": [
                    {
                      "price": 200000,
                      "type": "monthly"
                    }
                  ]
                }
                """;

        mockMvc.perform(patch("/places")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }
}
