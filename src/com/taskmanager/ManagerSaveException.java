package com.taskmanager;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException{

    public ManagerSaveException(String massage) {
        super(massage);
    }

    public ManagerSaveException(String message, IOException e) {
        super(message);
    }
}
