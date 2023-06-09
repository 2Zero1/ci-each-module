package com.truckhelper.application.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import jakarta.transaction.Transactional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.truckhelper.core.models.NotificationId;
import com.truckhelper.core.models.RentalId;

@RestController
@RequestMapping("backdoor")
@Transactional
public class BackdoorController {
    private final JdbcTemplate jdbcTemplate;

    public BackdoorController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("setup-database")
    public String setupDatabase() {
        jdbcTemplate.execute("DELETE FROM rentals");
        jdbcTemplate.execute("DELETE FROM place_images");
        jdbcTemplate.execute("DELETE FROM places");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("DELETE FROM notifications");

        createPlaces();
        createPlaceImages();
        createUsers();

        return "OK";
    }

    @GetMapping("clear-places")
    public String clearPlaces() {
        jdbcTemplate.execute("DELETE FROM rentals");
        jdbcTemplate.execute("DELETE FROM place_images");
        jdbcTemplate.execute("DELETE FROM places");

        return "OK";
    }

    @GetMapping("add-rental")
    public String addRental(
            @RequestParam String placeId,
            @RequestParam String plan,
            @RequestParam String beginningDate,
            @RequestParam String status
    ) {
        createRental(placeId, plan, beginningDate, status);

        return "OK";
    }

    @GetMapping("add-notification")
    public String addNotification(
            @RequestParam String title,
            @RequestParam String contents
    ) {
        createNotification(title, contents);

        return "OK";
    }

    private void createPlaces() {
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update("" +
                        "INSERT INTO places(" +
                        "   id, name, address1, address2," +
                        "   latitude, longitude," +
                        "   monthly_price, yearly_price," +
                        "   introduction, precautions," +
                        "   total_spaces, free_spaces," +
                        "   hidden, created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                "000100000000000000000PLACE",
                "복성칼국수",
                "용인시 처인구 중부대로 1595", "",
                37.2335787, 127.2217128,
                150_000L, 1_650_000L,
                "많은 이용 부탁드립니다.",
                "물세차 금지\n쓰레기 무단 투척 금지\n자가 정비 금지\n",
                6, 5, false, now, now
        );

        jdbcTemplate.update("" +
                        "INSERT INTO places(" +
                        "   id, name, address1, address2," +
                        "   latitude, longitude," +
                        "   monthly_price, yearly_price," +
                        "   introduction, precautions," +
                        "   total_spaces, free_spaces," +
                        "   hidden, created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                "000200000000000000000PLACE",
                "SK엔크린 대성주유소",
                "처인구 남사읍 경기동로 490", "",
                37.1391291, 127.17664,
                200_000L, 0L,
                "많은 이용 부탁드립니다.",
                "물세차 금지\n쓰레기 무단 투척 금지\n자가 정비 금지\n",
                9, 5, false, now, now
        );

        jdbcTemplate.update("" +
                        "INSERT INTO places(" +
                        "   id, name, address1, address2," +
                        "   latitude, longitude," +
                        "   monthly_price, yearly_price," +
                        "   introduction, precautions," +
                        "   total_spaces, free_spaces," +
                        "   hidden, created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                "000300000000000000000PLACE",
                "용인휴게소",
                "경기 용인시 처인구 주북로94번길 30-1", "",
                37.2455787, 127.2419807,
                500_000L, 5_500_000L,
                "많은 이용 부탁드립니다.",
                "물세차 금지\n쓰레기 무단 투척 금지\n자가 정비 금지\n",
                9, 5, true, now, now
        );
    }

