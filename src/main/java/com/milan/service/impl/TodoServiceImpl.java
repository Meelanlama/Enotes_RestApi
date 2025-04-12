package com.milan.service.impl;

import com.milan.dto.TodoDto;
import com.milan.enums.TodoStatus;
import com.milan.exception.ResourceNotFoundException;
import com.milan.model.Todo;
import com.milan.repository.TodoRepository;
import com.milan.service.TodoService;
import com.milan.util.CommonUtil;
import com.milan.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepo;

    private final ModelMapper mapper;

    private final Validation validation;

    @Override
    public Boolean saveTodo(TodoDto todoDto) throws Exception {

//         validate todo status
        validation.todoValidation(todoDto);

        //convert dto to entity
        Todo todo = mapper.map(todoDto, Todo.class);
        todo.setStatusId(todoDto.getStatus().getId());
        Todo saveTodo = todoRepo.save(todo);

        return !ObjectUtils.isEmpty(saveTodo);
    }

    /**
     * Retrieves a Todo by its ID, maps it to a TodoDto, and sets the corresponding status.
     *
     * @param id The ID of the Todo to retrieve.
     * @return A TodoDto object with the mapped data and status.
     * @throws ResourceNotFoundException if the Todo with the given ID does not exist.
     */
    @Override
    public TodoDto getTodoById(Integer id) throws ResourceNotFoundException {
        Todo todo = todoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo Not Found. Id is invalid"));
        TodoDto todoDto = mapper.map(todo, TodoDto.class);
        setStatus(todoDto,todo);
        return todoDto;
    }

    /**
     * Sets the status of the given TodoDto by matching the statusId from the Todo entity
     * with the appropriate enum value in TodoStatus.
     *
     * @param todoDto The DTO to which the status will be set.
     * @param todo The entity containing the statusId to match.
     */
    private void setStatus(TodoDto todoDto, Todo todo) {

        for(TodoStatus st:TodoStatus.values())
        {
            if(st.getId().equals(todo.getStatusId()))
            {
               TodoDto.StatusDto statusDto= TodoDto.StatusDto.builder()
                        .id(st.getId())
                        .name(st.getName())
                        .build();
                todoDto.setStatus(statusDto);
            }
        }

    }

    @Override
    public List<TodoDto> getTodoByUser() {
        Integer userId = CommonUtil.getLoggedInUser().getId();
        List<Todo> todos = todoRepo.findByCreatedBy(userId);
        return todos.stream().map(td -> mapper.map(td, TodoDto.class)).toList();
    }
}
