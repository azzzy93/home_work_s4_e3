package kg.geektech.taskapp36.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kg.geektech.taskapp36.Prefs;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> list;
    private OnItemClickListener onItemClickListener;
    private View view;

    public TaskAdapter() {
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));

        if (position % 2 == 0) {
            view.setBackgroundResource(R.color.red);
        } else {
            view.setBackgroundResource(R.color.green);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Task task) {
        list.add(0, task);
        notifyItemInserted(list.indexOf(task));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public String getStringPosition(int pos) {
        return list.get(pos).getText();
    }

    public void changeString(String s, int i) {
        list.remove(i);
        list.add(i, new Task(s, System.currentTimeMillis()));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitleTask);

            itemView.setOnClickListener(view -> {
                onItemClickListener.onClick(getAdapterPosition());
            });

            itemView.setOnLongClickListener(view -> {
                onItemClickListener.onLongClick(getAdapterPosition());
                return true;
            });
        }

        public void bind(Task task) {
            textTitle.setText(task.getText());
        }
    }
}
