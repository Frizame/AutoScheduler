import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// This class has a start date, end date, and work per day
public final class WorkInterval
{
  private final List<Task> tasks;
  private final int workPerDay; // in minutes
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;

  WorkInterval(int workPerDay, LocalDateTime startDate, LocalDateTime endDate, List<Task> tasks) {
    this.workPerDay = workPerDay;
    this.startDate = startDate;
    this.endDate = endDate;
    this.tasks = tasks;
  }

  public int getWorkPerDay() {
    return workPerDay;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public LocalTime getWorkPerDayInTime() {
    return LocalTime.of(workPerDay / 60, workPerDay % 60);
  }

  // Get the task list as a list of names. These are the tasks to work on in order
  public String[] getTaskList() {
    String[] taskList = new String[tasks.size()];
    for (int i = 0; i < taskList.length; i++) {
      taskList[i] = tasks.get(i).getTaskName();
    }
    return taskList;
  }
}