package com.vibespace.app.data.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.vibespace.app.data.db.entity.PlaybackStateEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlaybackStateDao_Impl implements PlaybackStateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlaybackStateEntity> __insertionAdapterOfPlaybackStateEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePosition;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCurrentTrack;

  private final SharedSQLiteStatement __preparedStmtOfUpdateShuffle;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRepeatMode;

  public PlaybackStateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlaybackStateEntity = new EntityInsertionAdapter<PlaybackStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `playback_state` (`id`,`currentTrackId`,`positionMs`,`shuffleEnabled`,`repeatMode`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlaybackStateEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCurrentTrackId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getCurrentTrackId());
        }
        statement.bindLong(3, entity.getPositionMs());
        final int _tmp = entity.getShuffleEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getRepeatMode());
      }
    };
    this.__preparedStmtOfUpdatePosition = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE playback_state SET positionMs = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateCurrentTrack = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE playback_state SET currentTrackId = ?, positionMs = 0 WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateShuffle = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE playback_state SET shuffleEnabled = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateRepeatMode = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE playback_state SET repeatMode = ? WHERE id = 1";
        return _query;
      }
    };
  }

  @Override
  public Object savePlaybackState(final PlaybackStateEntity state,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlaybackStateEntity.insert(state);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePosition(final long positionMs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePosition.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, positionMs);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdatePosition.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCurrentTrack(final long trackId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCurrentTrack.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, trackId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateCurrentTrack.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateShuffle(final boolean enabled, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateShuffle.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateShuffle.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRepeatMode(final int mode, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRepeatMode.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, mode);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateRepeatMode.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getPlaybackState(final Continuation<? super PlaybackStateEntity> $completion) {
    final String _sql = "SELECT * FROM playback_state WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PlaybackStateEntity>() {
      @Override
      @Nullable
      public PlaybackStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCurrentTrackId = CursorUtil.getColumnIndexOrThrow(_cursor, "currentTrackId");
          final int _cursorIndexOfPositionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "positionMs");
          final int _cursorIndexOfShuffleEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "shuffleEnabled");
          final int _cursorIndexOfRepeatMode = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatMode");
          final PlaybackStateEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Long _tmpCurrentTrackId;
            if (_cursor.isNull(_cursorIndexOfCurrentTrackId)) {
              _tmpCurrentTrackId = null;
            } else {
              _tmpCurrentTrackId = _cursor.getLong(_cursorIndexOfCurrentTrackId);
            }
            final long _tmpPositionMs;
            _tmpPositionMs = _cursor.getLong(_cursorIndexOfPositionMs);
            final boolean _tmpShuffleEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfShuffleEnabled);
            _tmpShuffleEnabled = _tmp != 0;
            final int _tmpRepeatMode;
            _tmpRepeatMode = _cursor.getInt(_cursorIndexOfRepeatMode);
            _result = new PlaybackStateEntity(_tmpId,_tmpCurrentTrackId,_tmpPositionMs,_tmpShuffleEnabled,_tmpRepeatMode);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
