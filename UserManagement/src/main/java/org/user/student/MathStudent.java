package org.user.student;

import org.springframework.stereotype.Component;

@Component
public class MathStudent implements Student {
    @Override
    public int getStudentCount() {
        return 50;
    }
}
