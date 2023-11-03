import java.time.LocalDateTime;
import java.time.LocalTime;

// A task has a time to finish and deadline (and name)
public final class Task {
  private final String name;
  private final LocalTime work;
  private final LocalDateTime deadline;

  Task(String name, LocalTime work, LocalDateTime deadline) {
    this.name = name;
    this.work = work;
    this.deadline = deadline;
  }

  public String getTaskName() {
    return this.name;
  }

  public LocalTime getWork() {
    return work;
  }

  public LocalDateTime getDeadline() {
    return deadline;
  }
}
