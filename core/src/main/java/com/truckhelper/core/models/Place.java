package com.truckhelper.core.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import lombok.Builder;

@Entity
@Table(name = "places")
public class Place {
    @EmbeddedId
    private PlaceId id;

    private String name;

    @Embedded
    private Address address;

    @Embedded
    private GeoPosition position;

    @Embedded
    private PlaceDescription description;

    @Embedded
    private PlaceSpaces spaces;

    @Column(name = "monthly_price")
    private Money monthlyPrice = Money.ZERO;

    @Column(name = "yearly_price")
    private Money yearlyPrice = Money.ZERO;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id")
    @OrderBy("sequence")
    @Where(clause = "deleted = false")
    private List<PlaceImage> images = new ArrayList<>();

    @Column(name = "hidden", nullable = false)
    @ColumnDefault("false")
    private boolean hidden = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Transient
    private Double distance;

    private Place() {
    }

    @Builder
    public Place(
            PlaceId id,
            String name,
            Address address,
            GeoPosition position,
            PlaceDescription description,
            PlaceSpaces spaces,
            List<Plan> plans,
            boolean hidden
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.position = position;
        this.description = description;
        this.spaces = spaces;
        plans.stream().filter(plan -> plan.is(PlanType.Monthly)).findFirst()
                .map(plan -> monthlyPrice = plan.price());
        plans.stream().filter(plan -> plan.is(PlanType.Yearly)).findFirst()
                .map(plan -> yearlyPrice = plan.price());
        this.hidden = hidden;
    }

    public PlaceId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public List<PlaceImage> images() {
        return images;
    }

    public boolean hidden() {
        return this.hidden;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeAddress(Address address) {
        this.address = address;
    }

    public void changePosition(GeoPosition position) {
        this.position = position;
    }

    public void changeDescription(PlaceDescription description) {
        this.description = description;
    }

    public void changeSpaces(PlaceSpaces spaces) {
        this.spaces = spaces;
    }

    public void changePlans(List<Plan> plans) {
        plans.forEach((plan) -> {
            if (plan.is(PlanType.Monthly)) {
                changeMonthlyPrice(plan.price());
                return;
            }
            changeYearlyPrice(plan.price());
        });
    }

    private void changeMonthlyPrice(Money monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    private void changeYearlyPrice(Money yearlyPrice) {
        this.yearlyPrice = yearlyPrice;
    }

    public void changeHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void moveImageTo(int from, int to) {
        PlaceImage image = images.stream().filter((i) -> i.sequence() == from)
                .findFirst().get();

        List<PlaceImage> notDeletedImages = new ArrayList<>();

        List<PlaceImage> sortedImages = images.stream()
                .filter((i) -> !i.isDeleted())
                .sorted(Comparator.comparing(PlaceImage::sequence))
                .collect(Collectors.toList());

        if (from < to) {
            notDeletedImages.addAll(sortedImages.subList(0, from));
            notDeletedImages.addAll(sortedImages.subList(from + 1, to + 1));
            notDeletedImages.add(image);
            notDeletedImages.addAll(sortedImages.subList(to + 1, sortedImages.size()));
        } else {
            notDeletedImages.addAll(sortedImages.subList(0, to));
            notDeletedImages.add(image);
            notDeletedImages.addAll(sortedImages.subList(to, from));
            notDeletedImages.addAll(sortedImages.subList(from + 1, sortedImages.size()));
        }

        for (int i = 0; i < notDeletedImages.size(); i++) {
            PlaceImageId id = notDeletedImages.get(i).id();

            PlaceImage target = this.images.stream()
                    .filter((v) -> v.id().equals(id)).findFirst().get();

            target.changeSequence(i);
        }
    }

    public void addImage(PlaceImage image) {
        this.images.add(image);
    }

    public void deleteImage(PlaceImage image) {
        List<PlaceImage> images = images().stream()
                .filter((i) -> !i.equals(image))
                .sorted(Comparator.comparing(PlaceImage::sequence))
                .collect(Collectors.toList());

        for (int i = 0; i < images.size(); i++) {
            images.get(i).changeSequence(i);
        }

        image.delete();
    }

    public Optional<Plan> plan(PlanType planType) {
        Money price = planType.equals(PlanType.Monthly)
                ? monthlyPrice
                : yearlyPrice;
        if (price.equals(Money.ZERO)) {
            return Optional.empty();
        }
        return Optional.of(new Plan(planType, price));
    }

    public Double distance() {
        return distance;
    }

    public static Place fake(String id) {
        return Place.fake(new PlaceId(id));
    }

    public static Place fake(PlaceId id) {
        return Place.builder()
                .id(id)
                .name("A1 Center")
                .address(new Address("Seongsu-dong", "B1"))
                .position(GeoPosition.SEOUL)
                .description(
                        new PlaceDescription("Introduction", "Precautions"))
                .spaces(new PlaceSpaces(20, 7))
                .plans(List.of(
                        new Plan(PlanType.Monthly, Money.krw(500_000L))))
                .build();
    }
}
