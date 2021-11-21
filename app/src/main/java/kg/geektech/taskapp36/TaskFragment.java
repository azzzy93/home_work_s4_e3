package kg.geektech.taskapp36;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import kg.geektech.taskapp36.databinding.FragmentTaskBinding;
import kg.geektech.taskapp36.models.Task;

public class TaskFragment extends Fragment {
    private Task myTask;
    private FragmentTaskBinding binding;
    private ActivityResultLauncher<String> getImage;
    private String uriStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        initListeners();
    }

    private void initViews() {
        binding.progressBar.setVisibility(View.GONE);

        myTask = (Task) requireArguments().getSerializable("task");
        if (myTask != null) {
            binding.editText.setText(myTask.getText());
            Glide.with(requireContext()).load(myTask.getImgUri()).into(binding.ivForUpload);
        }
    }

    private void initListeners() {
        getImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                binding.ivForUpload.setImageURI(result);
                upload(result);
                binding.btnSave.setEnabled(false);
            }
        });

        binding.ivForUpload.setOnClickListener(v -> {
            getImage.launch("image/*");
        });

        binding.btnSave.setOnClickListener(view1 -> {
            if (!binding.editText.getText().toString().isEmpty() && binding.ivForUpload.getDrawable() != null) {
                save();
            } else {
                Toast.makeText(requireActivity(), "!!! Поле для ввода пустое или фотография не выбрана !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upload(Uri uri) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String userID = FirebaseAuth.getInstance().getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("IMG_" + userID + "_" + timeStamp + ".jpg");
        reference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, com.google.android.gms.tasks.Task<Uri>>() {
            @Override
            public com.google.android.gms.tasks.Task<Uri> then(@NonNull com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> task) throws Exception {
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Uri> task) {
                uriStr = task.getResult().toString();
                binding.btnSave.setEnabled(true);
            }
        });
    }

    private void save() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnSave.setEnabled(false);

        String text = binding.editText.getText().toString().trim();

        if (myTask == null) {
            myTask = new Task(text, System.currentTimeMillis());
            if (uriStr != null) {
                myTask.setImgUri(uriStr);
            }
            App.getInstance().getDatabase().taskDao().insert(myTask);
            saveToFirestore(myTask);
        } else {
            if (!text.isEmpty()) {
                myTask.setText(text);
            }
            if (uriStr != null) {
                myTask.setImgUri(uriStr);
            }
            App.getInstance().getDatabase().taskDao().update(myTask);
            if (myTask.getDocId() != null) {
                updateToFirestore(myTask);
            } else {
                close();
            }
        }
    }

    private void updateToFirestore(Task task) {
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .document(task.getDocId()).update("text", task.getText(), "imgUri", task.getImgUri())
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
        binding.progressBar.setVisibility(View.GONE);
        binding.btnSave.setEnabled(true);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}
