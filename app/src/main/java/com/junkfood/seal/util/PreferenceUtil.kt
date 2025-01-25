package com.junkfood.seal.util

import android.os.Build
import androidx.annotation.DeprecatedSinceApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.android.material.color.DynamicColors
import com.junkfood.seal.App
import com.junkfood.seal.App.Companion.applicationScope
import com.junkfood.seal.App.Companion.context
import com.junkfood.seal.App.Companion.isDebugBuild
import com.junkfood.seal.App.Companion.isFDroidBuild
import com.junkfood.seal.R
import com.junkfood.seal.database.objects.CommandTemplate
import com.junkfood.seal.download.Task
import com.junkfood.seal.ui.theme.DEFAULT_SEED_COLOR
import com.junkfood.seal.util.PreferenceUtil.getInt
import com.kyant.monet.PaletteStyle
import com.tencent.mmkv.MMKV
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val CUSTOM_COMMAND = "custom_command"
const val CONCURRENT = "concurrent_fragments"
const val EXTRACT_AUDIO = "extract_audio"
const val THUMBNAIL = "create_thumbnail"
const val YT_DLP_VERSION = "yt-dlp_init"
const val YT_DLP_AUTO_UPDATE = "yt-dlp_update"
const val DEBUG = "debug"
const val CONFIGURE = "configure"
const val DARK_THEME_VALUE = "dark_theme_value"
const val AUDIO_CONVERT = "audio_convert"
const val AUDIO_CONVERSION_FORMAT = "audio_convert_format"
const val AUDIO_FORMAT = "audio_format_preferred"
const val AUDIO_QUALITY = "audio_quality"
const val VIDEO_FORMAT = "video_format"
const val VIDEO_QUALITY = "quality"

const val FORMAT_SORTING = "format_sorting"
const val SORTING_FIELDS = "sorting_fields"

const val WELCOME_DIALOG = "welcome_dialog"
const val VIDEO_DIRECTORY = "download_dir"
const val AUDIO_DIRECTORY = "audio_dir"
const val COMMAND_DIRECTORY = "command_directory"
const val SDCARD_DOWNLOAD = "sdcard_download"
const val SDCARD_URI = "sd_card_uri"
const val SUBDIRECTORY_EXTRACTOR = "sub-directory"
const val SUBDIRECTORY_PLAYLIST_TITLE = "subdirectory_playlist_title"
const val PLAYLIST = "playlist"
private const val LANGUAGE = "language"
const val NOTIFICATION = "notification"
private const val THEME_COLOR = "theme_color"
const val PALETTE_STYLE = "palette_style"
const val SUBTITLE = "subtitle"
const val EMBED_SUBTITLE = "embed_subtitle"
const val KEEP_SUBTITLE_FILES = "keep_subtitle"
const val SUBTITLE_LANGUAGE = "sub_lang"
const val AUTO_SUBTITLE = "auto_subtitle"
const val CONVERT_SUBTITLE = "convert_subtitle"
const val AUTO_TRANSLATED_SUBTITLES = "translated_subs"

const val TEMPLATE_ID = "template_id"
const val MAX_FILE_SIZE = "max_file_size"
const val SPONSORBLOCK = "sponsorblock"
const val SPONSORBLOCK_CATEGORIES = "sponsorblock_categories"
const val ARIA2C = "aria2c"
const val COOKIES = "cookies"
const val USER_AGENT = "user_agent"
const val USER_AGENT_STRING = "user_agent_string"
const val AUTO_UPDATE = "auto_update"
const val UPDATE_CHANNEL = "update_channel"
const val PRIVATE_MODE = "private_mode"
private const val DYNAMIC_COLOR = "dynamic_color"
const val CELLULAR_DOWNLOAD = "cellular_download"
const val RATE_LIMIT = "rate_limit"
const val MAX_RATE = "max_rate"
private const val HIGH_CONTRAST = "high_contrast"
const val DISABLE_PREVIEW = "disable_preview"
const val PRIVATE_DIRECTORY = "private_directory"
const val CROP_ARTWORK = "crop_artwork"
const val EMBED_THUMBNAIL = "embed_thumbnail"
const val FORMAT_SELECTION = "format_selection"
const val VIDEO_CLIP = "video_clip"
const val SHOW_SPONSOR_MSG = "sponsor_msg_v1"
const val PROXY = "proxy"
const val PROXY_URL = "proxy_url"
const val OUTPUT_TEMPLATE = "output_template"
const val CUSTOM_OUTPUT_TEMPLATE = "custom_output_template"
const val DOWNLOAD_ARCHIVE = "download_archive"
const val EMBED_METADATA = "embed_metadata"
const val RESTRICT_FILENAMES = "restrict_filenames"
const val AV1_HARDWARE_ACCELERATED = "av1_hardware_accelerated"
const val FORCE_IPV4 = "force_ipv4"
const val MERGE_OUTPUT_MKV = "merge_to_mkv"
const val USE_CUSTOM_AUDIO_PRESET = "custom_audio_preset"

