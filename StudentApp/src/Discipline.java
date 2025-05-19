package student_performance;

import java.util.*;

public class Discipline {
    private String name;
    private List<Integer> grades = new ArrayList<>();

    public Discipline(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addGrade(int grade) {
        grades.add(grade);
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public void clearGrades() {
        grades.clear();
    }

    public boolean removeOneGrade(int grade) {
        return grades.remove((Integer) grade);
    }

    public double getAverageGrade() {
        if (grades.isEmpty()) return 0;
        int sum = 0;
        for (int g : grades) sum += g;
        return (double) sum / grades.size();
    }
} // ← тільки ОДНА закриваюча дужка
