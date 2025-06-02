package com.example.ManageToDoList.controller;

import com.example.ManageToDoList.entity.ToDo;
import com.example.ManageToDoList.service.ToDoService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/toDos")
@RequiredArgsConstructor
@CrossOrigin
public class ToDoController {

    private final ToDoService toDoService;

    @PostConstruct
    public void initToDoService() {
        List<ToDo> toDos = Arrays.asList(
                new ToDo("TO-DO-1", "Test technique 1", true),
                new ToDo("TO-DO-2", "Test technique 2", false),
                new ToDo("TO-DO-3", "Test technique 3", true)
        );
        toDoService.createAllToDo(toDos);
    }

    @GetMapping
    public ResponseEntity<List<ToDo>> getToDoList() {
        List<ToDo> toDoList = toDoService.getAllToDo();
        return ResponseEntity.ok(toDoList);
    }

    @PostMapping
    public ResponseEntity<List<ToDo>> addToDo(@RequestBody List<ToDo> toDos) {
        List<ToDo> createdToDos = toDoService.createAllToDo(toDos);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdToDos);
    }

    @GetMapping("/{id}")
    public ToDo getToDoById(@PathVariable Long id) {
        return toDoService.getToDoById(id);
    }

    @GetMapping("/completed")
    public List<ToDo> getCompletedToDos() {
        return toDoService.getTasksByStatus(true);
    }

    @GetMapping("/notCompleted")
    public List<ToDo> getPendingToDos() {
        return toDoService.getTasksByStatus(false);
    }
    @PutMapping("/{id}")
    public ToDo updateToDoStatus(@PathVariable Long id, @RequestParam boolean completed) {
        return toDoService.updateTaskStatus(id, completed);
    }

}
