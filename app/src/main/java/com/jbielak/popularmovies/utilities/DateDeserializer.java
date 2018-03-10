package com.jbielak.popularmovies.utilities;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Justyna on 2018-03-08.
 */

public class DateDeserializer implements JsonDeserializer<Date> {

    private static final String TAG = DateDeserializer.class.getSimpleName();

    @Override
    public Date deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            String date = jsonElement.getAsString();
            return formatter.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }
}
