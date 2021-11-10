package kg.geektech.taskapp36.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Task implements Serializable {
    private String text;
    private long createAdd;

    @PrimaryKey(autoGenerate = true)
    private long id;

    public Task(String text, long createAdd) {
        this.text = text;
        this.createAdd = createAdd;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreateAdd() {
        return createAdd;
    }

    public void setCreateAdd(long createAdd) {
        this.createAdd = createAdd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