const val MERGE_MULTI_AUDIO_STREAM = "multi_audio_stream"

const val DOWNLOAD_TYPE_INITIALIZATION = "download_type_init"
private const val DOWNLOAD_TYPE = "download_type"

const val YT_DLP_UPDATE_CHANNEL = "yt-dlp_update_channel"
const val YT_DLP_UPDATE_TIME = "yt-dlp_last_update"
const val YT_DLP_UPDATE_INTERVAL = "yt-dlp_update_interval"

private const val INTERVAL_DAY = 86_400_000L
private const val INTERVAL_WEEK = 86_400_000L * 7
private const val INTERVAL_MONTH = 86_400_000L * 30

const val DEFAULT_INTERVAL = INTERVAL_WEEK // every week

val UpdateIntervalList =
    mapOf(
        INTERVAL_DAY to R.string.every_day,
        INTERVAL_WEEK to R.string.every_week,
        INTERVAL_MONTH to R.string.every_month,
    )

const val NOT_SPECIFIED = 0
const val DEFAULT = NOT_SPECIFIED
const val SYSTEM_DEFAULT = NOT_SPECIFIED
const val NOT_CONVERT = NOT_SPECIFIED

const val NONE = NOT_SPECIFIED
const val USE_PREVIOUS_SELECTION = 1

enum class DownloadType {
    Audio,
    Video,
    Playlist,
    Command,
}

const val CONVERT_ASS = 1
const val CONVERT_LRC = 2
const val CONVERT_SRT = 3
const val CONVERT_VTT = 4

const val STABLE = 0
const val PRE_RELEASE = 1

const val YT_DLP_STABLE = 0
const val YT_DLP_NIGHTLY = 1

const val OPUS = 1
const val M4A = 2

const val FORMAT_COMPATIBILITY = 1
const val FORMAT_QUALITY = 2

const val CONVERT_MP3 = 0
const val CONVERT_M4A = 1

const val HIGH = 1
const val MEDIUM = 2
const val LOW = 3
const val ULTRA_LOW = 4

const val RES_HIGHEST = 0
const val RES_2160P = 1
const val RES_1440P = 2
const val RES_1080P = 3
const val RES_720P = 4
const val RES_480P = 5
const val RES_360P = 6
const val RES_LOWEST = 7

const val TEMPLATE_EXAMPLE = """--no-mtime -S "ext""""

const val TEMPLATE_SHORTCUTS = "template_shortcuts"

const val TASK_LIST = "task_list"
const val SAVED_LINKS = "saved_links"

val paletteStyles =
    listOf(
        PaletteStyle.TonalSpot,
        PaletteStyle.Spritz,
        PaletteStyle.FruitSalad,
        PaletteStyle.Vibrant,
        PaletteStyle.Monochrome,
    )

const val STYLE_TONAL_SPOT = 0
const val STYLE_SPRITZ = 1
const val STYLE_FRUIT_SALAD = 2
const val STYLE_VIBRANT = 3
const val STYLE_MONOCHROME = 4

