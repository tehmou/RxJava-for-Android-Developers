package com.tehmou.chatclient;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;

import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivityPresenter {
    private final MainActivityViewModel mainActivityViewModel;
    private final ArrayAdapter<ChatMessage> arrayAdapter;
    private final View sendButton;
    private final EditText editText;

    private CompositeSubscription bindingSubscriptions;

    public MainActivityPresenter(MainActivity mainActivity, MainActivityViewModel mainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel;

        sendButton = mainActivity.findViewById(R.id.send_button);
        editText = (EditText) mainActivity.findViewById(R.id.edit_text);

        final ListView listView = (ListView) mainActivity.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
    }

    public void attach() {
        bindingSubscriptions = new CompositeSubscription();
        bindingSubscriptions.add(
                mainActivityViewModel
                        .getMessageList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                messageList -> {
                                    arrayAdapter.clear();
                                    arrayAdapter.addAll(messageList);
                                })
        );
        bindingSubscriptions.add(
                RxView.clicks(sendButton)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(ev -> {
                            final String text = editText.getText().toString();
                            if (text.length() > 0) {
                                mainActivityViewModel.sendMessage(text);
                                editText.setText("");
                            }
                        })
        );
    }

    public void detach() {
        bindingSubscriptions.unsubscribe();
        bindingSubscriptions = null;
    }

    public void destroy() {

    }
}
