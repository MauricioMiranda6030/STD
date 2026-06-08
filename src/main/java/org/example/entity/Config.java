package org.example.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class Config{
    private int timerHour, timerMin;
    private int resetHour, resetMin;
}


