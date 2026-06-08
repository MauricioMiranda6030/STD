package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task{
    private String name;
    private Boolean status;

    public void switchStatus(){
        this.status = !this.status;
    }
}
