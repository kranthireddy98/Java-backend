package org.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.user.dto.StudentsCountDto;
import org.user.student.School;

@RestController
@RequestMapping("/school")
public class SchoolController {
    private final School school;

    public SchoolController(School school) {
        this.school = school;
    }


    @GetMapping("/count")
    public ResponseEntity<StudentsCountDto> getStudentCount(){
        StudentsCountDto count = school.displayStudentCount();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }
}
