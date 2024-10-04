package com.example.library.web.model;

import lombok.Data;

@Data
public class UpsertBookRequest {

    private String title;

    private String author;

    private String categoryName;

}