private val StringPreferenceDefaults =
    mapOf(
        SPONSORBLOCK_CATEGORIES to "default",
        MAX_RATE to "1000",
        SUBTITLE_LANGUAGE to "en.*,.*-orig",
        OUTPUT_TEMPLATE to DownloadUtil.OUTPUT_TEMPLATE_ID,
        CUSTOM_OUTPUT_TEMPLATE to DownloadUtil.OUTPUT_TEMPLATE_ID,
    )

private val BooleanPreferenceDefaults =
    mapOf(
        FORMAT_SELECTION to true,
        CONFIGURE to true,
        CELLULAR_DOWNLOAD to false,
        YT_DLP_AUTO_UPDATE to true,
        NOTIFICATION to true,
        EMBED_METADATA to true,
        USE_CUSTOM_AUDIO_PRESET to false,
    )

private val IntPreferenceDefaults =
    mapOf(
        TEMPLATE_ID to 0,
        CONCURRENT to 8,
        LANGUAGE to SYSTEM_DEFAULT,
        PALETTE_STYLE to 0,
        DARK_THEME_VALUE to DarkThemePreference.FOLLOW_SYSTEM,
        WELCOME_DIALOG to 1,
        AUDIO_CONVERSION_FORMAT to NOT_SPECIFIED,
        VIDEO_QUALITY to NOT_SPECIFIED,
        VIDEO_FORMAT to FORMAT_QUALITY,
        UPDATE_CHANNEL to STABLE,
        SHOW_SPONSOR_MSG to 0,
        CONVERT_SUBTITLE to NOT_SPECIFIED,
        DOWNLOAD_TYPE_INITIALIZATION to USE_PREVIOUS_SELECTION,
        YT_DLP_UPDATE_CHANNEL to YT_DLP_NIGHTLY,
        DOWNLOAD_TYPE to DownloadType.Video.ordinal,
    )

private val LongPreferenceDefaults = mapOf(YT_DLP_UPDATE_INTERVAL to DEFAULT_INTERVAL)

fun String.getStringDefault() = StringPreferenceDefaults.getOrElse(this) { "" }

object PreferenceUtil {
    private val kv: MMKV = MMKV.defaultMMKV()
    private val json = Json {
        ignoreUnknownKeys = true
        allowStructuredMapKeys = true
    }

    fun String.getInt(default: Int = IntPreferenceDefaults.getOrElse(this) { 0 }): Int =
        kv.decodeInt(this, default)

    fun String.getString(
        default: String = StringPreferenceDefaults.getOrElse(this) { "" }
    ): String = kv.decodeString(this) ?: default

    fun String.getBoolean(
        default: Boolean = BooleanPreferenceDefaults.getOrElse(this) { false }
    ): Boolean = kv.decodeBool(this, default)

    fun String.getLong(default: Long = LongPreferenceDefaults.getOrElse(this) { 0L }) =
        kv.decodeLong(this, default)

    fun String.updateString(newString: String) = kv.encode(this, newString)

    fun String.updateInt(newInt: Int) = kv.encode(this, newInt)

    fun String.updateLong(newLong: Long) = kv.encode(this, newLong)

    fun String.updateBoolean(newValue: Boolean) = kv.encode(this, newValue)

    fun updateValue(key: String, b: Boolean) = key.updateBoolean(b)

    fun encodeInt(key: String, int: Int) = key.updateInt(int)

    fun encodeString(key: String, string: String) = key.updateString(string)

    fun containsKey(key: String) = kv.containsKey(key)

    fun getAudioConvertFormat(): Int = AUDIO_CONVERSION_FORMAT.getInt()

    fun getVideoResolution(): Int = VIDEO_QUALITY.getInt()

    fun getAudioQuality(): Int = AUDIO_QUALITY.getInt()

    fun getVideoFormat(): Int = VIDEO_FORMAT.getInt()

    fun getAudioFormat(): Int = AUDIO_FORMAT.getInt()

    fun getDownloadType(
        usePreviousType: Boolean = DOWNLOAD_TYPE_INITIALIZATION.getInt() == USE_PREVIOUS_SELECTION
    ): DownloadType? {
        return if (usePreviousType) {
            DownloadType.entries.firstOrNull { it.ordinal == DOWNLOAD_TYPE.getInt() }
                ?: DownloadType.Video
        } else {
            null
        }
    }

