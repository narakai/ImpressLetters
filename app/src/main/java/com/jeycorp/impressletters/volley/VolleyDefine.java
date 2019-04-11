package com.jeycorp.impressletters.volley;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class VolleyDefine {

    public static final int DEFAULT_TIMEOUT_MS = 30000;
    public static final int DEFAULT_MAX_RETRIES = 0;

	public static final Gson gson = new GsonBuilder()
    .registerTypeAdapter(Timestamp.class, new JsonSerializer<Timestamp>() {

        @Override
        public JsonElement serialize(Timestamp src, Type typeOfSrc, JsonSerializationContext context) {
            // TODO Auto-generated method stub

            Timestamp.class.getGenericSuperclass();
            return new JsonPrimitive(src.getTime());
        }
    })
        .registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() {

                @Override
                public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    // TODO Auto-generated method stub

                    long time = 0;
                    if (TextUtils.isEmpty(json.getAsString()) == false) {
                        if (Pattern.matches("^[0-9]*$", json.getAsString()) == false) {
                            try {
                                String strTime = json.getAsString().trim();
                                if (strTime.length() == 19) {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                                    Date date = simpleDateFormat.parse(strTime);
                                    time = date.getTime();
                                } else if (strTime.length() == 10) {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    Date date = simpleDateFormat.parse(strTime);
                                    time = date.getTime();
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();

                            }
                        } else {
                            time = Long.parseLong(json.getAsString());
                        }

                    } else {
                        time = 0;
                    }

                    return new Timestamp(time);
                }
            }

        )
                .

                    create();
        }
