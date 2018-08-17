package com.radhe.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.radhe.data.entity.CryptoCoinEntity;

@Database(entities = {CryptoCoinEntity.class}, version = 1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase{
    static final String DATABASE_NAME = "market_data";
    private static RoomDb INSTANCE;

    public abstract CoinDao coinDao();

    /*private static final Migration MIGRATION_1_to_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE coins " + "ADD COLUMN description TEXT");
        }
    };
*/
    public static RoomDb getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDb.class, DATABASE_NAME)./*addMigrations(MIGRATION_1_to_2).*/build();
        }
        return INSTANCE;
    }
}
