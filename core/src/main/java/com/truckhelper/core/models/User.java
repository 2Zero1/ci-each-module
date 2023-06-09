package com.truckhelper.core.models;

import java.time.LocalDateTime;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
public class User {
    @EmbeddedId
    private UserId id;

    @Embedded
    private Person person;

    @Embedded
    private Truck truck;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private User() {
    }

    public User(UserId id, Person person, Truck truck) {
        this.id = id;
        this.person = person;
        this.truck = truck;
    }

    public Person person() {
        return this.person;
    }

    public Truck truck() {
        return this.truck;
    }

    public void changePerson(Person person) {
        this.person = person;
    }

    public void changeTruck(Truck truck) {
        this.truck = truck;
    }

    public static User fake() {
        return User.fake(new UserId("kakao-123"));
    }

    public static User fake(UserId userId) {
        return new User(userId, Person.fake(), Truck.fake());
    }
}
