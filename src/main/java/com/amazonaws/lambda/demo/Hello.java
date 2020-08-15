package com.amazonaws.lambda.demo;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class Hello implements RequestHandler<Object, String> {
	
	 Regions clientRegion = Regions.US_EAST_1;
     String bucketName = "store-events-from-api";
     String stringObjKeyName = "";
     String fileObjKeyName = "test";
     String fileName = "input/";

    @Override
    public String handleRequest(Object input, Context context) {
    	 context.getLogger().log("Input: " + input);
    	  stringObjKeyName = ""+input;
    	  LocalDateTime date = LocalDateTime.now();
    	  fileName= fileName+date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth()+"-"+date.getHour()+"-"+context.getAwsRequestId()+".txt";
    	  try {
    	  AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                  .withRegion(clientRegion)
                  .build();

          s3Client.putObject(bucketName,fileName, stringObjKeyName).getMetadata().setContentType("plain/text");


      } catch (AmazonServiceException e) {
          // The call was transmitted successfully, but Amazon S3 couldn't process 
          // it, so it returned an error response.
          e.printStackTrace();
      } catch (SdkClientException e1) {
          // Amazon S3 couldn't be contacted for a response, or the client
          // couldn't parse the response from Amazon S3.
          e1.printStackTrace();
      }
      return input.toString();
    }

}