    fun updateDownloadType(type: DownloadType) = DOWNLOAD_TYPE.updateInt(type.ordinal)

    fun isNetworkAvailableForDownload() =
        CELLULAR_DOWNLOAD.getBoolean() || !App.connectivityManager.isActiveNetworkMetered

    fun isAutoUpdateEnabled(): Boolean {
        return when {
            isFDroidBuild() -> false
            isDebugBuild() -> false
            else -> AUTO_UPDATE.getBoolean()
        }
    }

    @DeprecatedSinceApi(api = 33)
    fun getLocaleFromPreference(): Locale? {
        val languageCode = LANGUAGE.getInt()
        return LocaleLanguageCodeMap.entries.find { it.value == languageCode }?.key
    }

    fun saveLocalePreference(locale: Locale?) {
        if (Build.VERSION.SDK_INT >= 33) {
            // No op
        } else {
            LANGUAGE.updateInt(LocaleLanguageCodeMap[locale] ?: SYSTEM_DEFAULT)
        }
    }

    fun getConcurrentFragments(level: Int = CONCURRENT.getInt()): Float {
        return when (level) {
            1 -> 0f
            8 -> 0.33f
            16 -> 0.66f
            else -> 1f
        }
    }

    fun getSponsorBlockCategories(): String = SPONSORBLOCK_CATEGORIES.getString()

    const val COOKIE_HEADER =
        "# Netscape HTTP Cookie File\n" + "# Auto-generated by Seal built-in WebView\n"

    val templateListStateFlow: StateFlow<List<CommandTemplate>> =
        DatabaseUtil.getTemplateFlow()
            .stateIn(applicationScope, started = SharingStarted.Eagerly, emptyList())

    private val List<CommandTemplate>.selectedTemplate: CommandTemplate?
        get() = find { it.id == TEMPLATE_ID.getInt() }

    fun getTemplate(): CommandTemplate {
        var template: CommandTemplate? = null
        runBlocking {
            for (cnt in 1..5) {
                template = templateListStateFlow.value.selectedTemplate
                if (template != null) return@runBlocking
                delay(100)
            }
        }
        return template ?: throw NoSuchElementException()
    }

    suspend fun initializeTemplateSample() {
        TEMPLATE_ID.updateInt(
            DatabaseUtil.insertTemplate(
                    CommandTemplate(
                        id = 0,
                        name = context.getString(R.string.custom_command_template),
                        template = TEMPLATE_EXAMPLE,
                    )
                )
                .toInt()
        )
    }

    data class AppSettings(
        val darkTheme: DarkThemePreference = DarkThemePreference(),
        val isDynamicColorEnabled: Boolean = false,
        val seedColor: Int = DEFAULT_SEED_COLOR,
        val paletteStyleIndex: Int = 0,
    )

    fun getMaxDownloadRate(): String = MAX_RATE.getString()

    private val mutableAppSettingsStateFlow =
        MutableStateFlow(
            AppSettings(
                DarkThemePreference(
                    darkThemeValue =
                        kv.decodeInt(DARK_THEME_VALUE, DarkThemePreference.FOLLOW_SYSTEM),
                    isHighContrastModeEnabled = kv.decodeBool(HIGH_CONTRAST, false),
                ),
                isDynamicColorEnabled =
                    kv.decodeBool(DYNAMIC_COLOR, DynamicColors.isDynamicColorAvailable()),
                seedColor = kv.decodeInt(THEME_COLOR, DEFAULT_SEED_COLOR),
                paletteStyleIndex = kv.decodeInt(PALETTE_STYLE, 0),
            )
        )
    val AppSettingsStateFlow = mutableAppSettingsStateFlow.asStateFlow()

