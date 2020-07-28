package com.example.workapp.presentation.screen.timer.operations;

import androidx.annotation.NonNull;

import java.util.Date;

public interface TimerOperationsApi {

    String calculateDifference(@NonNull Date dateOfFinish, @NonNull Date dateOfStart);

    String setTime();
}