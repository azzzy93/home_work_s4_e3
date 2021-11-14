package kg.geektech.taskapp36.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kg.geektech.taskapp36.models.Task;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task order by createAdd DESC")
    List<Task> getAll();

    @Query("SELECT * FROM task order by text")
    List<Task> getAllSortedByTitle();

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

}
