package com.jeycorp.impressletters.result;

public class BaseResult {
    public static final int RESULT_CODE_OK = 0;
    public static final int RESULT_CODE_MESSAGE = 1000;
    public static final int RESULT_CODE_FINISH_AFTER_MESSAGE = 2000;
    public static final int RESULT_CODE_LOGOUT = 3000;

    private int resultCode;
    private String resultMessage;
    private String que;

    public int getResultCode() {
        return resultCode;
    }
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    public String getResultMessage() {
        return resultMessage;
    }
    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
    public String getQue() {
        return que;
    }
    public void setQue(String que) {
        this.que = que;
    }
}
