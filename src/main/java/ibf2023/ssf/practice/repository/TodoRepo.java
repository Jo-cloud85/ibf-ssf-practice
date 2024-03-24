package ibf2023.ssf.practice.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    HashOperations<String, Integer, Todo> hashOps;

    @SuppressWarnings("null")
    public void saveTodoListToRedis(String key, List<Todo> todoList) throws ParseException {
        hashOps = template.opsForHash();
        for (Integer i = 0; i < todoList.size(); i++) {
            Todo todo = todoList.get(i);
            // // Have to convert all the dates to epoch date
            // long dueDateEpochMillis = todo.getDueDate().getTime();
            // long createdAtEpochMillis = todo.getCreatedAt().getTime();
            // long updatedAtEpochMillis = todo.getUpdatedAt().getTime();

            // // Update Todo object with epoch milliseconds dates
            // todo.setDueDateEpochMillis(dueDateEpochMillis);

            hashOps.putIfAbsent(Util.KEY_TODO, i, todo);
        }
    }

    public Map<Integer, Todo> getTodoListFrRedis() {
        hashOps = template.opsForHash();
        Map<Integer, Todo> todoMap = hashOps.entries(Util.KEY_TODO);

        // // Create a SimpleDateFormat object for formatting the date
        // SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM/dd/yyyy");

        // // Iterate over the todoMap entries
        // for (Map.Entry<Integer, Todo> entry : todoMap.entrySet()) {
        //     Todo todo = entry.getValue();
        //     System.out.println(todo);
        //     // Get the dueDate in epoch milliseconds from the todo object
        //     // long epochMilliseconds = todo.getDueDate();

        //     // // Convert epoch milliseconds to a human-readable date format
        //     // Date date = new Date(epochMilliseconds);
        //     // String formattedDate = sdf.format(date);

        //     // // Update the todo object with the formatted dueDate
        //     // todo.setDueDate(formattedDate); // Assuming you have a setter for dueDateStr in Todo class
        // }

        return todoMap;
    }

    @SuppressWarnings("null")
    public void saveTodoToRedis(Todo todo) {
        hashOps = template.opsForHash(); 
        // Get the size of existing data
        int lastId = getTodoListFrRedis().size();
        hashOps.putIfAbsent(Util.KEY_TODO, lastId+1, todo);
    }

}

