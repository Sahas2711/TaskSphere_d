package com.mit.tasksphere.TaskService.Controller;


import com.mit.tasksphere.TaskService.Entities.Task;
import com.mit.tasksphere.TaskService.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping
    public List<Task> getTasks(@RequestParam(required = false) Long assignedTo,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) String priority) {
        return taskService.getAllTasks(assignedTo, status, priority);
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}/reassign")
    public Task reassignTask(@PathVariable Long id, @RequestParam String newAssignee) {
        return taskService.reassignTask(id, newAssignee);
    }

    @PostMapping("/{id}/comment")
    public Task addComment(@PathVariable Long id, @RequestParam String comment) {
        return taskService.addComment(id, comment);
    }

    @GetMapping("/analytics")
    public Map<String, Object> getAnalytics() {
        return taskService.getAnalytics();
    }

    @GetMapping("/reminders")
    public List<Task> getReminders() {
        return taskService.getReminders();
    }
}
