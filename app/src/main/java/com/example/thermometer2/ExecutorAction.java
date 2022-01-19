package com.example.thermometer2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;


public class ExecutorAction implements Runnable{
    TextView textView;
    GetThermometerValueAlt connectionThread;
    MainActivity mainActivity;
    public ExecutorAction(TextView tv,GetThermometerValueAlt value,MainActivity mainActivity){
        this.textView=tv;
        this.connectionThread = value;
        this.mainActivity = mainActivity;
    }

    @Override
    public void run(){
        RefreshTextViewExecutor(textView, connectionThread.readStringHtml());
    }
    public void RefreshTextViewExecutor(TextView tv, String value) {
        if (value!=null && tv!=null){
//            Test
//            tv= mainActivity.findViewById(R.id.textView);
//            if (tv.getText().equals(value)) {
//                value = (Integer.parseInt(value) + 3) + "";
//            }

            Log.i("ExecutorAction", "Start");
            MainActivity.staticCreateNotification("Температура", "Температура печки  " + tv.getText() + " градусов", 1212,"temperature_notification",mainActivity);
            Handler mainHandler = new Handler(Looper.getMainLooper());
            String finalValue = value;
            TextView finalTv = tv;
            Runnable myRunnable = () -> finalTv.setText(finalValue);
            mainHandler.post(myRunnable);
            Log.i("ExecutorAction", "End");
        }
    }
}
