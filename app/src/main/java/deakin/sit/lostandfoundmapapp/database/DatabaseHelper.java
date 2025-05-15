package deakin.sit.lostandfoundmapapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LostAndFoundDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "posts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public DatabaseHelper(@Nullable Context context) {
        // super(context, name, factory, version);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_LATITUDE + " REAL,"
                + COLUMN_LONGITUDE + " REAL"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addPost(PostDataModel post){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TYPE, post.getType());
        values.put(COLUMN_NAME, post.getName());
        values.put(COLUMN_PHONE, post.getPhone());
        values.put(COLUMN_DESCRIPTION, post.getDescription());
        values.put(COLUMN_DATE, post.getDate());
        values.put(COLUMN_LOCATION, post.getLocation());
        values.put(COLUMN_LATITUDE, post.getLatitude());
        values.put(COLUMN_LONGITUDE, post.getLongitude());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<PostDataModel> getAllPosts() {
        List<PostDataModel> postList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if(cursor.moveToFirst()){
            do {
                PostDataModel post = new PostDataModel();
                post.setId(cursor.getInt(0));
                for(int i = 1 ; i < cursor.getColumnCount(); i++){
                    String colName = cursor.getColumnName(i);
                    switch (colName) {
                        case COLUMN_TYPE:
                            post.setType(cursor.getString(i));
                            break;
                        case COLUMN_NAME:
                            post.setName(cursor.getString(i));
                            break;
                        case COLUMN_PHONE:
                            post.setPhone(cursor.getString(i));
                            break;
                        case COLUMN_DESCRIPTION:
                            post.setDescription(cursor.getString(i));
                            break;
                        case COLUMN_DATE:
                            post.setDate(cursor.getString(i));
                            break;
                        case COLUMN_LOCATION:
                            post.setLocation(cursor.getString(i));
                            break;
                        case COLUMN_LATITUDE:
                            post.setLatitude(cursor.getDouble(i));
                            break;
                        case COLUMN_LONGITUDE:
                            post.setLongitude(cursor.getDouble(i));
                            break;
                    }
                }
                postList.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return postList;
    }

    public PostDataModel getPost(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
                + " WHERE " + COLUMN_ID + " = " + id, null);

        cursor.moveToFirst();
        PostDataModel post = new PostDataModel();
        post.setId(cursor.getInt(0));
        for(int i = 1 ; i < cursor.getColumnCount(); i++){
            String colName = cursor.getColumnName(i);
            switch (colName) {
                case COLUMN_TYPE:
                    post.setType(cursor.getString(i));
                    break;
                case COLUMN_NAME:
                    post.setName(cursor.getString(i));
                    break;
                case COLUMN_PHONE:
                    post.setPhone(cursor.getString(i));
                    break;
                case COLUMN_DESCRIPTION:
                    post.setDescription(cursor.getString(i));
                    break;
                case COLUMN_DATE:
                    post.setDate(cursor.getString(i));
                    break;
                case COLUMN_LOCATION:
                    post.setLocation(cursor.getString(i));
                    break;
                case COLUMN_LATITUDE:
                    post.setLatitude(cursor.getDouble(i));
                    break;
                case COLUMN_LONGITUDE:
                    post.setLongitude(cursor.getDouble(i));
                    break;
            }
        }

        cursor.close();
        db.close();
        return post;
    }

    public void updatePost(PostDataModel post){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TYPE, post.getType());
        values.put(COLUMN_NAME, post.getName());
        values.put(COLUMN_PHONE, post.getPhone());
        values.put(COLUMN_DESCRIPTION, post.getDescription());
        values.put(COLUMN_DATE, post.getDate());
        values.put(COLUMN_LOCATION, post.getLocation());
        values.put(COLUMN_LATITUDE, post.getLatitude());
        values.put(COLUMN_LONGITUDE, post.getLongitude());

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(post.getId())});
        db.close();
    }

    public void deletePost(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}