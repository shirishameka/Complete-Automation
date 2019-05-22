package com;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by shirisha
 * since  5/21/19.
 */
@Slf4j
public class ApiBase {


    //common methods

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    public HashMap<String, String> accessToken = new HashMap<String, String>();


    public enum  service {
        SERVICE,SERVICE2,API_KEY
    }

    public HashMap<String,String > getAPPTokens(String service){
        HashMap<String,String> hashMap=new HashMap<>();

        switch (service) {
            case "SERVICE":
                hashMap.put("X-APP-Token", "");
                return  hashMap;
            case "SERVICE2":
                hashMap.put("X-APP-Token", "");
                return hashMap;
            case "API_KEY":
                hashMap.put("X-APP-Token", "");
                return hashMap;
        }
        return null;
    }

}


