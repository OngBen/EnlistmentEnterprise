package com.orangeandbronze.enlistment.controllers;

import com.orangeandbronze.enlistment.domain.*;
import org.hibernate.*;
import org.junit.jupiter.api.*;

import javax.persistence.*;
import java.util.*;

import static com.orangeandbronze.enlistment.domain.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class EnlistControllerTest {

    @Test
    void enlist_student_in_section() {
        Student student = mock(newDefaultStudent().getClass());
        String sectionId = "X";
        UserAction userAction = UserAction.ENLIST;

        SectionRepository sectionRepository = mock(SectionRepository.class);
        Section section = new Section (sectionId, new Subject("X"), MTH830to10, new Room("X", 10), newFaculty(1));
        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));
        StudentRepository studentRepository = mock(StudentRepository.class);

        EnlistController controller = new EnlistController();
        controller.setSectionRepo(sectionRepository);
        controller.setStudentRepo(studentRepository);
        EntityManager entityManager = mock(EntityManager.class);
        Session session = mock(Session.class);
        when(entityManager.unwrap(Session.class)).thenReturn((session));
        controller.setEntityManager(entityManager);
        String returnVal = controller.enlistOrCancel(student, sectionId, userAction);

        verify(sectionRepository).findById(sectionId);
        verify(student).enlist(section);
        verify(studentRepository).save(student);
        verify(sectionRepository).save(section);
        assertEquals ("redirect:enlist", returnVal);
    }

}
