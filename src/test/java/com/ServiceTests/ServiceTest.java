package com.ServiceTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.lib.NetworkInstance;
import com.service.SampleService;
import com.utils.JsonNodeHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by shirisha
 * since  5/21/19.
 */
public class ServiceTest {

    @Test
    public void getDepartmentById() throws IOException {
        SampleService service = new SampleService();
        String response = service.getBreweryDetails();
        JsonNode node = JsonNodeHelper.stringToJsonNode(response);
        Assert.assertEquals(NetworkInstance.getInstance().getCode(),200,"Status codes not matching");
        Assert.assertEquals(node.get(0).get("brewery_type").asText(), "micro", "Brewery type not matching");
        Assert.assertNotNull(node.get(0).get("id"));
    }
}
