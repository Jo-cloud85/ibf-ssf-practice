package ibf2023.ssf.practice.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2023.ssf.practice.model.Todo;
import ibf2023.ssf.practice.repository.TodoRepo;

@Service
public class TodoService {
    
    @Autowired
    private TodoRepo todoRepo;

    @Autowired
    private DataService dataService;

    public void saveExistingTodoList(String key) throws IOException, ParseException {
        List<Todo> todoList = dataService.createListFromExtgJsonStr();
        todoRepo.saveTodoListToRedis(key, todoList);
    }

    public List<Todo> getToDoList() throws ParseException {
        return todoRepo.getTodoListFrRedis();
    }

    public void createTodo(Todo todo) throws IOException {
        todoRepo.saveNewTodoToRedis(todo);
    }

    public Todo getTodoById(String id) {
        return todoRepo.getTodoById(id);
    }

    public void updateTodo(Todo todo) throws IOException {
        todoRepo.updateTodo(todo);
    }

    public void deleteTodo(Todo todo) {
        todoRepo.deleteTodo(todo);
    }
}
