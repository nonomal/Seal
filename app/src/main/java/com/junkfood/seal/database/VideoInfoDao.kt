package com.junkfood.seal.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.junkfood.seal.database.objects.CommandTemplate
import com.junkfood.seal.database.objects.CookieProfile
import com.junkfood.seal.database.objects.DownloadedVideoInfo
import com.junkfood.seal.database.objects.OptionShortcut
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoInfoDao {

    @Insert suspend fun insert(info: DownloadedVideoInfo)

    @Insert suspend fun insertAll(infoList: List<DownloadedVideoInfo>)

    @Query("select * from DownloadedVideoInfo")
    fun getDownloadHistoryFlow(): Flow<List<DownloadedVideoInfo>>

    @Query("select * from DownloadedVideoInfo")
    suspend fun getDownloadHistory(): List<DownloadedVideoInfo>

    @Query("select * from DownloadedVideoInfo where id=:id")
    suspend fun getInfoById(id: Int): DownloadedVideoInfo

    @Query("DELETE FROM DownloadedVideoInfo WHERE id = :id") suspend fun deleteInfoById(id: Int)

    @Query("DELETE FROM DownloadedVideoInfo WHERE videoPath = :path")
    suspend fun deleteInfoByPath(path: String)

    @Query("select * from DownloadedVideoInfo where videoPath = :path")
    suspend fun getInfoByPath(path: String): DownloadedVideoInfo?

    @Transaction
    suspend fun insertInfoDistinctByPath(
        videoInfo: DownloadedVideoInfo,
        path: String = videoInfo.videoPath,
    ) {
        if (getInfoByPath(path) == null) insert(videoInfo)
    }

    @Delete suspend fun deleteInfo(vararg info: DownloadedVideoInfo)

    @Delete @Transaction suspend fun deleteInfoList(idList: List<DownloadedVideoInfo>)

    @Query("SELECT * FROM CommandTemplate") fun getTemplateFlow(): Flow<List<CommandTemplate>>

    @Query("SELECT * FROM CommandTemplate") suspend fun getTemplateList(): List<CommandTemplate>

    @Query("select * from CookieProfile") fun getCookieProfileFlow(): Flow<List<CookieProfile>>

    @Insert suspend fun insertTemplate(template: CommandTemplate): Long

    @Insert @Transaction suspend fun importTemplates(templateList: List<CommandTemplate>)

    @Update suspend fun updateTemplate(template: CommandTemplate)

    @Delete suspend fun deleteTemplate(template: CommandTemplate)

    @Query("SELECT * FROM CommandTemplate where id = :id")
    suspend fun getTemplateById(id: Int): CommandTemplate

    @Query("select * from CookieProfile where id=:id")
    suspend fun getCookieById(id: Int): CookieProfile?

    @Update suspend fun updateCookieProfile(cookieProfile: CookieProfile)

    @Delete suspend fun deleteCookieProfile(cookieProfile: CookieProfile)

    @Insert suspend fun insertCookieProfile(cookieProfile: CookieProfile)

    @Query("delete from CommandTemplate where id=:id") suspend fun deleteTemplateById(id: Int)

    @Delete suspend fun deleteTemplates(templates: List<CommandTemplate>)

    @Query("select * from OptionShortcut") fun getOptionShortcuts(): Flow<List<OptionShortcut>>

    @Query("select * from OptionShortcut") suspend fun getShortcutList(): List<OptionShortcut>

    @Delete suspend fun deleteShortcut(optionShortcut: OptionShortcut)

    @Insert suspend fun insertShortcut(optionShortcut: OptionShortcut): Long

    @Transaction @Insert suspend fun insertAllShortcuts(shortcuts: List<OptionShortcut>)
}
