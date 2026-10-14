/*
 * Copyright 2024 - 2025 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.handler;

import org.traccar.model.Position;
import org.traccar.session.cache.CacheManager;
import org.traccar.storage.Storage;
import org.traccar.storage.StorageException;
import org.traccar.storage.query.Columns;
import org.traccar.storage.query.Condition;
import org.traccar.storage.query.Order;
import org.traccar.storage.query.Request;

import jakarta.inject.Inject;
import java.time.Instant;
import java.util.Date;

public class OutdatedHandler extends BasePositionHandler {

    public static final long GPS_EPOCH = Instant.parse("1980-01-06T00:00:00Z").toEpochMilli();

    private final CacheManager cacheManager;
    private final Storage storage;

    @Inject
    public OutdatedHandler(CacheManager cacheManager, Storage storage) {
        this.cacheManager = cacheManager;
        this.storage = storage;
    }

    @Override
    public void onPosition(Position position, Callback callback) {
        if (position.getOutdated()) {
            Position last = cacheManager.getPosition(position.getDeviceId());
            if (last == null || (last.getLatitude() == 0 && last.getLongitude() == 0)) {
                last = getLastValidPosition(position.getDeviceId());
            }
            if (last != null) {
                position.setFixTime(last.getFixTime());
                position.setValid(last.getValid());
                position.setLatitude(last.getLatitude());
                position.setLongitude(last.getLongitude());
                position.setAltitude(last.getAltitude());
                position.setSpeed(last.getSpeed());
                position.setCourse(last.getCourse());
                position.setAccuracy(last.getAccuracy());
            } else {
                position.setFixTime(new Date(GPS_EPOCH));
            }
            if (position.getDeviceTime() == null) {
                position.setDeviceTime(position.getServerTime());
            }
        }
        callback.processed(false);
    }

    private Position getLastValidPosition(long deviceId) {
        try {
            return storage.getObject(Position.class, new Request(
                    new Columns.All(),
                    new Condition.And(
                            new Condition.Equals("deviceId", deviceId),
                            new Condition.Equals("valid", true)),
                    new Order("fixTime", true, 1)));
        } catch (StorageException e) {
            return null;
        }
    }

}
