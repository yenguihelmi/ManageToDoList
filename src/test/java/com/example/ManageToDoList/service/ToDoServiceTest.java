package com.example.ManageToDoList.service;

import com.example.ManageToDoList.entity.ToDo;
import com.example.ManageToDoList.repository.ToDoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToDoServiceTest {

    @Mock
    private ToDoRepository toDoRepository;

    @InjectMocks
    private ToDoService toDoService;

    private ToDo todo1;
    private ToDo todo2;

    @BeforeEach
    void setUp() {
        todo1 = new ToDo();
        todo1.setId(1L);
        todo1.setCompleted(false);

        todo2 = new ToDo();
        todo2.setId(2L);
        todo2.setCompleted(true);
    }

    @Test
    void getAllToDoShouldReturnAllTodos() {
        List<ToDo> expectedTodos = Arrays.asList(todo1, todo2);
        when(toDoRepository.findAll()).thenReturn(expectedTodos);

        List<ToDo> actualTodos = toDoService.getAllToDo();

        assertEquals(2, actualTodos.size());
        assertEquals(expectedTodos, actualTodos);
        verify(toDoRepository).findAll();
    }

    @Test
    void getToDoByIdWhenTodoExistsShouldReturnTodo() {
        when(toDoRepository.findById(1L)).thenReturn(Optional.of(todo1));

        ToDo foundTodo = toDoService.getToDoById(1L);

        assertNotNull(foundTodo);
        assertEquals(todo1.getId(), foundTodo.getId());
        verify(toDoRepository).findById(1L);
    }

    @Test
    void getToDoByIdWhenTodoNotExistsShouldThrowException() {
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> toDoService.getToDoById(anyLong()));
        verify(toDoRepository).findById(anyLong());
    }

    @Test
    void createAllToDoWithValidListShouldSaveAllTodos() {
        List<ToDo> todosToSave = Arrays.asList(todo1, todo2);
        when(toDoRepository.saveAll(todosToSave)).thenReturn(todosToSave);

        List<ToDo> savedTodos = toDoService.createAllToDo(todosToSave);

        assertEquals(2, savedTodos.size());
        verify(toDoRepository).saveAll(todosToSave);
    }

    @Test
    void createAllToDoWithNullListShouldThrowException() {
        assertThrows(NullPointerException.class, () -> toDoService.createAllToDo(null));
        verifyNoInteractions(toDoRepository);
    }

    @Test
    void createAllToDoWithEmptyListShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> toDoService.createAllToDo(Collections.emptyList()));
        verifyNoInteractions(toDoRepository);
    }

    @Test
    void updateTaskStatusWhenTodoExistsShouldUpdateStatus() {
        when(toDoRepository.findById(1L)).thenReturn(Optional.of(todo1));
        when(toDoRepository.save(todo1)).thenReturn(todo1);

        ToDo updatedTodo = toDoService.updateTaskStatus(1L, true);

        assertTrue(updatedTodo.isCompleted());
        verify(toDoRepository).findById(1L);
        verify(toDoRepository).save(todo1);
    }

    @Test
    void updateTaskStatusWhenTodoNotExistsShouldThrowException() {
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> toDoService.updateTaskStatus(anyLong(), true));
        verify(toDoRepository).findById(anyLong());
        verify(toDoRepository, never()).save(any());
    }

    @Test
    void getTasksByStatusShouldReturnFilteredTodos() {
        List<ToDo> completedTodos = Collections.singletonList(todo2);
        when(toDoRepository.findByCompleted(true)).thenReturn(completedTodos);

        List<ToDo> result = toDoService.getTasksByStatus(true);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isCompleted());
        verify(toDoRepository).findByCompleted(true);
    }
}