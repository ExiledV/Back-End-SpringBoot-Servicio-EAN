package com.tienda.gomez.raul.tiendaservicioproductos.constants;

public class ApiError {
    private String message;

    public ApiError(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
