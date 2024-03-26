package ibf2023.ssf.practice.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import ibf2023.ssf.practice.model.Todo;
import ibf2023.ssf.practice.util.Util;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class DataService {

    // Reading from file and create a giant Json string
    public String readFile(){
        StringBuilder sb = new StringBuilder();
        File file = new File(Util.FILE_PATH);

        if(file.exists()){
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }                    
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        } else {
            System.out.println("file not exist: " + Util.FILE_PATH);
        } 

        return sb.toString();
    }


    // Nothing to Redis yet
    public List<Todo> createListFromExtgJsonStr() throws ParseException {
        String jsonStr = readFile();
        JsonArray jsonArray = Json.createReader(new StringReader(jsonStr)).readArray();
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);

        List<Todo> todoList = new LinkedList<>();

        for (int i=0; i<jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).asJsonObject();
            Todo todo = convertJsonObjToTodo(jsonObject);
            todoList.add(todo);
        }
        return todoList;
    }


    // Convert each Todo object to a Json string for Redis
    public String convertTodoToJsonStr(Todo todo) throws IOException {
        JsonObject jsonObject = Json.createObjectBuilder()
            .add("id", todo.getId())
            .add("name", todo.getName())
            .add("description", todo.getDescription())
            .add("due_date", todo.getDueDate().getTime()) //converting to epoch for saving to redis
            .add("priority_level", todo.getPriority())
            .add("status", todo.getStatus())
            .add("created_at", todo.getCreatedAt().getTime()) //converting to epoch for saving to redis
            .add("updated_at", todo.getUpdatedAt().getTime()) //converting to epoch for saving to redis
            .build();

        String todoJsonStr;

        try(Writer writer = new StringWriter()) {
            Json.createWriter(writer).write(jsonObject);
            todoJsonStr = writer.toString();
        }

        return todoJsonStr;
    }


    // Convert each Json string to a Todo object
    public Todo convertJsonStrToTodo(String jsonStr) {
        JsonReader reader = Json.createReader(new StringReader(jsonStr));
        JsonObject jsonObject = reader.readObject();
        try {
            return convertJsonObjToTodo(jsonObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    // Helper method
    private Todo convertJsonObjToTodo(JsonObject jsonObject) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MM/dd/yyyy");

        Todo todo = new Todo();

        todo.setId(jsonObject.getString("id"));
        todo.setName(jsonObject.getString("name"));
        todo.setDescription(jsonObject.getString("description"));
        Date dueDate = formatter.parse(jsonObject.getString("due_date"));
        todo.setDueDate(dueDate);
        todo.setPriority(jsonObject.getString("priority_level"));
        todo.setStatus(jsonObject.getString("status"));
        Date createdAt = formatter.parse(jsonObject.getString("created_at"));
        todo.setCreatedAt(createdAt);
        Date updatedAt = formatter.parse(jsonObject.getString("updated_at"));
        todo.setUpdatedAt(updatedAt);

        return todo;
    }
}
