/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wallet.crypto.trustapp.db


import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.wallet.crypto.trustapp.App
import com.wallet.crypto.trustapp.TrustConstants
import com.wallet.crypto.trustapp.repository.entity.DbTokenInfo1

/**
 * Main database description.
 */
@Database(
        entities = [DbTokenInfo1::class],
        version = 1,
        exportSchema = false
)
abstract class TrustDb : RoomDatabase() {

    abstract fun tokenInfoDao(): TokenInfoDao

    companion object {
        private var db: TrustDb? = null

        fun getInstance(context: Context = App.context, fileName: String = TrustConstants.DB_NAME): TrustDb {
            if (db == null) {
                db = Room.databaseBuilder(context, TrustDb::class.java, fileName).build()
            }
            return db!!
        }
    }

}
