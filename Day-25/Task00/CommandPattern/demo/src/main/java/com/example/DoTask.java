package Module03OOAD.designPatterns.BehavioralDP.CommandPattern.demo.src.main.java.com.example;

public class DoTask implements Command{
    private Task task;

    public DoTask(Task task) {
        this.task = task;
    }
    public void doIt() {
        this.task.doo();
    }
}