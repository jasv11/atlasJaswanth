package Module03OOAD.designPatterns.BehavioralDP.CommandPattern.demo.src.main.java.com.example;
//controller // remote
public class Mom {
    Command command;
    public void setCommand(Command command) {
        this.command = command;
    }
    public void executeCommand() {
        this.command.doIt();
    }
}
