package com.absurd.onlite.base;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: mr-absurd
 * Github: http://github.com/mr-absurd
 * Data: 2017/9/13.
 */

public abstract class BaseDao<T> implements IBaseDao<T> {

    private SQLiteDatabase sqLiteDatabase;
    private boolean isInit=false;
    private String tableName;
    private Class<T> entityClass;
    private Map<String,Field>  cacheMap;
    public synchronized void init(Class<T> entity,SQLiteDatabase sqLiteDatabase){
        if (!isInit){
            this.sqLiteDatabase=sqLiteDatabase;
            this.tableName=entity.getAnnotation(OnTable.class).value();
            entityClass=entity;
            sqLiteDatabase.execSQL(createDataBase());
            initCacheMap();

        }
    }

    protected   void initCacheMap(){
        cacheMap=new HashMap<>();
    }

    public abstract String createDataBase();
}
