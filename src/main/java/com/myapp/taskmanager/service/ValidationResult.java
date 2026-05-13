package com.myapp.taskmanager.service;

public enum ValidationResult {
    VALID,
    NO_INPUT,
    EMPTY_TITLE,
    TOO_LONG_TITLE,
    INVALID_DATE,
    IN_PAST,
    INVALID_PRIORITY
}

