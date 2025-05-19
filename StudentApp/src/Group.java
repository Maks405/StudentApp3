package student_performance;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private List<Student> students = new ArrayList<>();
    private List<Discipline> disciplines = new ArrayList<>();

    public void addStudent(Student s) {
        students.add(s);
    }

    public List<Student> getStudents() {
        return students;
    }

    public Student findStudent(String fullName) {
        for (Student s : students) {
            if (s.getFullName().equalsIgnoreCase(fullName)) {
                return s;
            }
        }
        return null;
    }

    public double getGroupAverage() {
        if (students.isEmpty()) return 0;
        double total = 0;
        int count = 0;
        for (Student s : students) {
            double avg = s.getAverageGrade();
            if (avg > 0) {
                total += avg;
                count++;
            }
        }
        return count == 0 ? 0 : total / count;
    }

    // Дисципліни групи
    public void addDiscipline(Discipline d) {
        disciplines.add(d);
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public Discipline findDiscipline(String name) {
        for (Discipline d : disciplines) {
            if (d.getName().equalsIgnoreCase(name)) {
                return d;
            }
        }
        return null;
    }
}
