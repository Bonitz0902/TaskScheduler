package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Task {
    String name;
    int duration;
    List<String> dependencies;
    LocalDate startDate;
    LocalDate endDate;

    public Task(String name, int duration, List<String> dependencies){
        this.name = name;
        this.duration = duration;
        this.dependencies = dependencies != null ? dependencies : new ArrayList<String>();
    }

    public void setSchedule(LocalDate startDate){
        this.startDate = startDate;
        this.endDate = startDate.plusDays(duration - 1);
    }

    public String toString() {
        return String.format("Task: %s, Start: %s, End: %s", name, startDate, endDate);
    }
}
