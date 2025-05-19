package student_performance;

public class LaunchGUI {
    public static void main(String[] args) {
        Group group = new Group();
        JournalApp.loadFromFile(group); // змінити метод на static з параметром Group
        JournalAppGUI.launch(group);
    }
}
