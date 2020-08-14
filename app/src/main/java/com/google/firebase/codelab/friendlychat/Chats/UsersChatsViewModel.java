package com.google.firebase.codelab.friendlychat.Chats;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UsersChatsViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public UsersChatsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is chat fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
