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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kg.geektech.taskapp36.models.Task;

public class TaskFragment extends Fragment {
    private EditText editText;
    private Button buttonSave;
    private Bundle getBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.editText);
        buttonSave = view.findViewById(R.id.btnSave);


        if (getArguments() != null) {
            getBundle = getArguments();
            String s = getBundle.getString("keyString");
            editText.setText(s);

            buttonSave.setOnClickListener(v -> {
                change();
            });
        } else {
            buttonSave.setOnClickListener(view1 -> {
                save();
            });
        }
    }

    private void change() {
        if (!editText.getText().toString().isEmpty()) {
            Bundle setBundle = new Bundle();
            setBundle.putString("keyString1", editText.getText().toString().trim());
            setBundle.putInt("keyInt1", getBundle.getInt("keyInt"));
            getParentFragmentManager().setFragmentResult("rk_task1", setBundle);
        }
        close();
    }

    private void save() {
        if (!editText.getText().toString().isEmpty()) {
            String text = editText.getText().toString().trim();
            Task task = new Task(text, System.currentTimeMillis());
            Bundle bundle = new Bundle();
            bundle.putSerializable("task", task);
            getParentFragmentManager().setFragmentResult("rk_task", bundle);
        }
        close();
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}