    fun modifyDarkThemePreference(
        darkThemeValue: Int = AppSettingsStateFlow.value.darkTheme.darkThemeValue,
        isHighContrastModeEnabled: Boolean =
            AppSettingsStateFlow.value.darkTheme.isHighContrastModeEnabled,
    ) {
        applicationScope.launch(Dispatchers.IO) {
            mutableAppSettingsStateFlow.update {
                it.copy(
                    darkTheme =
                        AppSettingsStateFlow.value.darkTheme.copy(
                            darkThemeValue = darkThemeValue,
                            isHighContrastModeEnabled = isHighContrastModeEnabled,
                        )
                )
            }
            kv.encode(DARK_THEME_VALUE, darkThemeValue)
            kv.encode(HIGH_CONTRAST, isHighContrastModeEnabled)
        }
    }

    fun modifyThemeSeedColor(colorArgb: Int, paletteStyleIndex: Int) {
        applicationScope.launch(Dispatchers.IO) {
            mutableAppSettingsStateFlow.update {
                it.copy(seedColor = colorArgb, paletteStyleIndex = paletteStyleIndex)
            }
            kv.encode(THEME_COLOR, colorArgb)
            kv.encode(PALETTE_STYLE, paletteStyleIndex)
        }
    }

    fun switchDynamicColor(
        enabled: Boolean = !mutableAppSettingsStateFlow.value.isDynamicColorEnabled
    ) {
        applicationScope.launch(Dispatchers.IO) {
            mutableAppSettingsStateFlow.update { it.copy(isDynamicColorEnabled = enabled) }
            kv.encode(DYNAMIC_COLOR, enabled)
        }
    }

    fun encodeTaskListBackup(map: Map<Task, Task.State>) =
        runCatching { json.encodeToString<Map<Task, Task.State>>(map) }
            .onSuccess { kv.encode(TASK_LIST, it) }
            .onFailure { it.printStackTrace() }

    fun decodeTaskListBackup(): Map<Task, Task.State> =
        runCatching {
                kv.decodeString(TASK_LIST)?.let { json.decodeFromString<Map<Task, Task.State>>(it) }
            }
            .onFailure { it.printStackTrace() }
            .getOrNull() ?: emptyMap()

    fun getSavedLinks(): Set<String> = kv.decodeStringSet(SAVED_LINKS) ?: emptySet()

    fun updateSavedLinks(links: Set<String>) = kv.encode(SAVED_LINKS, links)

    private const val TAG = "PreferenceUtil"
}

data class DarkThemePreference(
    val darkThemeValue: Int = FOLLOW_SYSTEM,
    val isHighContrastModeEnabled: Boolean = false,
) {
    companion object {
        const val FOLLOW_SYSTEM = 1
        const val ON = 2
        const val OFF = 3
    }

    @Composable
    fun isDarkTheme(): Boolean {
        return if (darkThemeValue == FOLLOW_SYSTEM) isSystemInDarkTheme() else darkThemeValue == ON
    }

    @Composable
    fun getDarkThemeDesc(): String {
        return when (darkThemeValue) {
            FOLLOW_SYSTEM -> stringResource(R.string.follow_system)
            ON -> stringResource(R.string.on)
            else -> stringResource(R.string.off)
        }
    }
}

object PreferenceStrings {
    fun getSubtitleConversionFormat(subtitleFormat: Int = CONVERT_SUBTITLE.getInt()): String =
        when (subtitleFormat) {
            CONVERT_LRC -> context.getString(R.string.convert_to, "lrc")
            CONVERT_ASS -> context.getString(R.string.convert_to, "ass")
            CONVERT_SRT -> context.getString(R.string.convert_to, "srt")
            CONVERT_VTT -> context.getString(R.string.convert_to, "vtt")
            else -> context.getString(R.string.not_convert)
        }

    @Composable
    fun getAudioFormatDesc(audioFormatCode: Int = PreferenceUtil.getAudioFormat()): String =
        when (audioFormatCode) {
            M4A -> "M4A"
            OPUS -> "OPUS"
            else -> stringResource(R.string.not_specified)
        }

