package com.example.springschedulemanagement.dto;

import lombok.Data;

@Data
public class GenericDTO<T> {
    private T id;
}
