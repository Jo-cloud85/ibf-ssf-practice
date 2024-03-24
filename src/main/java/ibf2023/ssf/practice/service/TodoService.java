package ibf2023.ssf.practice.service;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2023.ssf.practice.model.Todo;
import ibf2023.ssf.practice.repository.TodoRepo;

@Service
public class TodoService {
    
    @Autowired
    private TodoRepo todoRepo;

    public void saveExistingTodoList(String key, List<Todo> todoList) throws ParseException {
        todoRepo.saveTodoListToRedis(key, todoList);
    }

    public Map<Integer, Todo> getToDoList() {
        return todoRepo.getTodoListFrRedis();
    }

    public void createTodo(Todo todo) {
        todoRepo.saveTodoToRedis(todo);
    }
}
