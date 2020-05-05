package com.ramaraj.tooltip;

/**
 * Created by ancientinc on 05/05/20.
 **/
public class ToolTipModel {

    private String pageName;

    private String componentId;

    private String title;

    private String message;

    private String backgroundColor;

    private String titleColor;

    private String messageColor;

    private String actionButtonColor;

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getMessageColor() {
        return messageColor;
    }

    public void setMessageColor(String messageColor) {
        this.messageColor = messageColor;
    }

    public String getActionButtonColor() {
        return actionButtonColor;
    }

    public void setActionButtonColor(String actionButtonColor) {
        this.actionButtonColor = actionButtonColor;
    }
}
