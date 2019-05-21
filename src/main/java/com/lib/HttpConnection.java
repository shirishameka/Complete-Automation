package com.lib;

import com.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;


@Slf4j
public class HttpConnection {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Header[] responseHeaders = null;

    Map<String, String> reqHeaders = null;

    List<NameValuePair> formParams = null;

    private boolean defaultHeader = true;

    public String execute(String url, String op, Object req) throws IOException {
        String json = JsonUtils.getJsonStringFromObject(req);
        return execute(url, op, json);
    }

    public void addFormParam(String key, String value) {
        if (formParams == null)
            formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair(key, value));
    }


    public String execute(String url, String op, String json) throws IOException {
        return execute(url, op, json, null);
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), len);
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public String execute(String url, String op, String reqJson, HashMap<String, String> reqHeaders) throws IOException {
        return execute(url, op, reqJson, reqHeaders, defaultHeader);
    }

    public String execute(String url, String op, String reqJson, HashMap<String, String> reqHeaders, boolean addDefaultHeaders) throws IOException {
        InputStream inputStream = null;
        String result = "";
        HttpResponse httpResponse = null;
        try {
            CloseableHttpClient httpclient = HttpClients.custom().setUserAgent("Agent").build();
            HashMap<String, String> defHeaders = new HashMap<String, String>();

            HttpRequestBase httpMethod = new HttpPost(url);
            if (op.equalsIgnoreCase("POST"))
                httpMethod = new HttpPost(url);
            else if (op.equalsIgnoreCase("GET"))
                httpMethod = new HttpGet(url);
            else if (op.equalsIgnoreCase("PUT"))
                httpMethod = new HttpPut(url);
            else if (op.equalsIgnoreCase("DELETE"))
                httpMethod = new HttpDelete(url);
            else if(op.equalsIgnoreCase("PATCH"))
                httpMethod=new HttpPatch(url);


            StringEntity se = new StringEntity(reqJson, "UTF-8");

            if (!CollectionUtils.isEmpty(formParams))
                ((HttpEntityEnclosingRequest) httpMethod).setEntity(new UrlEncodedFormEntity(formParams));
            else if (httpMethod instanceof HttpEntityEnclosingRequest) {
                defHeaders.put("Content-type", "application/json");
                ((HttpEntityEnclosingRequest) httpMethod).setEntity(se);
            } else
                defHeaders.put("Content-type", "application/json");


            if (addDefaultHeaders)
                for (String key : defHeaders.keySet())
                    httpMethod.setHeader(key, defHeaders.get(key));

            if (reqHeaders != null && reqHeaders.size() > 0) {
                for (Map.Entry<String, String> entry : reqHeaders.entrySet()) {
                    httpMethod.setHeader(entry.getKey(), entry.getValue());
                }

                Header[] headers = httpMethod.getAllHeaders();
                for (Header header : headers)
                    logger.info("Request header name" + header.getName()
                            + " value " + header.getValue());
            }

            logger.info("Method" + " " + op + "," + "Request url : " + url);
            httpResponse = httpclient.execute(httpMethod);


            inputStream = httpResponse.getEntity().getContent();

            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                inputStream = new GZIPInputStream(inputStream);
            }


            if (inputStream != null)
                result = readIt(inputStream, 10240);
            else
                result = "Did not work!";

            if (!reqJson.isEmpty()) {
                logger.info("Request json : " + reqJson.toString());
            }

            logger.info("Response : " + result);


            NetworkInstance.getInstance().setJson(result);
            NetworkInstance.getInstance().setCode(httpResponse.getStatusLine().getStatusCode());
            NetworkInstance.getInstance().setMsg(httpResponse.getStatusLine().getReasonPhrase());
            NetworkInstance.getInstance().setHeaders(httpResponse.getAllHeaders());
            responseHeaders = httpResponse.getAllHeaders();


        } catch (Exception e) {
            System.out.println("InputStream:::" + e.getLocalizedMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

        }

        return result;
    }

}
