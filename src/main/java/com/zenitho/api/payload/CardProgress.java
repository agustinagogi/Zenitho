package com.zenitho.api.payload;

import lombok.Data;

@Data
public class CardProgress {
    private double progressPercentage;
    private int completedTasks;
    private int totalTasks;
}