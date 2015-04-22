package com.defch.blogwbly.util;

import android.content.Intent;

import com.defch.blogwbly.activities.MainActivity;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by DiegoFranco on 4/22/15.
 */
public class VoiceLIstener extends WearableListenerService {

    private static final String PATH = "/app/BlogWbly";

    private static final String COMMAND = "open weebly";
    private static final String MESSAGE = "hello world";

    @Override
    public void onMessageReceived(MessageEvent message) {
        super.onMessageReceived(message);
        if(message != null) {
            Intent i = new Intent(this, MainActivity.class);
            if(message.getPath().equalsIgnoreCase(PATH + COMMAND)) {
                i.setAction(MESSAGE);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
}
