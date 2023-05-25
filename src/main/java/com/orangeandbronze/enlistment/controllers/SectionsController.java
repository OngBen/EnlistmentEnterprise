package com.orangeandbronze.enlistment.controllers;

import com.orangeandbronze.enlistment.domain.*;
import com.orangeandbronze.enlistment.domain.Period;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import javax.transaction.Transactional;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Transactional
@Controller
@RequestMapping("sections")
@SessionAttributes("admin")
class SectionsController {

    @Autowired
    private SubjectRepository subjectRepo;
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private RoomRepository roomRepo;
    @Autowired
    private SectionRepository sectionRepo;
    @Autowired
    private FacultyRepository facultyRepository;

    @ModelAttribute("admin")
    public Admin admin(Integer id) {
        return adminRepo.findById(id).orElseThrow(() -> new NoSuchElementException("no admin found for adminId " + id));
    }

    @GetMapping
    public String showPage(Model model, Integer id) {
        Admin admin = id == null ? (Admin) model.getAttribute("admin") :
                adminRepo.findById(id).orElseThrow(() -> new NoSuchElementException("no admin found for adminId " + id));
        model.addAttribute("admin", admin);
        model.addAttribute("subjects", subjectRepo.findAll());
        model.addAttribute("rooms", roomRepo.findAll());
        model.addAttribute("sections", sectionRepo.findAll());
        model.addAttribute("instructors", facultyRepository.findAll());
        return "sections";
    }

    @Retryable(value = ObjectOptimisticLockingFailureException.class, backoff =  @Backoff(multiplier = 2f, random = true, delay = 500, maxDelay = 2000))
    @PostMapping
    public String createSection(@RequestParam String sectionId, @RequestParam String subjectId,  @RequestParam int facultyNumber,
                                @RequestParam Days days, @RequestParam String start, @RequestParam String end, @RequestParam String roomName,
                                RedirectAttributes redirectAttrs) {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("H:m");
        LocalTime startTime = LocalTime.parse(start, parser);
        LocalTime endTime = LocalTime.parse(end, parser);

        Schedule schedule = new Schedule(days, new Period(startTime, endTime));
        Subject subject = subjectRepo.findById(subjectId).orElseThrow(() -> new NoSuchElementException("no such element found with subject ID " + subjectId));
        Room room = roomRepo.findById(roomName).orElseThrow(() -> new NoSuchElementException("no such element found with room name " + roomName));
        Faculty instructor = facultyRepository.findById(facultyNumber).orElseThrow(()->new NoSuchElementException("No faculty found for faculty number "+ facultyNumber));
        Section section = new Section(sectionId, subject, schedule, room, instructor);
        sectionRepo.save(section);
        redirectAttrs.addFlashAttribute("sectionSuccessMessage", "Successfully created new section " + sectionId);
        return "redirect:sections";
    }

    @ExceptionHandler(EnlistmentException.class)
    public String handleException(RedirectAttributes redirectAttrs, EnlistmentException e) {
        redirectAttrs.addFlashAttribute("sectionExceptionMessage", e.getMessage());
        return "redirect:sections";
    }

    void setSubjectRepo(SubjectRepository subjectRepo) {
        this.subjectRepo = subjectRepo;
    }

    void setSectionRepo(SectionRepository sectionRepo) {
        this.sectionRepo = sectionRepo;
    }

    void setRoomRepo(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    void setAdminRepo(AdminRepository adminRepo) {
        this.adminRepo = adminRepo;
    }

    public void setFacultyRepository(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }
}
