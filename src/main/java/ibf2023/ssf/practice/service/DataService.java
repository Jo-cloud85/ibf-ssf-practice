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
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;

@Service
public class DataService {

    // Reading from todos.txt file and create a giant Json string
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


    // Convert giant Json string to a List to display - nothing to Redis yet
    // Rmb the dates inside this giant Json string are in String
    public List<Todo> createListFromExtgJsonStr() throws ParseException {
        String jsonStr = readFile();
        JsonArray jsonArray = Json.createReader(new StringReader(jsonStr)).readArray();

        List<Todo> todoList = new LinkedList<>();

        for (int i=0; i<jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).asJsonObject();
            Todo todo = convertJsonObjToTodo(jsonObject);
            todoList.add(todo);
        }
        return todoList;
    }


    // Convert each Todo object to a Json string for Redis
    // Here, time gets stored as epochmilliseconds
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
    // Rmb the dates in the original Json object string from todos.txt are in "EEE, MM/dd/yyyy"
    private Todo convertJsonObjToTodo(JsonObject jsonObject) throws ParseException {
        Todo todo = new Todo();

        todo.setId(jsonObject.getString("id"));
        todo.setName(jsonObject.getString("name"));
        todo.setDescription(jsonObject.getString("description"));
        Date dueDate = convertJsonObjDateToDate(jsonObject, "due_date");
        todo.setDueDate(dueDate);
        todo.setPriority(jsonObject.getString("priority_level"));
        todo.setStatus(jsonObject.getString("status"));
        Date createdAtDate = convertJsonObjDateToDate(jsonObject, "created_at");
        todo.setCreatedAt(createdAtDate);
        Date updatedAtDate = convertJsonObjDateToDate(jsonObject, "updated_at");
        todo.setUpdatedAt(updatedAtDate);

        return todo;
    }


    private Date convertJsonObjDateToDate(JsonObject jsonObject, String key) {
        /* The tricky part is that when you first take dates from todos.txt, the dates are in strings
         * just that they are in this format - EEE, MM/dd/yyyy.
         * When write to List, it's converting from String to Date (Sun Oct 15 00:00:00 SGT 2023)
         * When save from List to Redis, it's converting from Date (Sun Oct 15 00:00:00 SGT 2023) to Long
         * When retrieve from Redis to List, it's converting from Long to Date (Sun Oct 15 00:00:00 SGT 2023)
         * so this JsonObjDate can be either in Sun Oct 15 00:00:00 SGT 2023 or epochmilliseconds date. 
         * Lastly, you see this format EEE, MM/dd/yyyy is because we use Thymeleaf to format. */
        SimpleDateFormat formatter = new SimpleDateFormat("E, MM/dd/yyyy");
        Date date = null;

        try {
            JsonValue jsonValue = jsonObject.get(key);
            if (jsonValue.getValueType() == ValueType.STRING) {
                JsonString jsonString = (JsonString) jsonValue;
                date = formatter.parse(jsonString.getString());
            } else if (jsonValue.getValueType() == ValueType.NUMBER) {
                JsonNumber jsonNumber = (JsonNumber) jsonValue;
                date = new Date(jsonNumber.longValue());
            }
            return date;
        } catch (ClassCastException e) {
            System.err.printf("Key: %s, cannot be casted... \n", key);
            // e.printStackTrace();
            return null; // Or throw a ParseException, or return a default date
        } catch (ParseException pe) {
            System.err.printf("Json object date %s cannot be parsed...", jsonObject.toString());
            // pe.printStackTrace();
            return null;
        }
    }
}
