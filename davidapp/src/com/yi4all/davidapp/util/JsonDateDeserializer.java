package com.yi4all.davidapp.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

public class JsonDateDeserializer implements JsonDeserializer<Date> {
	   public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	      String s = json.getAsJsonPrimitive().getAsString();
	      long l = Long.parseLong(s);
	      Date d = new Date(l);
	      return d; 
	   } 
	}