package ibf2023.ssf.practice.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class Todo {

    // Don't put @NotNull, @NotEmpty
    @Size(min=2, max=50, message="Maximum length is 50 characters")
    private String id;

    @NotEmpty(message="Name cannot be empty")
    @Size(min=10, max=50, message="Name must be between 10 and 50 characters")
    private String name;

    @Size(max=255, message="Description must not be more than 255 characters")
    private String description;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @FutureOrPresent(message="Date must be present or in the future")
    private Date dueDate;

    private String priority;

    private String status;

    private Date createdAt;

    private Date updatedAt;
    

    public Todo() {
        
    }


    public Todo(String name, String description, Date dueDate, String priority, String status, Date createdAt,
            Date updatedAt) {
        // this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Todo [id=" + id + 
                      ", name=" + name + 
                      ", description=" + description + 
                      ", dueDate=" + dueDate + 
                      ", priority=" + priority + 
                      ", status=" + status + 
                      ", createdAt=" + createdAt + 
                      ", updatedAt=" + updatedAt + "]";
    }
}
