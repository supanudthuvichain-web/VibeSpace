package com.vibespace.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.vibespace.app.data.db.dao.PlaybackStateDao;
import com.vibespace.app.data.db.dao.PlaybackStateDao_Impl;
import com.vibespace.app.data.db.dao.PlaylistDao;
import com.vibespace.app.data.db.dao.PlaylistDao_Impl;
import com.vibespace.app.data.db.dao.QueueDao;
import com.vibespace.app.data.db.dao.QueueDao_Impl;
import com.vibespace.app.data.db.dao.RecentlyPlayedDao;
import com.vibespace.app.data.db.dao.RecentlyPlayedDao_Impl;
import com.vibespace.app.data.db.dao.TrackDao;
import com.vibespace.app.data.db.dao.TrackDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class VibeSpaceDatabase_Impl extends VibeSpaceDatabase {
  private volatile TrackDao _trackDao;

  private volatile PlaylistDao _playlistDao;

  private volatile QueueDao _queueDao;

  private volatile PlaybackStateDao _playbackStateDao;

  private volatile RecentlyPlayedDao _recentlyPlayedDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `tracks` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `artist` TEXT NOT NULL, `album` TEXT NOT NULL, `albumId` INTEGER NOT NULL, `duration` INTEGER NOT NULL, `uri` TEXT NOT NULL, `dateAdded` INTEGER NOT NULL, `trackNumber` INTEGER NOT NULL, `year` INTEGER NOT NULL, `isUploaded` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlists` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlist_tracks` (`playlistId` INTEGER NOT NULL, `trackId` INTEGER NOT NULL, `position` INTEGER NOT NULL, PRIMARY KEY(`playlistId`, `trackId`), FOREIGN KEY(`playlistId`) REFERENCES `playlists`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`trackId`) REFERENCES `tracks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_playlist_tracks_playlistId` ON `playlist_tracks` (`playlistId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_playlist_tracks_trackId` ON `playlist_tracks` (`trackId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `queue_tracks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `trackId` INTEGER NOT NULL, `position` INTEGER NOT NULL, `isCurrent` INTEGER NOT NULL, FOREIGN KEY(`trackId`) REFERENCES `tracks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_queue_tracks_trackId` ON `queue_tracks` (`trackId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `playback_state` (`id` INTEGER NOT NULL, `currentTrackId` INTEGER, `positionMs` INTEGER NOT NULL, `shuffleEnabled` INTEGER NOT NULL, `repeatMode` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recently_played` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `trackId` INTEGER NOT NULL, `playedAt` INTEGER NOT NULL, FOREIGN KEY(`trackId`) REFERENCES `tracks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_recently_played_trackId` ON `recently_played` (`trackId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '669327624413c7e6f723133fa48b9c0d')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `tracks`");
        db.execSQL("DROP TABLE IF EXISTS `playlists`");
        db.execSQL("DROP TABLE IF EXISTS `playlist_tracks`");
        db.execSQL("DROP TABLE IF EXISTS `queue_tracks`");
        db.execSQL("DROP TABLE IF EXISTS `playback_state`");
        db.execSQL("DROP TABLE IF EXISTS `recently_played`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTracks = new HashMap<String, TableInfo.Column>(11);
        _columnsTracks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTracks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTracks.put("artist", new TableInfo.Column("artist", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTracks.put("album", new TableInfo.Column("album", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTracks.put("albumId", new TableInfo.Column("albumId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTracks.put("duration", new TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTracks.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTracks.put("dateAdded", new TableInfo.Column("dateAdded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTracks.put("trackNumber", new TableInfo.Column("trackNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTracks.put("year", new TableInfo.Column("year", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTracks.put("isUploaded", new TableInfo.Column("isUploaded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTracks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTracks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTracks = new TableInfo("tracks", _columnsTracks, _foreignKeysTracks, _indicesTracks);
        final TableInfo _existingTracks = TableInfo.read(db, "tracks");
        if (!_infoTracks.equals(_existingTracks)) {
          return new RoomOpenHelper.ValidationResult(false, "tracks(com.vibespace.app.data.db.entity.TrackEntity).\n"
                  + " Expected:\n" + _infoTracks + "\n"
                  + " Found:\n" + _existingTracks);
        }
        final HashMap<String, TableInfo.Column> _columnsPlaylists = new HashMap<String, TableInfo.Column>(4);
        _columnsPlaylists.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylists.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylists.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylists.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaylists = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlaylists = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlaylists = new TableInfo("playlists", _columnsPlaylists, _foreignKeysPlaylists, _indicesPlaylists);
        final TableInfo _existingPlaylists = TableInfo.read(db, "playlists");
        if (!_infoPlaylists.equals(_existingPlaylists)) {
          return new RoomOpenHelper.ValidationResult(false, "playlists(com.vibespace.app.data.db.entity.PlaylistEntity).\n"
                  + " Expected:\n" + _infoPlaylists + "\n"
                  + " Found:\n" + _existingPlaylists);
        }
        final HashMap<String, TableInfo.Column> _columnsPlaylistTracks = new HashMap<String, TableInfo.Column>(3);
        _columnsPlaylistTracks.put("playlistId", new TableInfo.Column("playlistId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("trackId", new TableInfo.Column("trackId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("position", new TableInfo.Column("position", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaylistTracks = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysPlaylistTracks.add(new TableInfo.ForeignKey("playlists", "CASCADE", "NO ACTION", Arrays.asList("playlistId"), Arrays.asList("id")));
        _foreignKeysPlaylistTracks.add(new TableInfo.ForeignKey("tracks", "CASCADE", "NO ACTION", Arrays.asList("trackId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesPlaylistTracks = new HashSet<TableInfo.Index>(2);
        _indicesPlaylistTracks.add(new TableInfo.Index("index_playlist_tracks_playlistId", false, Arrays.asList("playlistId"), Arrays.asList("ASC")));
        _indicesPlaylistTracks.add(new TableInfo.Index("index_playlist_tracks_trackId", false, Arrays.asList("trackId"), Arrays.asList("ASC")));
        final TableInfo _infoPlaylistTracks = new TableInfo("playlist_tracks", _columnsPlaylistTracks, _foreignKeysPlaylistTracks, _indicesPlaylistTracks);
        final TableInfo _existingPlaylistTracks = TableInfo.read(db, "playlist_tracks");
        if (!_infoPlaylistTracks.equals(_existingPlaylistTracks)) {
          return new RoomOpenHelper.ValidationResult(false, "playlist_tracks(com.vibespace.app.data.db.entity.PlaylistTrackEntity).\n"
                  + " Expected:\n" + _infoPlaylistTracks + "\n"
                  + " Found:\n" + _existingPlaylistTracks);
        }
        final HashMap<String, TableInfo.Column> _columnsQueueTracks = new HashMap<String, TableInfo.Column>(4);
        _columnsQueueTracks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQueueTracks.put("trackId", new TableInfo.Column("trackId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQueueTracks.put("position", new TableInfo.Column("position", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQueueTracks.put("isCurrent", new TableInfo.Column("isCurrent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQueueTracks = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysQueueTracks.add(new TableInfo.ForeignKey("tracks", "CASCADE", "NO ACTION", Arrays.asList("trackId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesQueueTracks = new HashSet<TableInfo.Index>(1);
        _indicesQueueTracks.add(new TableInfo.Index("index_queue_tracks_trackId", false, Arrays.asList("trackId"), Arrays.asList("ASC")));
        final TableInfo _infoQueueTracks = new TableInfo("queue_tracks", _columnsQueueTracks, _foreignKeysQueueTracks, _indicesQueueTracks);
        final TableInfo _existingQueueTracks = TableInfo.read(db, "queue_tracks");
        if (!_infoQueueTracks.equals(_existingQueueTracks)) {
          return new RoomOpenHelper.ValidationResult(false, "queue_tracks(com.vibespace.app.data.db.entity.QueueTrackEntity).\n"
                  + " Expected:\n" + _infoQueueTracks + "\n"
                  + " Found:\n" + _existingQueueTracks);
        }
        final HashMap<String, TableInfo.Column> _columnsPlaybackState = new HashMap<String, TableInfo.Column>(5);
        _columnsPlaybackState.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaybackState.put("currentTrackId", new TableInfo.Column("currentTrackId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaybackState.put("positionMs", new TableInfo.Column("positionMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaybackState.put("shuffleEnabled", new TableInfo.Column("shuffleEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaybackState.put("repeatMode", new TableInfo.Column("repeatMode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaybackState = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlaybackState = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlaybackState = new TableInfo("playback_state", _columnsPlaybackState, _foreignKeysPlaybackState, _indicesPlaybackState);
        final TableInfo _existingPlaybackState = TableInfo.read(db, "playback_state");
        if (!_infoPlaybackState.equals(_existingPlaybackState)) {
          return new RoomOpenHelper.ValidationResult(false, "playback_state(com.vibespace.app.data.db.entity.PlaybackStateEntity).\n"
                  + " Expected:\n" + _infoPlaybackState + "\n"
                  + " Found:\n" + _existingPlaybackState);
        }
        final HashMap<String, TableInfo.Column> _columnsRecentlyPlayed = new HashMap<String, TableInfo.Column>(3);
        _columnsRecentlyPlayed.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecentlyPlayed.put("trackId", new TableInfo.Column("trackId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecentlyPlayed.put("playedAt", new TableInfo.Column("playedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecentlyPlayed = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRecentlyPlayed.add(new TableInfo.ForeignKey("tracks", "CASCADE", "NO ACTION", Arrays.asList("trackId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRecentlyPlayed = new HashSet<TableInfo.Index>(1);
        _indicesRecentlyPlayed.add(new TableInfo.Index("index_recently_played_trackId", false, Arrays.asList("trackId"), Arrays.asList("ASC")));
        final TableInfo _infoRecentlyPlayed = new TableInfo("recently_played", _columnsRecentlyPlayed, _foreignKeysRecentlyPlayed, _indicesRecentlyPlayed);
        final TableInfo _existingRecentlyPlayed = TableInfo.read(db, "recently_played");
        if (!_infoRecentlyPlayed.equals(_existingRecentlyPlayed)) {
          return new RoomOpenHelper.ValidationResult(false, "recently_played(com.vibespace.app.data.db.entity.RecentlyPlayedEntity).\n"
                  + " Expected:\n" + _infoRecentlyPlayed + "\n"
                  + " Found:\n" + _existingRecentlyPlayed);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "669327624413c7e6f723133fa48b9c0d", "20f218d616db0e7d5c20d790060e0e62");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "tracks","playlists","playlist_tracks","queue_tracks","playback_state","recently_played");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `tracks`");
      _db.execSQL("DELETE FROM `playlists`");
      _db.execSQL("DELETE FROM `playlist_tracks`");
      _db.execSQL("DELETE FROM `queue_tracks`");
      _db.execSQL("DELETE FROM `playback_state`");
      _db.execSQL("DELETE FROM `recently_played`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TrackDao.class, TrackDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlaylistDao.class, PlaylistDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QueueDao.class, QueueDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlaybackStateDao.class, PlaybackStateDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecentlyPlayedDao.class, RecentlyPlayedDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TrackDao trackDao() {
    if (_trackDao != null) {
      return _trackDao;
    } else {
      synchronized(this) {
        if(_trackDao == null) {
          _trackDao = new TrackDao_Impl(this);
        }
        return _trackDao;
      }
    }
  }

  @Override
  public PlaylistDao playlistDao() {
    if (_playlistDao != null) {
      return _playlistDao;
    } else {
      synchronized(this) {
        if(_playlistDao == null) {
          _playlistDao = new PlaylistDao_Impl(this);
        }
        return _playlistDao;
      }
    }
  }

  @Override
  public QueueDao queueDao() {
    if (_queueDao != null) {
      return _queueDao;
    } else {
      synchronized(this) {
        if(_queueDao == null) {
          _queueDao = new QueueDao_Impl(this);
        }
        return _queueDao;
      }
    }
  }

  @Override
  public PlaybackStateDao playbackStateDao() {
    if (_playbackStateDao != null) {
      return _playbackStateDao;
    } else {
      synchronized(this) {
        if(_playbackStateDao == null) {
          _playbackStateDao = new PlaybackStateDao_Impl(this);
        }
        return _playbackStateDao;
      }
    }
  }

  @Override
  public RecentlyPlayedDao recentlyPlayedDao() {
    if (_recentlyPlayedDao != null) {
      return _recentlyPlayedDao;
    } else {
      synchronized(this) {
        if(_recentlyPlayedDao == null) {
          _recentlyPlayedDao = new RecentlyPlayedDao_Impl(this);
        }
        return _recentlyPlayedDao;
      }
    }
  }
}
