package fr.vlock.app.ui.notifications2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import fr.vlock.app.databinding.FragmentPlusBinding;
import fr.vlock.app.ui.notifications2.Notifications2ViewModel;

public class Notifications2Fragment extends Fragment {

    private FragmentPlusBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Notifications2ViewModel notifications2ViewModel =
                new ViewModelProvider(this).get(Notifications2ViewModel.class);

        binding = FragmentPlusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}