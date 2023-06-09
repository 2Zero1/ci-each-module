package com.truckhelper.admin.applications;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.truckhelper.admin.repositories.PlaceImageRepository;
import com.truckhelper.admin.repositories.PlaceRepository;
import com.truckhelper.admin.utils.ImageUploader;
import com.truckhelper.core.models.Address;
import com.truckhelper.core.models.GeoPosition;
import com.truckhelper.core.models.Money;
import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.PlaceDescription;
import com.truckhelper.core.models.PlaceId;
import com.truckhelper.core.models.PlaceImage;
import com.truckhelper.core.models.PlaceImageId;
import com.truckhelper.core.models.PlaceSpaces;
import com.truckhelper.core.models.Plan;
import com.truckhelper.core.models.PlanType;

class PlaceServiceTest {
    private PlaceService placeService;

    private PlaceRepository placeRepository;

    private PlaceImageRepository placeImageRepository;

    private ImageUploader imageUploader;

    MockMultipartFile file;

    @BeforeEach
    void setup() {
        placeRepository = mock(PlaceRepository.class);

        imageUploader = mock(ImageUploader.class);

        placeImageRepository = mock(PlaceImageRepository.class);

        placeService = new PlaceService(
                placeRepository,
                placeImageRepository,
                imageUploader
        );
    }

    @Test
    void createPlace() {
        placeService.createPlace(Place.fake("00001-ID-PLACE"));

        verify(placeRepository).save(any(Place.class));
    }

    @Test
    void updatePlace() {
        String id = "00001-ID-PLACE";

        Place place = Place.fake(id);

        given(placeRepository.findById(new PlaceId(id)))
                .willReturn(Optional.of(place));

        placeService.updatePlace(
                new PlaceId(id),
                "TEST1",
                new Address("address1", "address2"),
                new GeoPosition(1.111, 2.2222),
                new PlaceDescription("introduction", "precautions"),
                new PlaceSpaces(2, 1),
                List.of(new Plan(PlanType.Monthly, Money.krw(10000L))),
                true
        );

        assertThat(place.hidden()).isEqualTo(true);
    }

    @Test
    void uploadImage() {
        file = new MockMultipartFile(
                "file",
                "hello.png",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        Place place = Place.fake("PLACE-ID-01");

        place.addImage(PlaceImage.fake("PLACE-ID-01", 0));
        place.addImage(PlaceImage.fake("PLACE-ID-01", 1));
        place.addImage(PlaceImage.fake("PLACE-ID-01", 2));

        given(placeRepository.findById(new PlaceId("PLACE-ID-01")))
                .willReturn(Optional.of(place));

        placeService.uploadImage(new PlaceId("PLACE-ID-01"), file);

        verify(imageUploader).upload(any(), any());

        assertThat(place.images().size()).isEqualTo(4);
        assertThat(
                place.images().stream().filter((v) -> v.sequence() == 3).count()
        ).isEqualTo(1);
    }

    @Test
    void uploadImageWhenIncludesDeleted() {
        file = new MockMultipartFile(
                "file",
                "hello.png",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        Place place = Place.fake("PLACE-ID-01");

        place.addImage(PlaceImage.fake("PLACE-ID-01", 0, true));
        place.addImage(PlaceImage.fake("PLACE-ID-01", 0));
        place.addImage(PlaceImage.fake("PLACE-ID-01", 1));

        given(placeRepository.findById(new PlaceId("PLACE-ID-01")))
                .willReturn(Optional.of(place));

        placeService.uploadImage(new PlaceId("PLACE-ID-01"), file);

        verify(imageUploader).upload(any(), any());

        assertThat(place.images().size()).isEqualTo(4);
        assertThat(
                place.images().stream().filter((v) -> v.sequence() == 2).count()
        ).isEqualTo(1);
    }

    @Test
    void deleteImage() {
        file = new MockMultipartFile(
                "file",
                "hello.png",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        Place place = Place.fake("PLACE-ID-01");

        PlaceImage image1 = PlaceImage.fake("PLACE-ID-01", "PLACE-IMAGE-ID-01", 0);
        PlaceImage image2 = PlaceImage.fake("PLACE-ID-01", "PLACE-IMAGE-ID-02", 1);
        PlaceImage image3 = PlaceImage.fake("PLACE-ID-01", "PLACE-IMAGE-ID-03", 2);

        place.addImage(image1);
        place.addImage(image2);
        place.addImage(image3);

        given(placeRepository.findById(new PlaceId("PLACE-ID-01")))
                .willReturn(Optional.of(place));

        given(placeImageRepository.findById(new PlaceImageId("PLACE-IMAGE-ID-01")))
                .willReturn(Optional.of(image1));

        placeService.deleteImage(new PlaceImageId("PLACE-IMAGE-ID-01"));

        PlaceImage updatedImage1 = place.images().stream()
                .filter((i) -> i.id().equals(
                        new PlaceImageId("PLACE-IMAGE-ID-02"))).findFirst().get();

        PlaceImage updatedImage2 = place.images().stream()
                .filter((i) -> i.id().equals(
                        new PlaceImageId("PLACE-IMAGE-ID-03"))).findFirst().get();

        assertThat(updatedImage1.sequence()).isEqualTo(0);
        assertThat(updatedImage2.sequence()).isEqualTo(1);
    }
}
