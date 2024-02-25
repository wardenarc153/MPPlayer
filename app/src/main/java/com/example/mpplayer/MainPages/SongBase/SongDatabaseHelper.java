package com.example.mpplayer.MainPages.SongBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class SongDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "song_db";
    private static final String TABLE_SONG = "songs";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_AUDIO_PATH = "audio_path";  // Добавлен новый столбец

    public SongDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SONG_TABLE = "CREATE TABLE " + TABLE_SONG + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE_PATH + " TEXT,"
                + KEY_AUDIO_PATH + " TEXT" + ")";
        db.execSQL(CREATE_SONG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        onCreate(db);
    }

    public long addSong(SongDataClass song) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, song.getTitle());
        values.put(KEY_IMAGE_PATH, song.getImagePath());

        long result = db.insertWithOnConflict(TABLE_SONG, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

        return result;
    }

    public List<SongDataClass> getAllSongs() {
        List<SongDataClass> songList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SONG, null);

        if (cursor.moveToFirst()) {
            do {
                SongDataClass song = new SongDataClass();
                song.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                song.setImagePath(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));
                song.setAudioPath(cursor.getString(cursor.getColumnIndex(KEY_AUDIO_PATH)));

                songList.add(song);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return songList;
    }


    public long saveSong(String title, Uri imagePath, Uri audioPath) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_IMAGE_PATH, imagePath.toString());
        values.put(KEY_AUDIO_PATH, audioPath.toString());

        long result = db.insert(TABLE_SONG, null, values);
        db.close();

        return result;
    }
}