    private void createPlaceImages() {
        LocalDateTime now = LocalDateTime.now();

        String[] imageUrls = {
                "https://res.cloudinary.com/truckhelper/image/upload/places/000100010000000000000IMAGE.jpg",
                "https://res.cloudinary.com/truckhelper/image/upload/places/000100020000000000000IMAGE.jpg",
                "https://res.cloudinary.com/truckhelper/image/upload/places/000100030000000000000IMAGE.jpg",
                "https://res.cloudinary.com/truckhelper/image/upload/places/000200010000000000000IMAGE.jpg",
                "https://res.cloudinary.com/truckhelper/image/upload/places/000200020000000000000IMAGE.jpg",
                "https://res.cloudinary.com/truckhelper/image/upload/places/000200030000000000000IMAGE.jpg",
                "https://res.cloudinary.com/truckhelper/image/upload/places/000300010000000000000IMAGE.jpg",
        };

        jdbcTemplate.update("" +
                        "INSERT INTO place_images(" +
                        "   id, place_id, url, sequence," +
                        "   created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?)",
                "000100010000000000000IMAGE",
                "000100000000000000000PLACE",
                imageUrls[0], 2, now, now
        );

        jdbcTemplate.update("" +
                        "INSERT INTO place_images(" +
                        "   id, place_id, url, sequence," +
                        "   created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?)",
                "000100020000000000000IMAGE",
                "000100000000000000000PLACE",
                imageUrls[1], 1, now, now
        );

        jdbcTemplate.update("" +
                        "INSERT INTO place_images(" +
                        "   id, place_id, url, sequence," +
                        "   created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?)",
                "000100030000000000000IMAGE",
                "000100000000000000000PLACE",
                imageUrls[2], 3, now, now
        );

        jdbcTemplate.update("" +
                        "INSERT INTO place_images(" +
                        "   id, place_id, url, sequence," +
                        "   created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?)",
                "000200010000000000000IMAGE",
                "000200000000000000000PLACE",
                imageUrls[3], 1, now, now
        );

        jdbcTemplate.update("" +
                        "INSERT INTO place_images(" +
                        "   id, place_id, url, sequence," +
                        "   created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?)",
                "000200020000000000000IMAGE",
                "000200000000000000000PLACE",
                imageUrls[4], 2, now, now
        );

        jdbcTemplate.update("" +
                        "INSERT INTO place_images(" +
                        "   id, place_id, url, sequence," +
                        "   created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?)",
                "000200030000000000000IMAGE",
                "000200000000000000000PLACE",
                imageUrls[5], 3, now, now
        );

        jdbcTemplate.update("" +
                        "INSERT INTO place_images(" +
                        "   id, place_id, url, sequence," +
                        "   created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?)",
                "000300010000000000000IMAGE",
                "000300000000000000000PLACE",
                imageUrls[6], 1, now, now
        );
    }

    private void createUsers() {
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update("" +
                        "INSERT INTO users(" +
                        "   id, name, phone_number, email," +
                        "   address1, address2, latitude, longitude," +
                        "   vehicle_plate, manufacturer, car_model," +
                        "   vehicle_type, loading_weight, loading_length," +
                        "   length, height, width," +
                        "   created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                        "         ?, ?, ?, ?, ?, ?, ?, ?)",
                "kakao-tester", "테스터", "01012345678", "tester@example.com",
                "경기도 성남시 분당구 삼평동 585", "", 37.400418, 127.0935768,
                "123가 4568", "현대", "메가트럭", "카고", "5톤", "5.5m",
                "7.2m", "2.5m", "2.35m", now, now
        );

        jdbcTemplate.update("" +
                        "INSERT INTO users(" +
                        "   id, name, phone_number, email," +
                        "   address1, address2, latitude, longitude," +
                        "   vehicle_plate, manufacturer, car_model," +
                        "   vehicle_type, loading_weight, loading_length, " +
                        "   length, height, width," +
                        "   created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                        "         ?, ?, ?, ?, ?, ?, ?, ?)",
                "kakao-clark", "클라크", "01011112222", "clark@example.com",
                "경기도 성남시 분당구 삼평동 585", "", 37.400418, 127.0935768,
                "123가 4568", "현대", "포터 II", "탑차", "5톤", "5.5m",
                "7.2m", "2.5m", "2.35m", now, now
        );
    }

    private void createRental(String placeId, String plan,
                              String beginningDate, String status) {
        LocalDateTime now = LocalDateTime.now();
        RentalId id = RentalId.generate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(beginningDate, formatter);

        LocalDateTime beginningDateTime = date.atTime(19, 0, 0);
        LocalDateTime endDateTime = date.plus(
                1,
                plan.equals("monthly") ? ChronoUnit.MONTHS : ChronoUnit.YEARS
        ).atTime(8, 0, 0);

        jdbcTemplate.update("" +
                        "INSERT INTO rentals(" +
                        "   id, user_id, place_id, status, plan," +
                        "   beginning_date, end_date, price, method," +
                        "   receipt_id, paid_at, created_at, updated_at" +
                        ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id.toString(), "kakao-tester", placeId, "paid", plan,
                beginningDateTime, endDateTime, 500_000L, "card",
                status.equals("new") ? "" : "63058829cf9f6d001f439f79",
                status.equals("new") ? null : beginningDateTime.minusHours(4),
                beginningDateTime, now
        );
    }

    private void createNotification(String title, String contents) {
        LocalDateTime now = LocalDateTime.now();
        NotificationId id = NotificationId.generate();

        jdbcTemplate.update("" +
                        "INSERT INTO notifications(" +
                        "   id, user_id, title, contents, created_at" +
                        ") VALUES(?, ?, ?, ?, ?)",
                id.toString(), "kakao-tester", title, contents, now
        );
    }
}
