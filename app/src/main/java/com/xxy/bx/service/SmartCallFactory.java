package com.xxy.bx.service;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class SmartCallFactory extends CallAdapter.Factory {
    //缓存类
    private final CachingSystem cachingSystem;
    //线程池
    private final Executor asyncExecutor;

    public SmartCallFactory(CachingSystem cachingSystem) {
        this.cachingSystem = cachingSystem;
        this.asyncExecutor = new AndroidExecutor();
    }

    public SmartCallFactory(CachingSystem cachingSystem, Executor executor) {
        this.cachingSystem = cachingSystem;
        this.asyncExecutor = executor;
    }

    @Override
    public CallAdapter<SmartCall<?>> get(final Type returnType, final Annotation[] annotations, final Retrofit retrofit) {

        TypeToken<?> token = TypeToken.of(returnType);
        //如果不是对应的SmartCall则不会执行
        if (token.getRawType() != SmartCall.class) {
            return null;
        }

        if (!(returnType instanceof ParameterizedType)) {
            //必须有实体类
            throw new IllegalStateException(
                    "SmartCall must have generic type (e.g., SmartCall<ResponseBody>)");
        }

        //获得SmartCall<T>中的类型
        final Type responseType = ((ParameterizedType) returnType).getActualTypeArguments()[0];
        final Executor callbackExecutor = asyncExecutor;

        return new CallAdapter<SmartCall<?>>() {
            @Override
            public Type responseType() {
                return responseType;
            }

            @Override
            public <R> SmartCall<R> adapt(Call<R> call) {

                return new SmartCallImpl<>(callbackExecutor, call, responseType(), annotations,
                        retrofit, cachingSystem);
            }
        };
    }

    /**
     * 自定义一个回调实例
     * 所有的逻辑都是在这里面
     *
     * @param <T>
     */
    static class SmartCallImpl<T> implements SmartCall<T> {
        private final Executor callbackExecutor;
        private final Call<T> baseCall;
        private final Type responseType;
        private final Annotation[] annotations;
        private final Retrofit retrofit;
        private final CachingSystem cachingSystem;
        private final Request request;

        public SmartCallImpl(Executor callbackExecutor, Call<T> baseCall, Type responseType, Annotation[] annotations, Retrofit retrofit, CachingSystem cachingSystem) {
            this.callbackExecutor = callbackExecutor;
            this.baseCall = baseCall;
            this.responseType = responseType;
            this.annotations = annotations;
            this.retrofit = retrofit;
            this.cachingSystem = cachingSystem;

            // This one is a hack but should create a valid Response (which can later be cloned)
            this.request = buildRequestFromCall();
        }

        /***
         * 构建一个新的请求
         * 这里使用的反射机制
         * * @return A valid Request (that contains query parameters, right method and endpoint)
         */
        private Request buildRequestFromCall() {
            try {
                Field argsField = baseCall.getClass().getDeclaredField("args");
                argsField.setAccessible(true);
                Object[] args = (Object[]) argsField.get(baseCall);
                //retrofit2.0更改了字段(1.0+)requestFactory-->(2.0+)serviceMethod
                Field serviceMethodField = baseCall.getClass().getDeclaredField("serviceMethod");
                serviceMethodField.setAccessible(true);
                Object requestFactory = serviceMethodField.get(baseCall);
                //retrofit2.0更改了方法(1.0+)create-->(2.0+)toRequest
                Method createMethod = requestFactory.getClass().getDeclaredMethod("toRequest", Object[].class);
                createMethod.setAccessible(true);
                return (Request) createMethod.invoke(requestFactory, new Object[]{args});
            } catch (Exception exc) {
//                Log.e("buildRequestFromCall"+exc.toString());
                return null;
            }
        }

        public void enqueueWithCache(final Callback<T> callback) {
            Runnable enqueueRunnable = new Runnable() {
                @Override
                public void run() {
                    /* 读取缓存 */
                    byte[] data = cachingSystem.getFromCache(buildRequest());
                    if (data != null) {
                        //获得缓存数据
                        final T convertedData = SmartUtils.bytesToResponse(retrofit, responseType, annotations, data);
                        Runnable cacheCallbackRunnable = new Runnable() {
                            @Override
                            public void run() {
                                //存在数据直接回调给调用者,
                                callback.onResponse(baseCall, Response.success(convertedData));
                            }
                        };
                        callbackExecutor.execute(cacheCallbackRunnable);
                    }

                    /* 运行网络请求 */
                    baseCall.enqueue(new Callback<T>() {
                        @Override
                        public void onResponse(final Call<T> call, final Response<T> response) {
                            Runnable responseRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (response.isSuccessful()) {
                                        //保存数据
                                        byte[] rawData = SmartUtils.responseToBytes(retrofit, response.body(),
                                                responseType(), annotations);
                                        cachingSystem.addInCache(response, rawData);
                                    }
                                    //再一次回调给调用者
                                    callback.onResponse(call, response);
                                }
                            };
                            // Run it on the proper thread
                            callbackExecutor.execute(responseRunnable);
                        }

                        @Override
                        public void onFailure(final Call<T> call, final Throwable t) {
                            Runnable failureRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure(call, t);
                                }
                            };
                            callbackExecutor.execute(failureRunnable);
                        }

                    });

                }
            };
            Thread enqueueThread = new Thread(enqueueRunnable);
            enqueueThread.start();
        }

        @Override
        public void enqueue(final Callback<T> callback) {
            if (buildRequest().method().equals("GET")) {
                //只对GET请求有用
                enqueueWithCache(callback);
            } else {
                //其他的请求和retrofit一样
                baseCall.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(final Call<T> call, final Response<T> response) {
                        callbackExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onResponse(call, response);
                            }
                        });
                    }

                    @Override
                    public void onFailure(final Call<T> call, final Throwable t) {
                        callbackExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(call, t);
                            }
                        });
                    }
                });
            }
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public Request buildRequest() {
            return request.newBuilder().build();
        }

        @Override
        public SmartCall<T> clone() {
            return new SmartCallImpl<>(callbackExecutor, baseCall.clone(), responseType(),
                    annotations, retrofit, cachingSystem);
        }

        @Override
        public Response<T> execute() throws IOException {
            return baseCall.execute();
        }

        @Override
        public void cancel() {
            baseCall.cancel();
        }
    }
}