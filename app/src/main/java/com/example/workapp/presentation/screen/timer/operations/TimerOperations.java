package com.example.workapp.presentation.screen.timer.operations;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimerOperations implements TimerOperationsApi {

    @Override
    public String calculateDifference(@NonNull Date dateOfFinish, @NonNull Date dateOfStart)
            throws NullPointerException {
        long milliseconds = dateOfStart.getTime() - dateOfFinish.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        return (hours + " hours " + minutes + " minutes " + seconds + " seconds ");
    }

    @Override
    public String setTime() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat
                ("dd.MM.yyyy HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }

    @NotNull
    public Date getCalendarInstance() {
        return Calendar.getInstance().getTime();
    }
}