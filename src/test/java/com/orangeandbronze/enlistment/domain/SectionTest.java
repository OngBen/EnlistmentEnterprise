package com.orangeandbronze.enlistment.domain;

import org.junit.jupiter.api.*;

import static com.orangeandbronze.enlistment.domain.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class SectionTest {

    @Test
    void newSection_same_room_diff_sked() {
        Room room = new Room("X", 10);

        new Section("A", DEFAULT_SUBJECT, MTH830to10, room, newFaculty(1));
        assertDoesNotThrow(() -> new Section("B", DEFAULT_SUBJECT, TF10to1130, room, newFaculty(2)));
    }

    @Test
    void newSection_same_room_same_sked() {
        Room room = new Room("X", 10);
        new Section("A", DEFAULT_SUBJECT, MTH830to10, room, newFaculty(1));
        assertThrows(ScheduleConflictException.class, () -> new Section("B", DEFAULT_SUBJECT, MTH830to10, room, newFaculty(2)));
    }

    @Test
    void two_sections_same_instructor_diff_sked() {
        Faculty instructor = new Faculty(1);

        new Section("A", new Subject("C"), MTH830to10, new Room("X", 10), instructor);
        assertDoesNotThrow(() -> new Section("B", new Subject("D"), TF10to1130, new Room("Y", 10), instructor));
    }

    @Test
    void two_sections_same_instructor_same_sked() {
        Faculty instructor = new Faculty(1);

        new Section("A", new Subject("C"), MTH830to10, new Room("X", 10), instructor);
        assertThrows(ScheduleConflictException.class, () -> new Section("B", new Subject("D"), MTH830to10, new Room("Y", 10), instructor));
    }



}
