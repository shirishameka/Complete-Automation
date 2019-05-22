package com.service;

import com.ApiBase;
import com.constants.Constants;
import com.lib.HttpConnection;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by shirisha
 * since  5/21/19.
 */
public class GeoCodingService extends ApiBase{

    public String getGeoCodingDetails(String address,String key) throws IOException {
        String url= Constants.Service.GEOCODING+Constants.TestUrl.GETINFO + "?address=" +address + "&Key=" + key;

        HttpConnection connection=new HttpConnection();
        return connection.execute(url,"GET","",getAPPTokens("BINTAN"));
    }
}
