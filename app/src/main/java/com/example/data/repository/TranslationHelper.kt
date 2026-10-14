package com.example.data.repository

import com.example.data.model.SubtitleLine
import com.example.data.model.VocabularyItem
import java.util.regex.Pattern

object TranslationHelper {

    fun extractYouTubeVideoId(input: String): String? {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return null

        // If it's already a clean 11-char ID
        if (trimmed.matches(Regex("^[a-zA-Z0-9_-]{11}$"))) {
            return trimmed
        }

        val patterns = listOf(
            Pattern.compile("(?:v=|/v/|youtu\\.be/|/embed/|/shorts/|/watch\\?v=)([^#&?\\n]+)"),
            Pattern.compile("youtu\\.be/([^#&?\\n]+)"),
            Pattern.compile("youtube\\.com/watch\\?.*v=([^#&?\\n]+)")
        )

        for (pattern in patterns) {
            val matcher = pattern.matcher(trimmed)
            if (matcher.find()) {
                val candidate = matcher.group(1)
                if (candidate != null && candidate.length >= 11) {
                    return candidate.substring(0, 11)
                }
            }
        }
        return null
    }

    // Built-in intelligent English-Persian dictionary for offline/instant YouTube translation
    private val dictionary: Map<String, String> = mapOf(
        "hello" to "سلام",
        "welcome" to "خوش آمدید",
        "today" to "امروز",
        "video" to "ویدیو",
        "channel" to "کانال",
        "subscribe" to "عضو شدن در کانال (سابسکرایب)",
        "like" to "پسندیدن (لایک)",
        "share" to "به اشتراک گذاشتن",
        "comment" to "دیدگاه (کامنت)",
        "artificial intelligence" to "هوش مصنوعی",
        "machine learning" to "یادگیری ماشین",
        "deep learning" to "یادگیری عمیق",
        "neural network" to "شبکه عصبی",
        "technology" to "فناوری و تکنولوژی",
        "computer" to "رایانه",
        "science" to "علم و دانش",
        "world" to "جهان",
        "future" to "آینده",
        "success" to "موفقیت",
        "habit" to "عادت",
        "habits" to "عادت‌ها",
        "system" to "سیستم",
        "goal" to "هدف",
        "goals" to "اهداف",
        "life" to "زندگی",
        "time" to "زمان",
        "people" to "مردم",
        "story" to "داستان",
        "important" to "مهم",
        "learn" to "یاد گرفتن",
        "learning" to "یادگیری",
        "english" to "انگلیسی",
        "persian" to "فارسی",
        "speak" to "صحبت کردن",
        "listen" to "گوش دادن",
        "watch" to "تماشا کردن",
        "focus" to "تمرکز",
        "mind" to "ذهن",
        "brain" to "مغز",
        "change" to "تغییر",
        "step" to "گام / مرحله",
        "rules" to "قوانین",
        "rule" to "قانون",
        "secret" to "راز",
        "start" to "شروع کردن",
        "finish" to "به پایان رساندن",
        "question" to "پرسش / سوال",
        "answer" to "پاسخ",
        "practice" to "تمرین کردن",
        "vocabulary" to "واژگان",
        "pronunciation" to "تلفظ",
        "grammar" to "گرامر و دستور زبان",
        "simple" to "ساده",
        "easy" to "آسان",
        "difficult" to "دشوار / سخت",
        "interesting" to "جالب",
        "power" to "قدرت",
        "powerful" to "قدرتمند",
        "never" to "هرگز",
        "always" to "همیشه",
        "sometimes" to "گاهی اوقات",
        "remember" to "به خاطر سپردن",
        "forget" to "فراموش کردن",
        "first" to "نخست / اول",
        "second" to "دوم",
        "third" to "سوم",
        "finally" to "در نهایت",
        "because" to "زیرا / چون",
        "although" to "اگرچه",
        "however" to "با این وجود",
        "therefore" to "بنابراین",
        "in addition" to "علاوه بر این",
        "for example" to "برای نمونه / مثلاً",
        "in other words" to "به عبارت دیگر",
        "look forward" to "چشم‌انتظار بودن",
        "connect the dots" to "پیوند دادن تجربه‌ها",
        "piece of cake" to "مثل آب خوردن ساده",
        "bite the bullet" to "دندان روی جگر گذاشتن و با سختی روبرو شدن",
        "break a leg" to "موفق باشی (در اصطلاح نمایش)",
        "call it a day" to "کار را برای امروز تعطیل کردن",
        "hit the sack" to "به رختخواب رفتن و خوابیدن",
        "under the weather" to "کمی ناخوش‌احوال و کسل",
        "stay hungry" to "همواره مشتاق و حریص به پیشرفت بمانید",
        "stay foolish" to "همواره نادان و کنجکاو در پی آموختن بمانید",
        "don't waste your time" to "زمان ارزشمند خود را هدر ندهید",
        "trust the process" to "به فرآیند رشد و یادگیری اعتماد کنید"
    )

