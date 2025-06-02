package com.example.ManageToDoList.service;

import com.example.ManageToDoList.entity.ToDo;
import com.example.ManageToDoList.repository.ToDoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;

    public List<ToDo> getAllToDo() {
        return toDoRepository.findAll();
    }

    public ToDo getToDoById(Long id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ToDo not found with id: " + id));
    }

    @Transactional
    public List<ToDo> createAllToDo(List<ToDo> toDos) {
        Objects.requireNonNull(toDos, "ToDo list must not be null");
        if (toDos.isEmpty()) {
            throw new IllegalArgumentException("ToDo list must not be empty");
        }
        return toDoRepository.saveAll(toDos);
    }

    @Transactional
    public ToDo updateTaskStatus(Long id, boolean completed) {
        ToDo task = getToDoById(id);
        task.setCompleted(completed);
        return toDoRepository.save(task);
    }

    public List<ToDo> getTasksByStatus(boolean completed) {
        return toDoRepository.findByCompleted(completed);
    }
}
