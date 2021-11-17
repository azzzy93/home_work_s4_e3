package kg.geektech.taskapp36;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import kg.geektech.taskapp36.databinding.FragmentTaskBinding;
import kg.geektech.taskapp36.models.Task;

public class TaskFragment extends Fragment {
    private Task task;
    private FragmentTaskBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListeners();
    }

    private void initListeners() {
        task = (Task) requireArguments().getSerializable("task");
        if (task != null) {
            binding.editText.setText(task.getText());
        }

        binding.btnSave.setOnClickListener(view1 -> {
            if (!binding.editText.getText().toString().isEmpty()) {
                save();
            } else {
                close();
            }
        });
    }

    private void save() {
        String text = binding.editText.getText().toString().trim();

        if (task == null) {
            task = new Task(text, System.currentTimeMillis());
            App.getInstance().getDatabase().taskDao().insert(task);
            saveToFirestore(task);
        } else {
            task.setText(text);
            App.getInstance().getDatabase().taskDao().update(task);
            if (task.getDocId() != null) {
                updateToFirestore(task);
            } else {
                close();
            }
        }
    }

    private void updateToFirestore(Task task) {
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .document(task.getDocId())
                .update("text", task.getText())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(requireActivity(), "База данных успешно обновлено", Toast.LENGTH_SHORT).show();
                    close();
                });
    }

    private void saveToFirestore(Task task) {
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .add(task)
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful())
                        close();
                });
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}
