package com.milan.service;

import com.milan.dto.TodoDto;
import com.milan.exception.ResourceNotFoundException;

import java.util.List;

public interface TodoService {
    Boolean saveTodo(TodoDto todo) throws Exception;

    TodoDto getTodoById(Integer id) throws ResourceNotFoundException;

    List<TodoDto> getTodoByUser();
}
