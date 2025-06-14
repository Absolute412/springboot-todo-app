package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // Display all tasks
    @GetMapping("/")
    public String index(Model model) {
        List<Task> allTasks = taskRepository.findAll();

        List<Task> pendingTasks = allTasks.stream()
            .filter(task -> !task.isCompleted())
            .toList();

        List<Task> completedTasks = allTasks.stream()
            .filter(Task::isCompleted)
            .toList();

        model.addAttribute("pendingTasks", pendingTasks);
        model.addAttribute("completedTasks", completedTasks);
        return "index";
    }

    // Show form to add new task
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("task", new Task());
        return "add";
    }

    // Handle form submission for new task
    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task) {
        taskRepository.save(task);
        return "redirect:/";
    }

    // Delete a task
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }

    // Show form to edit a task
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            model.addAttribute("task", task);
            return "edit";
        }
        return "redirect:/";
    }

    // Handle update form submission
    @PostMapping("/update")
    public String updateTask(@ModelAttribute Task task) {
        System.out.println("🚨🚨 TASK UPDATE: ID = " + task.getId() + " 🚨🚨"); // For debug
        taskRepository.save(task); // Will update if ID exists
        return "redirect:/";
    }
}