    fun translateEnglishSentenceOffline(text: String): String {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return ""

        // Check full phrase match first
        val lower = trimmed.lowercase().removeSuffix(".").removeSuffix("?").removeSuffix("!")
        dictionary[lower]?.let { return it }

        // Tokenize and build semantic Persian translation
        val words = trimmed.split(Regex("\\s+"))
        val translatedParts = mutableListOf<String>()

        var i = 0
        while (i < words.size) {
            val w1 = words[i].lowercase().replace(Regex("[^a-zA-Z]"), "")
            // Check two-word idioms
            if (i + 1 < words.size) {
                val w2 = words[i + 1].lowercase().replace(Regex("[^a-zA-Z]"), "")
                val twoWords = "$w1 $w2"
                if (dictionary.containsKey(twoWords)) {
                    translatedParts.add(dictionary[twoWords]!!)
                    i += 2
                    continue
                }
            }

            val translated = dictionary[w1]
            if (translated != null) {
                translatedParts.add(translated)
            } else {
                // If it's a known small word, apply natural Persian connectors
                when (w1) {
                    "is", "am", "are" -> translatedParts.add("است")
                    "the", "a", "an" -> {} // omit in Persian
                    "and" -> translatedParts.add("و")
                    "or" -> translatedParts.add("یا")
                    "in", "at", "on" -> translatedParts.add("در")
                    "to" -> translatedParts.add("به")
                    "of" -> translatedParts.add("از")
                    "for" -> translatedParts.add("برای")
                    "with" -> translatedParts.add("با")
                    "you" -> translatedParts.add("شما")
                    "we" -> translatedParts.add("ما")
                    "i" -> translatedParts.add("من")
                    "they" -> translatedParts.add("آن‌ها")
                    "this" -> translatedParts.add("این")
                    "that" -> translatedParts.add("آن")
                    "can" -> translatedParts.add("می‌تواند")
                    "will" -> translatedParts.add("خواهد")
                    else -> translatedParts.add(words[i])
                }
            }
            i++
        }

        return if (translatedParts.isNotEmpty()) {
            translatedParts.joinToString(" ")
        } else {
            "ترجمه: $trimmed"
        }
    }

    fun extractVocabularyOffline(text: String): List<VocabularyItem> {
        val words = text.split(Regex("[^a-zA-Z]+"))
            .map { it.lowercase().trim() }
            .filter { it.length > 3 }
            .distinct()

        val found = mutableListOf<VocabularyItem>()
        for (w in words) {
            val meaning = dictionary[w]
            if (meaning != null && meaning != "است" && meaning != "در" && meaning != "برای") {
                found.add(
                    VocabularyItem(
                        word = w.replaceFirstChar { it.uppercase() },
                        phonetic = "/$w/",
                        persianMeaning = meaning,
                        partOfSpeech = "واژه",
                        englishContext = text.take(60) + "...",
                        persianExample = "مورد استفاده در این ویدیو"
                    )
                )
            }
        }
        return found
    }

    fun parseSubtitlesFromRawText(rawText: String, defaultPersian: String? = null): List<SubtitleLine> {
        val lines = rawText.lines().map { it.trim() }.filter { it.isNotEmpty() }
        val result = mutableListOf<SubtitleLine>()

        var currentStart = 0f
        var currentId = 1

        for (line in lines) {
            // Check for [01:23] format
            val timestampMatcher = Pattern.compile("^\\[?(\\d{1,2}):(\\d{2})\\]?\\s*(.*)$").matcher(line)
            if (timestampMatcher.find()) {
                val min = timestampMatcher.group(1)?.toIntOrNull() ?: 0
                val sec = timestampMatcher.group(2)?.toIntOrNull() ?: 0
                val text = timestampMatcher.group(3) ?: ""
                val start = (min * 60 + sec).toFloat()
                val persian = translateEnglishSentenceOffline(text)
                result.add(
                    SubtitleLine(
                        id = currentId++,
                        startSeconds = start,
                        endSeconds = start + 5f,
                        englishText = text,
                        persianText = persian
                    )
                )
            } else {
                val persian = translateEnglishSentenceOffline(line)
                result.add(
                    SubtitleLine(
                        id = currentId++,
                        startSeconds = currentStart,
                        endSeconds = currentStart + 5f,
                        englishText = line,
                        persianText = persian
                    )
                )
                currentStart += 5f
            }
        }
        return result
    }
}
