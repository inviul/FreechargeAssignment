package com.project.freecharge.test;

import com.project.freecharge.controller.APITestBed;
import com.project.freecharge.controller.MyListeners;
import com.project.freecharge.utilities.RESTUtility;
import com.relevantcodes.extentreports.ExtentReports;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import com.project.freecharge.utilities.Configuration;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * This test class is to test the api call of GitHub Teams by using RestAssured. It inherits APITestBes class where
 * @BeforeSuite & @AfterSuite are implemented.
 * @BeforeTest annotation sets up reporting and RequestSpecification.
 * @AfterTest annotation tears down everything
 * @Test annotation contains the real tests
 * It implements ITestListener as well
 */


@Listeners(MyListeners.class)
public class GitHubTeamApiTesting extends APITestBed {

    RequestSpecification request;
    String projectId ="";


  @BeforeTest
    public void setUp(){
      reports = new ExtentReports(reportPath+this.getClass().getSimpleName()+".html",false);
      test= generateTest(reports, this.getClass().getSimpleName(), "This test is to validate the GitHub Team API");
      request= RESTUtility.requestSpecification(Configuration.ACCESS_TOKEN);
  }

  @AfterTest
    public void tearDown(){
      super.tearDown(test, reports);
  }

  @Test(alwaysRun = true, description = "Validate response code of base URL")
  public void validateBaseUrl(){
      Response response = RESTUtility.getDefaultResponse(request);
      int responseCodeBaseUrl = RESTUtility.getStatusCode(response);
      Assert.assertEquals(responseCodeBaseUrl,200);
  }

  @Test(priority = 1, groups={"Positive Test"}, description="Validate user get lists of team")
    public void listTeams(){
       Response response= RESTUtility.restGetCall(request, "/orgs/inviultestteam/teams");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body of Team List: "+body);
        Assert.assertEquals(responseCode, 200);
  }

    @Test(priority = 2, groups={"Positive Test"}, description="Validate user gets team by its name")
    public void getTeamByName(){
        Response response= RESTUtility.restGetCall(request, "/orgs/inviultestteam/teams/teaminviul");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body of Team Inviul: "+body);
        Assert.assertEquals(responseCode, 200);
    }

    @Test(priority = 3, groups={"Negative Test"}, description="Validate user can't create same team twice")
    public void createATeam(){
        String media = Configuration.DEFAULT_MEDIA_TYPE;
        JSONObject jObj = getJsonObject("teamcreation.json");
        Response response= RESTUtility.restPostCallJson(jObj, media, request, "/orgs/inviultestteam/teams");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertNotEquals(responseCode, 201);
    }

    @Test(priority = 4, groups={"Positive Test"}, description="Validate user creates a new team")
    public void createNewTeam(){
        String media = Configuration.DEFAULT_MEDIA_TYPE;
        JSONObject jObj = getJsonObject("newteam.json");
        Response response= RESTUtility.restPostCallJson(jObj, media, request, "/orgs/inviultestteam/teams");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertEquals(responseCode, 201);
    }

    @Test(priority = 5, groups={"Negative Test"}, description="Validate user can't edit existing team with same name")
    public void editTeamWithExistingData(){
        String media = Configuration.DEFAULT_MEDIA_TYPE;
        JSONObject jObj = getJsonObject("newteam.json");
        Response response= RESTUtility.restPatchCallJson(jObj, media, request, "/orgs/inviultestteam/teams/team-panther");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertEquals(responseCode, 422);
    }

    @Test(priority = 6, groups={"Positive Test"}, description="Validate user is able to edit existing team")
    public void editExistingTeam(){
        String media = Configuration.DEFAULT_MEDIA_TYPE;
        JSONObject jObj = getJsonObject("editteam.json");
        Response response= RESTUtility.restPatchCallJson(jObj, media, request, "/orgs/inviultestteam/teams/team-panther");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
       Assert.assertEquals(responseCode, 200);
    }


