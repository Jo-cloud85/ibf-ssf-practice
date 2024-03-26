package ibf2023.ssf.practice.repository;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ibf2023.ssf.practice.model.Todo;
import ibf2023.ssf.practice.service.DataService;
import ibf2023.ssf.practice.util.Util;

@Repository
public class TodoRepo {

    @Autowired
    @Qualifier(Util.KEY_TODO)
    RedisTemplate<String, String> template;

    HashOperations<String, String, String> hashOps;

    @Autowired
    DataService dataService;

    // Save existing list to Redis
    @SuppressWarnings("null")
    public void saveTodoListToRedis(String key, List<Todo> todoList) throws IOException {
        hashOps = template.opsForHash();
        for (Todo todo : todoList) {
            String todoJsonStr = dataService.convertTodoToJsonStr(todo);
            hashOps.putIfAbsent(Util.KEY_TODO, todo.getId(), todoJsonStr);
        }
    }

    // Get a list of all todos from Redis
    public List<Todo> getTodoListFrRedis() throws ParseException {
        hashOps = template.opsForHash();
        List<Todo> todoList = new LinkedList<>();
        Map<String, String> todoMap = hashOps.entries(Util.KEY_TODO);

        for (Map.Entry<String, String> entry : todoMap.entrySet()) {
            String jsonObjStr = entry.getValue(); // each Json str
            Todo todo = dataService.convertJsonStrToTodo(jsonObjStr);
            todoList.add(todo);
        }

        return todoList;
    }

    // Add a new todo to Redis
    @SuppressWarnings("null")
    public void saveNewTodoToRedis(Todo todo) throws IOException {
        hashOps = template.opsForHash(); 
        String todoJsonStr = dataService.convertTodoToJsonStr(todo);
        hashOps.putIfAbsent(Util.KEY_TODO, todo.getId(), todoJsonStr);
    }

    // Get a specific todo from Redis
    public Todo getTodoById(String id) {
        hashOps = template.opsForHash(); 
        Map<String, String> todoMap = hashOps.entries(Util.KEY_TODO);
    
        Optional<Todo> todoOptional = todoMap.values().stream()
                                        .map(jsonStr -> dataService.convertJsonStrToTodo(jsonStr))
                                        .filter(todo -> todo.getId().equals(id))
                                        .findFirst();
        
        return todoOptional.orElse(null);
    }

    // Update a specific todo to Redis - make sure id no change
    @SuppressWarnings("null")
    public void updateTodo(Todo todo) throws IOException {
        hashOps = template.opsForHash(); 
        String jsonStr = dataService.convertTodoToJsonStr(todo);
        hashOps.put(Util.KEY_TODO, todo.getId(), jsonStr);
    }

    // Delete a specific todo from Redis
    public void deleteTodo(Todo todo) {
        hashOps = template.opsForHash(); 
        hashOps.delete(Util.KEY_TODO, todo.getId());
    }
}

