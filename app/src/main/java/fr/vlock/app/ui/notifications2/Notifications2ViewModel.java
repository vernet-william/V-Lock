package fr.vlock.app.ui.notifications2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Notifications2ViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public Notifications2ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment2");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