    @Test(priority = 7, groups={"Positive Test"}, description="Validate user is able to delete a team")
    public void deleteTeam(){
        Response response= RESTUtility.restDeleteCall(request, "/orgs/inviultestteam/teams/wolf-pack");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertEquals(responseCode, 204);
    }

    @Test(priority = 8, groups={"Positive Test"}, description="Validate user get lists of child team")
    public void listChildTeams(){
        Response response= RESTUtility.restGetCall(request, "/orgs/inviultestteam/teams/teaminviul/teams");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body of Child Team List: "+body);
        Assert.assertEquals(responseCode, 200);
    }

    @Test(priority = 9, groups={"Positive Test"}, description="Validate user get lists of team repos")
    public void listTeamRepo(){
        Response response= RESTUtility.restGetCall(request, "/orgs/inviultestteam/teams/teaminviul/repos");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertEquals(responseCode, 200);
    }

    @Test(priority = 10, groups={"Negative Test"}, description="Validate user can't get repo who doesn't have permission")
    public void teamManagesRepoNegativeTest1(){
        Response response= RESTUtility.restGetCall(request, "/orgs/inviultestteam/teams/team-panther/repos/inviultestteam/MyTestRepo");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertEquals(responseCode, 404);
    }

    @Test(priority = 11, groups={"Negative Test"}, description="Validate user gets repo who has permission")
    public void teamManagesRepoNegativeTest2(){
        Response response= RESTUtility.restGetCall(request, "/orgs/inviultestteam/teams/teaminviul/repos/inviultestteam/MyTestRepo");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertEquals(responseCode, 204);
    }

    @Test(priority = 12, groups={"Positive Test"}, description="Validate user gets repo who has ownership")
    public void teamManagesRepoPositiveTest(){
        String media = Configuration.JSON_MEDIA_TYPE;
        Response response= RESTUtility.restGetCallWithMediaType(request, media,"/orgs/inviultestteam/teams/teaminviul/repos/inviultestteam/MyTestRepo");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertEquals(responseCode, 200);
    }

    @Test(priority = 13, groups={"Positive Test"}, description="Validate list of users in a team")
    public void listUserTeams(){
        Response response= RESTUtility.restGetCall(request, "/user/teams");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertEquals(responseCode, 200);
    }

    @Test(priority = 14, groups={"Positive Test"}, description="Validate user gets a list of team projects")
    public void listTeamProjects(){
        String media = Configuration.JSON_MEDIA;
        Response response= RESTUtility.restGetCallWithMediaType(request, media,"/orgs/inviultestteam/teams/teaminviul/projects");
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertEquals(responseCode, 200);
        projectId=readObjectFromRestBody(body,"id");
    }

    @Test(priority = 15, groups={"Positive Test"}, description="Validate user reviews a team project")
    public void reviewTeamProjects(){
        String media = Configuration.JSON_MEDIA;
        Response response= RESTUtility.restGetCallWithMediaType(request, media,"/orgs/inviultestteam/teams/teaminviul/projects/"+projectId);
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertEquals(responseCode, 200);
    }

    @Test(priority = 16, groups={"Positive Test"}, description="Validate user can update the team project")
    public void updateTeamProjectPositiveTest(){
        String media = Configuration.JSON_MEDIA;
        JSONObject jObj = getJsonObject("updateteamproject.json");
        Response response= RESTUtility.restPutCallJson(jObj, media, request, "/orgs/inviultestteam/teams/teaminviul/projects/"+projectId);
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        System.out.println(responseCode);
        Assert.assertEquals(responseCode, 204);
    }

    @Test(priority = 17, groups={"Positive Test"}, description="Validate user removes team project")
    public void removeTeamProject(){
        String media = Configuration.JSON_MEDIA;
        Response response= RESTUtility.restDeleteCall(request, "/orgs/inviultestteam/teams/team-lion/projects/"+projectId);
        int responseCode = RESTUtility.getStatusCode(response);
        String body = RESTUtility.getStringBody(response);
        logger.info("JSON Body: "+body);
        Assert.assertEquals(responseCode, 204);
    }
}
