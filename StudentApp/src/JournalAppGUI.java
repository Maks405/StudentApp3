package student_performance;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JournalAppGUI extends JFrame {
    private Group group;
    private JTextArea outputArea;

    public JournalAppGUI(Group group) {
        this.group = group;
        setTitle("Журнал успішності студентів");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(10, 1, 5, 5));

        JButton btnAddStudent = new JButton("Додати студента");
        JButton btnAddGroupDiscipline = new JButton("Додати дисципліну в групу");
        JButton btnAddDiscipline = new JButton("3Додати дисципліну студенту");
        JButton btnAddGrade = new JButton("4Виставити оцінку студенту");
        JButton btnViewStudentPerformance = new JButton("Переглянути успішність студента");
        JButton btnViewGroupReport = new JButton("Переглянути звіт групи");
        JButton btnDelete = new JButton("Видалення");
        JButton btnViewAllGrades = new JButton("Переглянути всі оцінки всіх студентів");
        JButton btnSave = new JButton("Зберегти дані");
        panel.add(btnSave);
        JButton btnExit = new JButton("Вийти");

        panel.add(btnAddStudent);
        panel.add(btnAddGroupDiscipline);
        panel.add(btnAddDiscipline);
        panel.add(btnAddGrade);
        panel.add(btnViewStudentPerformance);
        panel.add(btnViewGroupReport);
        panel.add(btnDelete);
        panel.add(btnViewAllGrades);
        panel.add(btnExit);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(panel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        btnAddStudent.addActionListener(e -> addStudent());
        btnAddGroupDiscipline.addActionListener(e -> addDisciplineToGroup());
        btnAddDiscipline.addActionListener(e -> addDisciplineToStudent());
        btnAddGrade.addActionListener(e -> addGradeToStudent());
        btnViewStudentPerformance.addActionListener(e -> viewStudentPerformance());
        btnViewGroupReport.addActionListener(e -> viewGroupReport());
        btnDelete.addActionListener(e -> deleteMenu());
        btnViewAllGrades.addActionListener(e -> viewAllGrades());
        btnSave.addActionListener(e -> {
            JournalApp.saveToFile(group);
            JOptionPane.showMessageDialog(this, "Дані збережено вручну.", "Інформація", JOptionPane.INFORMATION_MESSAGE);
        });
        btnExit.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(null, "Дані збережено.", "Вихід", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void addStudent() {
        String fullName = JOptionPane.showInputDialog(this, "Введіть прізвище, ім'я та (за бажанням) по батькові студента:");
        if (fullName == null || fullName.trim().isEmpty()) return;
        String[] parts = fullName.trim().split("\\s+");
        if (parts.length < 2 || parts.length > 3) {
            JOptionPane.showMessageDialog(this, "Ім’я повинно містити від 2 до 3 слів.", "Помилка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        group.addStudent(new Student(fullName.trim()));
        outputArea.append("Студента додано: " + fullName + "\n");
        JOptionPane.showMessageDialog(this, "Студента додано.", "Інформація", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addDisciplineToGroup() {
        String disciplineName = JOptionPane.showInputDialog(this, "Введіть назву дисципліни для групи:");
        if (disciplineName == null || disciplineName.trim().isEmpty()) return;

        for (Discipline d : group.getDisciplines()) {
            if (d.getName().equalsIgnoreCase(disciplineName.trim())) {
                JOptionPane.showMessageDialog(this, "Така дисципліна вже існує в групі.", "Інформація", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        group.addDiscipline(new Discipline(disciplineName.trim()));
        outputArea.append("Дисципліна додана в групу: " + disciplineName + "\n");
    }

    private Student selectStudentDialog(String message) {
        List<Student> students = group.getStudents();
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Немає студентів для вибору.", "Інформація", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String[] studentNames = students.stream().map(Student::getFullName).toArray(String[]::new);
        JComboBox<String> comboBox = new JComboBox<>(studentNames);

        int option = JOptionPane.showOptionDialog(this, comboBox, message,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, new Object[]{"Далі", "Скасувати"}, "Далі");

        if (option == 0) {
            String selectedName = (String) comboBox.getSelectedItem();
            return group.findStudent(selectedName);
        }
        return null;
    }

    private Discipline selectDisciplineDialog(Student student, String message) {
        List<Discipline> disciplines = student.getDisciplines();
        if (disciplines.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Студент не має дисциплін.", "Інформація", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String[] disciplineNames = disciplines.stream().map(Discipline::getName).toArray(String[]::new);
        JComboBox<String> comboBox = new JComboBox<>(disciplineNames);

        int option = JOptionPane.showOptionDialog(this, comboBox, message,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, new Object[]{"Далі", "Скасувати"}, "Далі");

        if (option == 0) {
            String selectedName = (String) comboBox.getSelectedItem();
            return student.getDiscipline(selectedName);
        }
        return null;
    }

    private void addDisciplineToStudent() {
        Student student = selectStudentDialog("Виберіть студента:");
        if (student == null) return;

        List<Discipline> groupDisciplines = group.getDisciplines();
        if (groupDisciplines.isEmpty()) {
            JOptionPane.showMessageDialog(this, "У групі немає дисциплін. Спочатку додайте дисципліну в групу.", "Інформація", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] disciplineNames = groupDisciplines.stream().map(Discipline::getName).toArray(String[]::new);
        JComboBox<String> comboBox = new JComboBox<>(disciplineNames);

        int option = JOptionPane.showOptionDialog(this, comboBox, "Виберіть дисципліну для студента:",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, new Object[]{"Далі", "Скасувати"}, "Далі");

        if (option == 0) {
            String selectedName = (String) comboBox.getSelectedItem();

            if (student.getDiscipline(selectedName) != null) {
                JOptionPane.showMessageDialog(this, "Студент вже має цю дисципліну.", "Інформація", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Discipline d : groupDisciplines) {
                if (d.getName().equalsIgnoreCase(selectedName)) {
                    student.addDiscipline(d);
                    outputArea.append("Дисципліна " + selectedName + " додана студенту " + student.getFullName() + "\n");
                    break;
                }
            }
        }
    }

    private void addGradeToStudent() {
        Student student = selectStudentDialog("Виберіть студента:");
        if (student == null) return;

        Discipline discipline = selectDisciplineDialog(student, "Виберіть дисципліну:");
        if (discipline == null) return;

        String gradeStr = JOptionPane.showInputDialog(this, "Введіть оцінку (0-100):");
        if (gradeStr == null) return;
        try {
            int grade = Integer.parseInt(gradeStr);
            if (grade < 0 || grade > 100) {
                JOptionPane.showMessageDialog(this, "Оцінка повинна бути від 0 до 100.", "Помилка", JOptionPane.WARNING_MESSAGE);
                return;
            }
            discipline.addGrade(grade);
            outputArea.append("Оцінку додано студенту " + student.getFullName() + " з дисципліни " + discipline.getName() + ": " + grade + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Некоректне число.", "Помилка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void viewStudentPerformance() {
        Student student = selectStudentDialog("Виберіть студента:");
        if (student == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Успішність студента ").append(student.getFullName()).append(":\n");
        for (Discipline d : student.getDisciplines()) {
            sb.append("- ").append(d.getName()).append(": ").append(String.format("%.2f", d.getAverageGrade())).append(" (середній бал)\n");
        }
        sb.append("Середній бал по всіх дисциплінах: ").append(String.format("%.2f", student.getAverageGrade())).append("\n");

        outputArea.append(sb.toString());
    }

    private void viewGroupReport() {
        outputArea.append("Звіт по групі:\n");
        for (Student s : group.getStudents()) {
            outputArea.append(s.getFullName());
            outputArea.append(" - Середній бал: ");
            outputArea.append(String.format("%.2f", s.getAverageGrade()));
            outputArea.append("\n");
        }
        outputArea.append("Середній бал по групі: ");
        outputArea.append(String.format("%.2f", group.getGroupAverage()));
        outputArea.append("\n");
    }

    private void deleteMenu() {
        String[] options = {
                "Видалити одного студента",
                "Видалити всіх студентів",
                "Видалити всі оцінки одного студента",
                "Видалити всі оцінки всіх студентів",
                "Видалити одну конкретну оцінку",
                "Скасувати"
        };

        int choice = JOptionPane.showOptionDialog(this, "Виберіть варіант видалення:", "Видалення",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> {
                Student s = selectStudentDialog("Виберіть студента для видалення:");
                if (s != null) {
                    group.getStudents().remove(s);
                    outputArea.append("Студента видалено: " + s.getFullName() + "\n");
                }
            }
            case 1 -> {
                group.getStudents().clear();
                outputArea.append("Усіх студентів видалено.\n");
            }
            case 2 -> {
                Student s = selectStudentDialog("Виберіть студента для видалення оцінок:");
                if (s != null) {
                    for (Discipline d : s.getDisciplines()) {
                        d.clearGrades();
                    }
                    outputArea.append("Усі оцінки видалено у студента: " + s.getFullName() + "\n");
                }
            }
            case 3 -> {
                for (Student s : group.getStudents()) {
                    for (Discipline d : s.getDisciplines()) {
                        d.clearGrades();
                    }
                }
                outputArea.append("Оцінки всіх студентів видалено.\n");
            }
            case 4 -> {
                Student student = selectStudentDialog("Виберіть студента:");
                if (student == null) return;

                Discipline discipline = selectDisciplineDialog(student, "Виберіть дисципліну:");
                if (discipline == null) return;

                String gradeStr = JOptionPane.showInputDialog(this, "Введіть значення оцінки, яку потрібно видалити:");
                if (gradeStr == null) return;
                try {
                    int targetGrade = Integer.parseInt(gradeStr);
                    boolean removed = discipline.removeOneGrade(targetGrade);
                    if (removed) {
                        outputArea.append("Оцінку " + targetGrade + " видалено.\n");
                    } else {
                        outputArea.append("Оцінка не знайдена.\n");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Некоректне число.", "Помилка", JOptionPane.WARNING_MESSAGE);
                }
            }
            default -> outputArea.append("Видалення скасовано.\n");
        }
    }

    private void viewAllGrades() {
        if (group.getStudents().isEmpty()) {
            outputArea.append("Група порожня.\n");
            return;
        }
        for (Student s : group.getStudents()) {
            outputArea.append("\n" + s.getFullName() + ":\n");
            for (Discipline d : s.getDisciplines()) {
                outputArea.append(" - " + d.getName() + ": ");
                List<Integer> grades = d.getGrades();
                if (grades.isEmpty()) {
                    outputArea.append("немає оцінок\n");
                } else {
                    for (int g : grades) {
                        outputArea.append(g + " ");
                    }
                    outputArea.append("\n");
                }
            }
        }
    }

    public static void launch(Group group) {
        SwingUtilities.invokeLater(() -> new JournalAppGUI(group).setVisible(true));
    }
}
