package org.user.student;

import org.springframework.stereotype.Component;
import org.user.dto.StudentsCountDto;

@Component
public class School {

    private final Student student;

    public School(Student student){
        this.student=student;
    }

    public StudentsCountDto displayStudentCount(){
        int count = student.getStudentCount();

        return new StudentsCountDto(count);
    }

}
