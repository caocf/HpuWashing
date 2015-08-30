package com.edaixi.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.edaixi.activity.OpenCityModle;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table OPEN_CITY_MODLE.
*/
public class OpenCityModleDao extends AbstractDao<OpenCityModle, Long> {

    public static final String TABLENAME = "OPEN_CITY_MODLE";

    /**
     * Properties of entity OpenCityModle.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Is_show = new Property(2, Boolean.class, "is_show", false, "IS_SHOW");
        public final static Property Created_at = new Property(3, String.class, "created_at", false, "CREATED_AT");
        public final static Property Updated_at = new Property(4, String.class, "updated_at", false, "UPDATED_AT");
        public final static Property Fuwufanwei = new Property(5, String.class, "fuwufanwei", false, "FUWUFANWEI");
        public final static Property Initials = new Property(6, String.class, "initials", false, "INITIALS");
    };


    public OpenCityModleDao(DaoConfig config) {
        super(config);
    }
    
    public OpenCityModleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'OPEN_CITY_MODLE' (" + //
                "'_id' INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'IS_SHOW' INTEGER," + // 2: is_show
                "'CREATED_AT' TEXT," + // 3: created_at
                "'UPDATED_AT' TEXT," + // 4: updated_at
                "'FUWUFANWEI' TEXT," + // 5: fuwufanwei
                "'INITIALS' TEXT);"); // 6: initials
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'OPEN_CITY_MODLE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, OpenCityModle entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        Boolean is_show = entity.getIs_show();
        if (is_show != null) {
            stmt.bindLong(3, is_show ? 1l: 0l);
        }
 
        String created_at = entity.getCreated_at();
        if (created_at != null) {
            stmt.bindString(4, created_at);
        }
 
        String updated_at = entity.getUpdated_at();
        if (updated_at != null) {
            stmt.bindString(5, updated_at);
        }
 
        String fuwufanwei = entity.getFuwufanwei();
        if (fuwufanwei != null) {
            stmt.bindString(6, fuwufanwei);
        }
 
        String initials = entity.getInitials();
        if (initials != null) {
            stmt.bindString(7, initials);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public OpenCityModle readEntity(Cursor cursor, int offset) {
        OpenCityModle entity = new OpenCityModle( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // is_show
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // created_at
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // updated_at
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // fuwufanwei
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // initials
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, OpenCityModle entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIs_show(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setCreated_at(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUpdated_at(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFuwufanwei(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setInitials(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(OpenCityModle entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(OpenCityModle entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}