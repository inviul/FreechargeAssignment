package com.project.freecharge.utilities;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class RESTUtility {
    public static String path; //Rest request path

    public static void setBaseURI (String baseURI){
        RestAssured.baseURI = baseURI;
    }

    public static void resetBaseURI(String baseUrl){
        RestAssured.baseURI = null;
    }

    public static RequestSpecification requestSpecification(String accessToken){
        return RestAssured.given().auth().oauth2(accessToken);
    }

    public static Response getDefaultResponse(RequestSpecification httpReq){
        return httpReq.get();
    }

    public static Response restGetCall(RequestSpecification httpReq, String trailPath){
        return httpReq.request(Method.GET, trailPath);
    }

    public static Response restGetCallWithMediaType(RequestSpecification httpReq, String mediaType, String trailPath){
        return httpReq.accept(mediaType).request(Method.GET, trailPath);
    }

    public static Response restPostCallJson(JSONObject jsonObject, String mediaType, RequestSpecification httpReq, String trailPath){
        //httpReq.header("Content-Type", "application/json");
        httpReq.body(jsonObject.toJSONString());
        return httpReq.accept(mediaType).request(Method.POST, trailPath);
    }

    public static Response restPutCallJson(JSONObject jsonObject, String mediaType, RequestSpecification httpReq, String trailPath){
        //httpReq.header("Content-Type", "application/json");
        httpReq.body(jsonObject.toJSONString());
        return httpReq.accept(mediaType).request(Method.PUT, trailPath);
    }

    public static Response restPatchCallJson(JSONObject jsonObject, String mediaType, RequestSpecification httpReq, String trailPath){
        httpReq.header("Content-Type", "application/json");
        httpReq.body(jsonObject.toJSONString());
        return httpReq.accept(mediaType).request(Method.PATCH, trailPath);
    }

    public static Response restDeleteCall(RequestSpecification httpReq, String trailPath){
        return httpReq.request(Method.DELETE, trailPath);
    }

    public static int getStatusCode(Response response){
        return response.getStatusCode();
    }

    public static String getStringBody(Response response){
        return response.getBody().asString();
    }

    public static void setContentType (ContentType Type){
        given().contentType(Type);
    }

    public static void  createSearchQueryPath(String searchTerm, String jsonPathTerm, String param, String paramValue) {
        path = searchTerm + "/" + jsonPathTerm + "?" + param + "=" + paramValue;
    }

    public static JsonPath getJsonPath (Response res) {
        String json = res.asString();
        return new JsonPath(json);
    }
}