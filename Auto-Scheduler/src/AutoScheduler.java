import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

// This class is responsible for running the auto-scheduler
public class AutoScheduler {
  public static final double ONE_DAY = 24 * 60; // number of minutes in a day

  public static void main(String[] args) {
    List<Task> tasks = new ArrayList<>();

    // Unity work for job, due on wednesday 12 hours
    tasks.add(new Task("Unity Work", LocalTime.of(12, 0), LocalDateTime.of(2023, Month.NOVEMBER, 8, 0, 0)));

    // Algorithms, due monday at 11:59, 4 hours
    tasks.add(new Task("Algorithms HW", LocalTime.of(4, 0), LocalDateTime.of(2023, Month.NOVEMBER, 6, 23, 59)));

    // Weekly assignment 7, due today at 6. Will take 4 hours max.
    tasks.add(new Task("iOS Assignment", LocalTime.of(4, 0), LocalDateTime.of(2023, Month.NOVEMBER, 3, 18, 0)));

    // Work on song to release on nov 11, will take about 6 hours max
    tasks.add(new Task("Song", LocalTime.of(6, 0), LocalDateTime.of(2023, Month.NOVEMBER, 11, 0, 0)));

    List<WorkInterval> workIntervals = makeSchedule(tasks, LocalDateTime.of(2023, Month.NOVEMBER, 3, 0, 0));

    System.out.println("In order to minimize the amount of work that you have to do on any one day, and also minimize lateness, you must follow the schedule below.\n");

    for (WorkInterval w : workIntervals) {
      System.out.print("Tasks to work on (in order): ");
      String[] taskNames = w.getTaskList();
      for (int i = 0; i < taskNames.length - 1; i++) {
        System.out.print(taskNames[i] + ", ");
      }
      System.out.println(taskNames[taskNames.length - 1]);
      System.out.println("Start Date: " + w.getStartDate() + "\nEnd Date: " + w.getEndDate() + "\nWork Per Day: " + w.getWorkPerDayInTime());
      System.out.println();
    }
  }


  // Returns a list of work intervals in order to minimize max work in one day and minimize lateness
  public static List<WorkInterval> makeSchedule(List<Task> tasks, LocalDateTime startTime) {
    // First, we must sort the tasks by earliest ascending deadline
    tasks.sort(Comparator.comparing(o -> getMinutesBetween(startTime, o.getDeadline())));

    // Initialize work interval list
    List<WorkInterval> workIntervals = new ArrayList<>();

    // Now, we make the schedule
    makeScheduleHelper(tasks, workIntervals, startTime);

    return workIntervals;
  }

  private static void makeScheduleHelper(List<Task> tasks, List<WorkInterval> workIntervals, LocalDateTime startDate) {
    // Base case is if there are no tasks, we return
    if (tasks.size() == 0) {
      return;
    }

    // Here, lets create an array of the cumulative work for the tasks
    int[] cumulativeWork = new int[tasks.size()];
    cumulativeWork[0] = getInMinutes(tasks.get(0).getWork());

    // At the same time, we will populate the work per day array
    double[] workPerDayArr = new double[tasks.size()];
    workPerDayArr[0] = (double) cumulativeWork[0] / ((double) getMinutesBetween(startDate, tasks.get(0).getDeadline()) / ONE_DAY);

    for (int i = 1; i < cumulativeWork.length; i++) {
      cumulativeWork[i]  = cumulativeWork[i - 1] + getInMinutes(tasks.get(i).getWork());
      workPerDayArr[i] = (double) cumulativeWork[i] / ((double) getMinutesBetween(startDate, tasks.get(i).getDeadline()) / ONE_DAY);
    }

    // Now, we find the max of the work per day array
    int maxWorkPerDayIndex = 0;
    for (int i = 1; i < workPerDayArr.length; i++) {
      if (workPerDayArr[maxWorkPerDayIndex] <= workPerDayArr[i]) {
        maxWorkPerDayIndex = i;
      }
    }

    // Now, we add the necessary work interval
    LocalDateTime endDate = tasks.get(maxWorkPerDayIndex).getDeadline();

    // We should remove all the tasks we have done so far up to this deadline and put them into the work interval
    List<Task> tasksComplete = new ArrayList<>();
    for (int i = 0; i < maxWorkPerDayIndex; i++) {
      tasksComplete.add(tasks.remove(0));
    }
    tasksComplete.add(tasks.remove(0));

    workIntervals.add(new WorkInterval((int) Math.ceil(workPerDayArr[maxWorkPerDayIndex]), startDate, endDate, tasksComplete));

    // Now, we recurse on the rest of the list
    makeScheduleHelper(tasks, workIntervals, endDate);
  }

  // Gets the minutes between two dates
  public static int getMinutesBetween(LocalDateTime startTime, LocalDateTime endTime) {
    return (int) startTime.until(endTime, ChronoUnit.MINUTES);
  }

  // Gets a time in minutes
  public static int getInMinutes(LocalTime time) {
    LocalTime zeroTime = LocalTime.of(0,0);

    return (int) zeroTime.until(time, ChronoUnit.MINUTES);
  }
}