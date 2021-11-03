package kg.geektech.taskapp36.models;

import java.io.Serializable;

public class Task implements Serializable {
    private String text;
    private long createAdd;

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
}
