package kg.geektech.taskapp36.ui.on_board;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;

import kg.geektech.taskapp36.Prefs;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentBoardBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;


public class BoardFragment extends Fragment {
    private FragmentBoardBinding binding;
    private BoardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBoardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initListeners();
    }

    private void initView() {
        adapter = new BoardAdapter();
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayoutBoard, binding.viewPager, ((tab, position) -> {
        })).attach();
    }

    private void initListeners() {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                closeFragment();
            }

            @Override
            public void onLongClick(int position) {

            }
        });

        binding.btnScip.setOnClickListener(v -> {
            closeFragment();
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });

    }

    private void closeFragment() {
        Prefs prefs = new Prefs(requireContext());
        prefs.saveBoardState();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}