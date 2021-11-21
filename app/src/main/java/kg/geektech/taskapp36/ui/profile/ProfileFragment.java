package kg.geektech.taskapp36.ui.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import kg.geektech.taskapp36.Prefs;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Prefs prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initListeners();
    }

    private void initViews() {
        prefs = new Prefs(requireContext());

        if (prefs.getString() != null) {
            binding.etProfile.setText(prefs.getString());
        }

        if (prefs.getStringImg() != null) {
            Glide.with(requireContext()).load(Uri.parse(prefs.getStringImg())).into(binding.ivProfile);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (!binding.etProfile.getText().toString().isEmpty()) {
            prefs.setString(binding.etProfile.getText().toString());
        }
    }

    private void initListeners() {

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        prefs.setStringImg(uri.toString());
                        binding.ivProfile.setImageURI(uri);
                    }
                });

        binding.ivProfile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });

        binding.btnSignOut.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("Выход с аккаунта");
            alertDialog.setMessage("Вы точно хотите выйти с аккаунта?");

            alertDialog.setNegativeButton("Нет", (dialog, which) -> {
                Toast.makeText(requireActivity(), "Вы остались в аккаунте", Toast.LENGTH_SHORT).show();
            });

            alertDialog.setPositiveButton("Да", (dialog, which) -> {
                FirebaseAuth.getInstance().signOut();
                openFragment();
            });

            alertDialog.show();
        });
    }

    private void openFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.loginFragment);
    }
}