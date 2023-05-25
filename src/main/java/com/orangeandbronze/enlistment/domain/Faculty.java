package com.orangeandbronze.enlistment.domain;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.*;

@Entity
public class Faculty {
    @Id
    private final int facultyNumber;
    @Column(columnDefinition = "text")
    private final String firstName;
    @Column(columnDefinition = "text")
    private final String lastName;
    @OneToMany
    private final Collection<Section> sections = new HashSet<>();

    @Version
    @ColumnDefault("0")
    private final int version = 0;

    public Faculty(int facultyNumber, String lastName, String firstName, Collection<Section> sections) {
        isTrue(facultyNumber>=0, "facultyNumber must be non-negative, was: "+ facultyNumber);
        notBlank(lastName);
        notBlank(firstName);
        notNull(sections);
        this.facultyNumber = facultyNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.sections.addAll(sections);
        this.sections.removeIf(Objects::isNull);
    }

    public Faculty(int facultyNumber, String firstName, String lastName){
        this(facultyNumber, firstName, lastName, Collections.emptyList());
    }

    void addSection(Section newSection) {
        notNull(newSection);
        sections.forEach(currSection -> currSection.checkForScheduleConflict(newSection, true));
        sections.add(newSection);
    }

    public void removeSection (Section newSection){
        notNull(newSection);
        sections.remove(newSection);
    }

    public Faculty(int facultyNumber) {
        this(facultyNumber, "Name", "Default", Collections.emptyList());
    }

    public int getFacultyNumber() {
        return facultyNumber;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFirstName() {
        return firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return facultyNumber == faculty.facultyNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(facultyNumber);
    }

    @Override
    public String toString() {
        return lastName + ", " + firstName;
    }

    private Faculty(){
        facultyNumber = -1;
        lastName = null;
        firstName = null;

    }
}
