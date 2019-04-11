package com.jeycorp.impressletters.result;


import com.jeycorp.impressletters.type.User;
import com.jeycorp.impressletters.type.Device;
import com.jeycorp.impressletters.type.Setting;

import java.util.Map;

public class GetUserResult extends BaseResult{
    private User user;
    private Device device;
    private Setting setting;

    private Map<String,String> register;
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Map<String, String> getRegister() {
        return register;
    }
    public void setRegister(Map<String, String> register) {
        this.register = register;
    }
}
