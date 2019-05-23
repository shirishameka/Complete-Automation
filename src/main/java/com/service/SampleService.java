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
public class SampleService extends ApiBase{

    public String getBreweryDetails() throws IOException {
        String url= Constants.Service.OPENBREWERY+Constants.TestUrl.GETINFO;

        HttpConnection connection=new HttpConnection();
        return connection.execute(url,"GET","");
    }
}



