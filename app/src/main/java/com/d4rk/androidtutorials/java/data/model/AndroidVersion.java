package com.d4rk.androidtutorials.java.data.model;

public record AndroidVersion(
        String version,
        String api,
        String codeName,
        String codenameLiteral,
        String year
) { }