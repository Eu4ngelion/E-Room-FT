package com.eroomft.restful.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Wrapper standar untuk semua response API. Field 'data' dapat berupa objek, array, atau null tergantung endpoint.")
public class ResponseWrapper {
    @Schema(description = "Status response (success/error)", example = "success")
    private String status;

    @Schema(description = "Pesan hasil operasi", example = "Login berhasil")
    private String message;

     @Schema(description = "Data hasil operasi, bisa berupa objek, array, atau null")
    private Object data;

    public ResponseWrapper() {
    }

    public ResponseWrapper(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
}
