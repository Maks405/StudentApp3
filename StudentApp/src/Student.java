package student_performance;

import java.util.*;

public class Student {
    private String fullName;
    private List<Discipline> disciplines = new ArrayList<>();

    public Student(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void addDiscipline(Discipline d) {
        disciplines.add(d);
    }

    public Discipline getDiscipline(String name) {
        for (Discipline d : disciplines) {
            if (d.getName().equalsIgnoreCase(name)) return d;
        }
        return null;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public double getAverageGrade() {
        if (disciplines.isEmpty()) return 0;
        double total = 0;
        int count = 0;
        for (Discipline d : disciplines) {
            double avg = d.getAverageGrade();
            if (avg > 0) {
                total += avg;
                count++;
            }
        }
        return count == 0 ? 0 : total / count;
    }
}