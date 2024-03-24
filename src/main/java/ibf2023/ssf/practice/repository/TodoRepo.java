package ibf2023.ssf.practice.repository;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ibf2023.ssf.practice.model.Todo;
import ibf2023.ssf.practice.util.Util;

@Repository
public class TodoRepo {

    @Autowired
    @Qualifier(Util.KEY_TODO)
    RedisTemplate<String, Todo> template;

    HashOperations<String, String, Todo> hashOps;

    @SuppressWarnings("null")
    public void saveTodoListToRedis(String key, List<Todo> todoList) throws ParseException {
        hashOps = template.opsForHash();
        for (Integer i = 0; i < todoList.size(); i++) {
            Todo todo = todoList.get(i);
            hashOps.putIfAbsent(Util.KEY_TODO, todo.getId(), todo);
        }
    }

    public Map<String, Todo> getTodoListFrRedis() {
        hashOps = template.opsForHash();
        Map<String, Todo> todoMap = hashOps.entries(Util.KEY_TODO);
        return todoMap;
    }

    @SuppressWarnings("null")
    public void saveNewTodoToRedis(Todo todo) {
        hashOps = template.opsForHash(); 
        hashOps.putIfAbsent(Util.KEY_TODO, todo.getId(), todo);
    }

    // Read a specific record (from Redis Map)
    public Todo getTodoById(String id) {
        hashOps = template.opsForHash(); 
        Map<String, Todo> todoMap = hashOps.entries(Util.KEY_TODO);
    
        Optional<Todo> todoOptional = todoMap.values().stream()
                                        .filter(todo -> todo.getId().equals(id))
                                        .findFirst();
        
        return todoOptional.orElse(null);
    }

    // Update a specific record - make sure id no change
    @SuppressWarnings("null")
    public void updateTodo(Todo todo) {
        hashOps = template.opsForHash(); 
        System.out.println("Updating statement from Repo..." + todo);
        hashOps.put(Util.KEY_TODO, todo.getId(), todo);
    }

    // Delete operations of a record (in Redis Map)
    public void deleteTodo(Todo todo) {
        hashOps = template.opsForHash(); 
        hashOps.delete(Util.KEY_TODO, todo.getId());
    }

}

