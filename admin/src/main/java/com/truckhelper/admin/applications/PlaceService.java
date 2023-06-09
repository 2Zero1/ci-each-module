package com.truckhelper.admin.applications;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.truckhelper.admin.exceptions.PlaceImageNotFound;
import com.truckhelper.admin.exceptions.PlaceNotFound;
import com.truckhelper.admin.repositories.PlaceImageRepository;
import com.truckhelper.admin.repositories.PlaceRepository;
import com.truckhelper.admin.utils.ImageUploader;
import com.truckhelper.core.models.Address;
import com.truckhelper.core.models.GeoPosition;
import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.PlaceDescription;
import com.truckhelper.core.models.PlaceId;
import com.truckhelper.core.models.PlaceImage;
import com.truckhelper.core.models.PlaceImageId;
import com.truckhelper.core.models.PlaceSpaces;
import com.truckhelper.core.models.Plan;

@Service
@Transactional
public class PlaceService {
    private PlaceRepository placeRepository;

    private final PlaceImageRepository placeImageRepository;

    private ImageUploader imageUploader;

    public PlaceService(
            PlaceRepository placeRepository,
            PlaceImageRepository placeImageRepository,
            ImageUploader imageUploader
    ) {
        this.placeRepository = placeRepository;
        this.placeImageRepository = placeImageRepository;
        this.imageUploader = imageUploader;
    }

    public void createPlace(Place place) {
        this.placeRepository.save(place);
    }

    public void updatePlace(
            PlaceId id,
            String name,
            Address address,
            GeoPosition position,
            PlaceDescription description,
            PlaceSpaces spaces,
            List<Plan> plans,
            boolean hidden
    ) {
        Place place = this.placeRepository.findById(id).orElseThrow();
        place.changeName(name);
        place.changeAddress(address);
        place.changePosition(position);
        place.changeDescription(description);
        place.changeSpaces(spaces);
        place.changePlans(plans);
        place.changeHidden(hidden);
    }

    public void uploadImage(PlaceId placeId, MultipartFile file) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFound(placeId));

        List<PlaceImage> images = place.images();

        int sequence;

        if (images.size() == 0) {
            sequence = 0;
        } else {
            int max = images.stream().filter((image) -> !image.isDeleted())
                    .map((image) -> image.sequence()).max(Comparator.naturalOrder())
                    .orElse(0);

            sequence = max + 1;
        }

        String imageUrl = imageUploader.upload("places", file);

        PlaceImage image = PlaceImage.builder()
                .placeId(placeId)
                .id(PlaceImageId.generate())
                .url(imageUrl)
                .sequence(sequence)
                .build();

        place.addImage(image);

        placeRepository.save(place);
    }

    public void updateImageOrder(PlaceId placeId, int from, int to) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFound(placeId));

        place.moveImageTo(from, to);

        place.images().forEach((i) -> placeImageRepository.save(i));

        placeRepository.save(place);
    }

    public PlaceImage deleteImage(PlaceImageId placeImageId) {
        PlaceImage image = placeImageRepository
                .findById(placeImageId)
                .orElseThrow(() -> new PlaceImageNotFound(placeImageId));

        Place place = placeRepository.findById(image.placeId()).get();

        place.deleteImage(image);

        placeRepository.save(place);

        return image;
    }
}
