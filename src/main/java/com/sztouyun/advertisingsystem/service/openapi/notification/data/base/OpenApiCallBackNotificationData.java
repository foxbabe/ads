package com.sztouyun.advertisingsystem.service.openapi.notification.data.base;

public abstract class OpenApiCallBackNotificationData  extends OpenApiNotificationData{

    private boolean success;
    private String  message;
    private String thirdPartId;

    public OpenApiCallBackNotificationData(NotificationTypeEnum notificationType) {
        super(notificationType);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThirdPartId() {
        return thirdPartId;
    }

    public void setThirdPartId(String thirdPartId) {
        this.thirdPartId = thirdPartId;
    }
}
