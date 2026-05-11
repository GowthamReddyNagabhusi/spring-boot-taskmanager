package com.myapp.taskmanager.service;

public enum TaskEditResult {
    EDIT_SUCCESS,
    INVALID_PRIORITY,
    TASK_NOT_FOUND,
    DUE_DATE_IN_PAST,
    DUE_DATE_INVALID_FORMAT,
    NO_CHANGES_PROVIDED
}
