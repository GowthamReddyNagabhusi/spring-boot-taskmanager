package com.myapp.taskmanager.service;

public enum TaskAddResult {
    ADD_SUCCESS,
    EMPTY_TITLE,
    TOO_LONG_TITLE,
    INVALID_PRIORITY,
    DUE_DATE_INVALID_FORMAT
}