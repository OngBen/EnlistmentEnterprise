package com.orangeandbronze.enlistment.controllers;

import com.orangeandbronze.enlistment.domain.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.servlet.mvc.support.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.orangeandbronze.enlistment.domain.Days.MTH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.orangeandbronze.enlistment.domain.TestUtils.*;

class SectionsControllerTest {

    @Test
    void createSection_save_new_section_to_repository() {
        // Given the controller, repositories & valid parameter arguments for creating a section
        // When the controller receives the arguments
        // Then
        // - it should retrieve the entities from the db, create a new section
        // - save the section in the db
        // - set a flash attribute called "sectionSuccessMessage" with the message "Successfully created new section " + sectionId
        // - return the string value "redirect:sections" to redirect to the GET method

        SectionsController controller = new SectionsController();
        SectionRepository sectionRepository = mock(SectionRepository.class);
        SubjectRepository subjectRepository = mock(SubjectRepository.class);
        RoomRepository roomRepository = mock(RoomRepository.class);
        FacultyRepository facultyRepository = mock(FacultyRepository.class);

        String subjectId = "STSWENG";
        String sectionId = "ABC";
        String roomName = "AG1110";
        int facultyNumber = 1;
        Days days = MTH;


        LocalTime startTime = LocalTime.of(9, 00);
        LocalTime endTime = LocalTime.of(10, 30);

        String start = startTime.toString();
        String end = endTime.toString();


        Subject subject = new Subject(subjectId);
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));

        Room room = new Room(roomName,45);
        when(roomRepository.findById(roomName)).thenReturn(Optional.of(room));

        Schedule schedule = new Schedule(days, new Period(startTime, endTime));

        Faculty faculty = new Faculty(1);
        when(facultyRepository.findById(1)).thenReturn(Optional.of(faculty));

        Section section = new Section(sectionId, subject, schedule, room, faculty);
        when(sectionRepository.save(section)).thenReturn(section);
        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));

        subjectRepository.save(subject);
        roomRepository.save(room);

        room.removeSection(section);
        faculty.removeSection(section);

        controller.setSectionRepo(sectionRepository);
        controller.setSubjectRepo(subjectRepository);
        controller.setRoomRepo(roomRepository);
        controller.setFacultyRepository(facultyRepository);
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        String returnVal = controller.createSection(sectionId, subjectId, facultyNumber, days, start, end, roomName, redirectAttrs);

        verify(sectionRepository).save(section);

        assertEquals ("redirect:sections", returnVal);
    }
}
