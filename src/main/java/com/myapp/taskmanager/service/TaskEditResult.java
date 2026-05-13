package com.myapp.taskmanager.service;

public enum TaskEditResult {
    EDIT_SUCCESS,
    TASK_NOT_FOUND,
    INVALID_PRIORITY,
    DUE_DATE_IN_PAST,
    DUE_DATE_INVALID_FORMAT,
    NO_CHANGES_PROVIDED,
    EMPTY_TITLE,
    TOO_LONG_TITLE
}