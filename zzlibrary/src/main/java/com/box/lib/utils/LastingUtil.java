package com.box.lib.utils;


import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 序列化对象持久化工具类
 * Created by xxy on 2016/8/15 0015.
 */
public class LastingUtil {
    private static final int CACHE_TIME = 60 * 60000;// 缓存失效时间

    /**
     * 判断文件是否存在
     *
     * @param cacheFile
     * @return
     */
    private static boolean isExistDataCache(Context context, String cacheFile) {
        return context.getFileStreamPath(cacheFile).exists();
    }

    /**
     * 判断文件是否失效
     *
     * @param cacheFile
     * @return
     */
    public static boolean isCacheDataFailure(Context context, String cacheFile) {
        File data = context.getFileStreamPath(cacheFile);
        return !data.exists() || (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME;
    }

    /**
     * 清除持久文件
     *
     * @param context
     * @param file
     * @return
     */
    public static boolean delete(Context context, String file) {
        return context.getFileStreamPath(file).delete();
    }

    /**
     * 保存对象
     *
     * @param ser
     * @param file
     */
    public static boolean saveObject(Context context, Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(file, Application.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     */
    public static <T extends Serializable> T readObject(Context context, String file) {
        if (!isExistDataCache(context, file)) return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (T) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = context.getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
