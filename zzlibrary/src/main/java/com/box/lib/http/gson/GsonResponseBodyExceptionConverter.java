package com.box.lib.http.gson;

/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;


final class GsonResponseBodyExceptionConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;

    GsonResponseBodyExceptionConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return adapter.fromJson(new StringReader(throwException(value.string())));
        } finally {
            if (value != null)
                value.close();
        }
    }

    private String throwException(String bodyStr) {
        try {
            JSONObject obj = new JSONObject(bodyStr);
            if (obj.getInt("error") != 0)
                throw new RuntimeException("数据" + obj.getString("status"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return bodyStr;
    }
}
