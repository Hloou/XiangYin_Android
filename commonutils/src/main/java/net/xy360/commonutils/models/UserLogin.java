package net.xy360.commonutils.models;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jolin on 2016/2/26.
 */
public class UserLogin {
    public String telephone;
    public String password;

    public UserLogin(String telephone, String password) {
        this.telephone = telephone;
        this.password = password;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("telephone", telephone);
        map.put("password", password);
        return map;
    }
}
