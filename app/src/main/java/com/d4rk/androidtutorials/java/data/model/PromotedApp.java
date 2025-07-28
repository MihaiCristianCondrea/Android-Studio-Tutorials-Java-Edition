package com.d4rk.androidtutorials.java.data.model;

/**
 * Model representing a promoted application fetched from the remote API.
 */
public record PromotedApp(String name, String packageName, String iconUrl) {}
