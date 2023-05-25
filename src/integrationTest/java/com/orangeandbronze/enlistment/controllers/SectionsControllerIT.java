package com.orangeandbronze.enlistment.controllers;

import com.orangeandbronze.enlistment.domain.*;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.*;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static com.orangeandbronze.enlistment.domain.Days.MTH;
import static com.orangeandbronze.enlistment.domain.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


class SectionsControllerIT extends AbstractControllerIT  {
    @Autowired
    private SectionRepository sectionRepository;

    // No need for multi-threaded test, unlikely that work of two or more admins will collide
    @Test
    void createSection_save_to_db() throws Exception {
        insertDefaultRoomWithCapacity(10);
        insertDefaultSubject();
        insertDefaultFaculty();

        insertSectionWithTime(mockMvc, DEFAULT_SECTION_ID, TIME830, TIME1000);
        int count = getSectionsCount(DEFAULT_SECTION_ID);
        assertEquals(1, count);
    }

    @Test
    void create_section_same_room_and_schedule_concurrent() throws Exception {
        int startCount = getSectionsCount();
        insertDefaultRoomWithCapacity(10);
        insertDefaultSubject();
        insertDefaultFaculty();

        startSectionThreadsSameSchedule(true);
        int count = getSectionsCount();
        assertEquals(1, count-startCount);
    }
    @Test
    void create_section_same_room_different_schedule() throws Exception {
        int startCount = getSectionsCount();
        insertDefaultRoomWithCapacity(10);
        insertDefaultSubject();
        insertDefaultFaculty();

        startSectionThreadDifferentSchedule(true  );
        int count = getSectionsCount();
        assertEquals(NUM, count- startCount  );
    }

    @Test
    void create_section_same_faculty_same_schedule() throws Exception {
        int startCount = getSectionsCount();
        insertRooms();
        insertDefaultFaculty();
        insertDefaultSubject();
        startSectionThreadsSameSchedule(false);

        int count = getSectionsCount();
        assertEquals(1, count- startCount  );
    }

    @Test
    void create_section_same_faculty_different_schedule() throws Exception {
        int startCount = getSectionsCount();
        insertRooms();
        insertDefaultFaculty();
        insertDefaultSubject();
        startSectionThreadDifferentSchedule(false);

        int count = getSectionsCount();
        assertEquals(NUM, count- startCount  );
    }
    private Integer getSectionsCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM section", Integer.class);
    }
    private Integer getSectionsCount(String sectionId){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM section WHERE section_id = ?", Integer.class, sectionId);
    }

    private void insertDefaultSubject() {
        jdbcTemplate.update("INSERT INTO subject (subject_id) VALUES (?)", DEFAULT_SUBJECT.toString());
    }

    private void insertDefaultRoomWithCapacity(int capacity) {
        jdbcTemplate.update("INSERT INTO room (name, capacity) VALUES (?, ?)",
                DEFAULT_ROOM_NAME, capacity);
    }
    private void insertRooms(){
        for(int i = START_NUM;  i <= END_NUM; i++){
            jdbcTemplate.update("INSERT INTO room (name, capacity) VALUES (?, ?)",
                    String.valueOf(i), 10);
        }
    }

    private void insertDefaultFaculty(){
        jdbcTemplate.update("INSERT INTO faculty (faculty_number, first_name, last_name) VALUES (?, ?, ?)",
                DEFAULT_FACULTY_NUMBER, "firstname", "lastname");
    }

    private void startSectionThreadDifferentSchedule(boolean sameRoom) throws  Exception{
        CountDownLatch latch = new CountDownLatch(1);
        int startHour = 9;
        for (int i = START_NUM; i <= END_NUM; i++){
            LocalTime startTime;
            LocalTime endTime;
            if(i%2 == 0) {
                startTime = LocalTime.of(startHour, 0);
                endTime = LocalTime.of(startHour, 30);
            } else{
                startTime = LocalTime.of(startHour, 30);
                endTime = LocalTime.of(startHour+1, 0);
                startHour++;
            }
            new SectionThread(String.valueOf(i), latch, mockMvc, startTime, endTime,
                    sameRoom?DEFAULT_ROOM_NAME:String.valueOf(i)).start();
        }
        latch.countDown();
        Thread.sleep(5000);
    }

    private final static int START_NUM = 11;
    private final static int NUM = 10;
    private final static int END_NUM = START_NUM + NUM - 1;



    private void startSectionThreadsSameSchedule(boolean sameRoom) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = START_NUM; i <= END_NUM; i++) {
            new SectionThread(String.valueOf(i), latch, mockMvc, TIME830, TIME1000,
                    sameRoom?DEFAULT_ROOM_NAME:String.valueOf(i)).start();
        }
        latch.countDown();
        Thread.sleep(5000); // wait time to allow all the threads to finish
    }

    private static void insertSectionWithTime(MockMvc mockMvc, String sectionId, LocalTime start, LocalTime end, String roomName) throws Exception {
        mockMvc.perform(post("/sections").sessionAttr("admin", 1).param("sectionId", sectionId)
                .param("subjectId", DEFAULT_SUBJECT.toString())
                .param("facultyNumber", String.valueOf(DEFAULT_FACULTY_NUMBER))
                .param("days", String.valueOf(MTH))
                .param("start", String.valueOf(start))
                .param("end", String.valueOf(end))
                .param("roomName", roomName));
    }
    private static void insertSectionWithTime(MockMvc mockMvc, String sectionId, LocalTime start, LocalTime end) throws Exception {
        insertSectionWithTime(mockMvc, sectionId, start, end, DEFAULT_ROOM_NAME );
    }

    private static class SectionThread extends Thread {
        private final String sectionId;
        private final CountDownLatch latch;
        private final MockMvc mockMvc;
        private final LocalTime startTime;
        private final LocalTime  endTime;
        private final String roomName;


        public SectionThread(String sectionId, CountDownLatch latch, MockMvc mockMvc, LocalTime startTime, LocalTime endTime, String roomName) {
            this.sectionId = sectionId;
            this.latch = latch;
            this.mockMvc = mockMvc;
            this.startTime = startTime;
            this.endTime = endTime;
            this.roomName = roomName;
        }

        @Override
        public void run() {
            try {
                latch.await(); // The thread keeps waiting till it is informed
                 insertSectionWithTime(mockMvc, sectionId, startTime, endTime, roomName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    }
}
