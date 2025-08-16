package Module03OOAD.designPatterns.BehavioralDP.CommandPattern.demo.src.main.java.com.example;

public class DontDoTask implements Command{
    private Task task;

    public DontDoTask(Task task) {
        this.task = task;
    }
    public void doIt() {
        this.task.dont();
    }
}