    @Composable
    fun getAudioQualityDesc(audioQualityCode: Int = PreferenceUtil.getAudioQuality()): String =
        when (audioQualityCode) {
            NOT_SPECIFIED -> stringResource(R.string.best_quality)
            HIGH -> "192 Kbps"
            MEDIUM -> "128 Kbps"
            LOW -> "64 Kbps"
            ULTRA_LOW -> "32 Kbps"
            else -> stringResource(R.string.lowest_bitrate)
        }

    @Composable
    fun getAudioConvertDesc(audioFormatCode: Int = PreferenceUtil.getAudioConvertFormat()): String {
        return when (audioFormatCode) {
            0 -> stringResource(R.string.convert_to).format("mp3")
            else -> stringResource(R.string.convert_to).format("m4a")
        }
    }

    @Composable
    fun getVideoFormatDescComp(videoFormatCode: Int = PreferenceUtil.getVideoFormat()): String {
        return when (videoFormatCode) {
            FORMAT_COMPATIBILITY -> stringResource(R.string.prefer_compatibility_desc)
            FORMAT_QUALITY -> stringResource(R.string.prefer_quality_desc)
            else -> stringResource(R.string.not_specified)
        }
    }

    @Composable
    fun getVideoResolutionDesc(
        videoQualityCode: Int = PreferenceUtil.getVideoResolution()
    ): String {
        return when (videoQualityCode) {
            1 -> "2160p"
            2 -> "1440p"
            3 -> "1080p"
            4 -> "720p"
            5 -> "480p"
            6 -> "360p"
            7 -> stringResource(R.string.lowest_quality)
            else -> stringResource(R.string.best_quality)
        }
    }

    @Composable
    fun getVideoFormatLabel(videoFormatPreference: Int = PreferenceUtil.getVideoFormat()): String {
        return when (videoFormatPreference) {
            FORMAT_COMPATIBILITY -> stringResource(id = R.string.legacy)
            else -> stringResource(id = R.string.quality)
        }
    }

    @Composable
    fun getUpdateIntervalText(interval: Long): String {
        return stringResource(
            id =
                when (interval) {
                    INTERVAL_DAY -> R.string.every_day
                    INTERVAL_WEEK -> R.string.every_week
                    INTERVAL_MONTH -> R.string.every_month
                    else -> R.string.disabled
                }
        )
    }

    @Composable
    fun getAudioPresetText(preferences: DownloadUtil.DownloadPreferences): String {
        return with(preferences) {
            when {
                formatSorting -> {
                    sortingFields
                }

                !useCustomAudioPreset -> {
                    stringResource(R.string.best_quality)
                }

                convertAudio -> {
                    when (audioConvertFormat) {
                        CONVERT_MP3 -> stringResource(R.string.convert_to, "MP3")
                        else -> stringResource(R.string.convert_to, "M4A")
                    }
                }

                else -> {
                    val preferredFormat =
                        when (audioFormat) {
                            M4A -> stringResource(R.string.prefer_placeholder, "M4A")
                            OPUS -> stringResource(R.string.prefer_placeholder, "OPUS")
                            else -> null
                        }
                    val preferredQuality =
                        when (audioQuality) {
                            NOT_SPECIFIED -> stringResource(R.string.best_quality)
                            HIGH -> "192 Kbps"
                            MEDIUM -> "128 Kbps"
                            LOW -> "64 Kbps"
                            ULTRA_LOW -> "32 Kbps"
                            else -> stringResource(R.string.lowest_bitrate)
                        }
                    listOfNotNull(preferredFormat, preferredQuality).joinToString(separator = ", ")
                }
            }
        }
    }

    @Composable
    fun getVideoPresetText(preferences: DownloadUtil.DownloadPreferences): String {
        return with(preferences) {
            when {
                formatSorting -> {
                    sortingFields
                }

                else -> {
                    val preferredFormat =
                        stringResource(
                            id = R.string.prefer_placeholder,
                            stringResource(
                                id =
                                    if (videoFormat == FORMAT_QUALITY) R.string.quality
                                    else R.string.legacy
                            ),
                        )
                    val preferredResolution = getVideoResolutionDesc(videoResolution)
                    listOf(preferredFormat, preferredResolution).joinToString(separator = ", ")
                }
            }
        }
    }
}
