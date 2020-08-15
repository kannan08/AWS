package com.amazonaws.lambda.demo;

import java.time.LocalDateTime;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class Hello implements RequestHandler<Object, String> {

	Regions clientRegion = Regions.US_EAST_1;
	String bucketName = "store-events-from-api";
	String stringObjKeyName = "";
	String fileName = "input/";

	@Override
	public String handleRequest(Object  input, Context context) {
		context.getLogger().log("Input: " + input.toString());
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
		/*
		 * if(input.getHttpMethod().equals("POST")) { stringObjKeyName = ""+ input; }
		 * else if(input.getHttpMethod().equals("GET")) { stringObjKeyName = ""+
		 * input.getQueryStringParameters(); }else {
		 */
			stringObjKeyName =""+input;
		//}

		LocalDateTime date = LocalDateTime.now();
		String s3fileName = fileName + date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth() + "-"
				+ date.getHour() + "-" + context.getAwsRequestId() + ".txt";
		try {
			s3Client.putObject(bucketName, s3fileName, stringObjKeyName).getMetadata().setContentType("plain/text");

		} catch (AmazonServiceException e) {
			e.printStackTrace();
		} catch (SdkClientException e1) {

			e1.printStackTrace();
		}
		return input.toString();
	}

}
