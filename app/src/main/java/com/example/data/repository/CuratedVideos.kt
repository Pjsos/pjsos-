package com.example.data.repository

import com.example.data.model.SubtitleLine
import com.example.data.model.VideoData
import com.example.data.model.VocabularyItem

object CuratedVideos {
    val sampleVideos: List<VideoData> = listOf(
        VideoData(
            id = "UF8uR6Z6KLc",
            title = "Steve Jobs' Stanford Commencement Address (Stay Hungry, Stay Foolish)",
            author = "Stanford University",
            thumbnailUrl = "https://img.youtube.com/vi/UF8uR6Z6KLc/hqdefault.jpg",
            durationSeconds = 240,
            categoryPersian = "انگیزشی و موفقیت",
            summaryPersian = "استیو جابز در این سخنرانی ماندگار، سه داستان از زندگی خود را روایت می‌کند: پیوند دادن نقطه‌ها، عشق و فقدان، و مرگ. پیام اصلی او این است که زمان انسان محدود است، بنابراین باید با شجاعت به دنبال علاقه قلبی خود رفت.",
            keyPointsPersian = listOf(
                "نمی‌توانید نقطه‌های زندگی را رو به جلو وصل کنید، فقط وقتی به گذشته نگاه می‌کنید به هم وصل می‌شوند.",
                "اخراج شدن از اپل بهترین اتفاقی بود که می‌توانست برای من بیفتد، چون سنگینی موفقیت با سبکی آغاز دوباره جایگزین شد.",
                "مرگ بزرگ‌ترین اختراع زندگی است و باعث می‌شود کارهای غیرضروری را کنار بگذارید.",
                "گرسنه بمانید، نادان (کنجکاو) بمانید."
            ),
            subtitles = listOf(
                SubtitleLine(
                    id = 1,
                    startSeconds = 0f,
                    endSeconds = 4f,
                    englishText = "I am honored to be with you today at your commencement.",
                    persianText = "باعث افتخار من است که امروز در جشن فارغ‌التحصیلی شما حضور دارم."
                ),
                SubtitleLine(
                    id = 2,
                    startSeconds = 5f,
                    endSeconds = 10f,
                    englishText = "From one of the finest universities in the world.",
                    persianText = "در یکی از بهترین دانشگاه‌های جهان."
                ),
                SubtitleLine(
                    id = 3,
                    startSeconds = 11f,
                    endSeconds = 16f,
                    englishText = "Today I want to tell you three stories from my life.",
                    persianText = "امروز می‌خواهم سه داستان از زندگی‌ام برای شما تعریف کنم."
                ),
                SubtitleLine(
                    id = 4,
                    startSeconds = 17f,
                    endSeconds = 22f,
                    englishText = "That's it. No big deal. Just three stories.",
                    persianText = "همین. مسئله مهمی نیست، فقط سه داستان ساده."
                ),
                SubtitleLine(
                    id = 5,
                    startSeconds = 23f,
                    endSeconds = 30f,
                    englishText = "The first story is about connecting the dots.",
                    persianText = "داستان اول درباره متصل کردن نقطه‌ها به یکدیگر است."
                ),
                SubtitleLine(
                    id = 6,
                    startSeconds = 31f,
                    endSeconds = 38f,
                    englishText = "You can't connect the dots looking forward; you can only connect them looking backward.",
                    persianText = "شما نمی‌توانید در حین نگاه به جلو نقطه‌ها را به هم وصل کنید؛ فقط با نگاه به گذشته متصل می‌شوند."
                ),
                SubtitleLine(
                    id = 7,
                    startSeconds = 39f,
                    endSeconds = 47f,
                    englishText = "So you have to trust that the dots will somehow connect in your future.",
                    persianText = "بنابراین باید اعتماد داشته باشید که این نقطه‌ها به نحوی در آینده به هم خواهند پیوست."
                ),
                SubtitleLine(
                    id = 8,
                    startSeconds = 48f,
                    endSeconds = 55f,
                    englishText = "Your time is limited, so don't waste it living someone else's life.",
                    persianText = "زمان شما محدود است، پس آن را با زندگی کردن بر اساس خواسته دیگران هدر ندهید."
                ),
                SubtitleLine(
                    id = 9,
                    startSeconds = 56f,
                    endSeconds = 64f,
                    englishText = "Don't let the noise of others' opinions drown out your own inner voice.",
                    persianText = "اجازه ندهید سر و صدای نظرات دیگران، صدای ندای درونی شما را خاموش کند."
                ),
                SubtitleLine(
                    id = 10,
                    startSeconds = 65f,
                    endSeconds = 72f,
                    englishText = "Stay hungry. Stay foolish. Thank you all very much.",
                    persianText = "گرسنه (مشتاق) بمانید. نادان (کنجکاو) بمانید. بسیار از همه شما سپاسگزارم."
                )
            ),
            vocabulary = listOf(
                VocabularyItem(
                    word = "Commencement",
                    phonetic = "/kəˈmens.mənt/",
                    persianMeaning = "جشن فارغ‌التحصیلی / آغاز رسمی",
                    partOfSpeech = "اسم (Noun)",
                    englishContext = "At your commencement from university.",
                    persianExample = "در جشن فارغ‌التحصیلی شما از دانشگاه."
                ),
                VocabularyItem(
                    word = "Connecting the dots",
                    phonetic = "/kəˈnek.tɪŋ ðə dɑːts/",
                    persianMeaning = "پیوند دادن تجربه‌ها و درک ارتباط پدیده‌ها",
                    partOfSpeech = "اصطلاح (Idiom)",
                    englishContext = "The first story is about connecting the dots.",
                    persianExample = "داستان اول درباره ربط دادن اتفاقات گذشته است."
                ),
                VocabularyItem(
                    word = "Drown out",
                    phonetic = "/draʊn aʊt/",
                    persianMeaning = "خفه کردن صدا / ناشنوا کردن با سر و صدای بیشتر",
                    partOfSpeech = "فعل عبارتی (Phrasal Verb)",
                    englishContext = "Don't let the noise drown out your inner voice.",
                    persianExample = "نگذارید سر و صدا صدای درونتان را خفه کند."
                ),
                VocabularyItem(
                    word = "Stay foolish",
                    phonetic = "/steɪ ˈfuː.lɪʃ/",
                    persianMeaning = "همواره در پی یادگیری و نترسیدن از اشتباه بودن",
                    partOfSpeech = "اصطلاح (Idiom)",
                    englishContext = "Stay hungry, stay foolish.",
                    persianExample = "همیشه مشتاق و در پی کشف ناشناخته‌ها بمانید."
                )
            )
        ),
        VideoData(
            id = "2ePf9rue1Ao",
            title = "How Artificial Intelligence Works in Simple Terms",
            author = "Tech Vision",
            thumbnailUrl = "https://img.youtube.com/vi/2ePf9rue1Ao/hqdefault.jpg",
            durationSeconds = 195,
            categoryPersian = "تکنولوژی و هوش مصنوعی",
            summaryPersian = "این ویدیو نحوه عملکرد شبکه‌های عصبی و یادگیری ماشین را به زبان ساده توضیح می‌دهد. هوش مصنوعی بر اساس الگوبرداری از داده‌های فراوان و یادگیری مداوم کار می‌کند و زندگی روزمره ما را متحول کرده است.",
            keyPointsPersian = listOf(
                "هوش مصنوعی با پردازش میلیاردها الگوی داده و یادگیری از خطاها کار می‌کند.",
                "یادگیری عمیق (Deep Learning) شبیه به عملکرد نورون‌های مغز انسان طراحی شده است.",
                "کاربردهای اصلی شامل بینایی ماشین، پردازش زبان طبیعی و رباتیک است."
            ),
            subtitles = listOf(
                SubtitleLine(
                    id = 1,
                    startSeconds = 0f,
                    endSeconds = 5f,
                    englishText = "Have you ever wondered how computers can recognize your face or translate speech?",
                    persianText = "آیا تا به حال فکر کرده‌اید کامپیوترها چگونه چهره شما را تشخیص می‌دهند یا گفتار را ترجمه می‌کنند؟"
                ),
                SubtitleLine(
                    id = 2,
                    startSeconds = 6f,
                    endSeconds = 12f,
                    englishText = "The secret behind all of this is modern Artificial Intelligence and Machine Learning.",
                    persianText = "راز پشت همه این قابلیت‌ها، هوش مصنوعی مدرن و یادگیری ماشین است."
                ),
                SubtitleLine(
                    id = 3,
                    startSeconds = 13f,
                    endSeconds = 20f,
                    englishText = "Instead of programming every rule manually, we teach machines using massive amounts of data.",
                    persianText = "به جای برنامه‌نویسی دستی تک تک قوانین، ما ماشین‌ها را با حجم عظیمی از داده آموزش می‌دهیم."
                ),
                SubtitleLine(
                    id = 4,
                    startSeconds = 21f,
                    endSeconds = 29f,
                    englishText = "Neural networks detect patterns that are far too complex for human eyes to spot immediately.",
                    persianText = "شبکه‌های عصبی الگوهایی را شناسایی می‌کنند که تشخیص فوری آنها برای چشم انسان بسیار دشوار است."
                ),
                SubtitleLine(
                    id = 5,
                    startSeconds = 30f,
                    endSeconds = 38f,
                    englishText = "With each iteration, the system adjusts its weights and becomes significantly more accurate.",
                    persianText = "با هر بار تکرار، سیستم وزن‌های خود را تنظیم کرده و به شکل چشمگیری دقیق‌تر می‌شود."
                )
            ),
            vocabulary = listOf(
                VocabularyItem(
                    word = "Neural Network",
                    phonetic = "/ˈnjʊə.rəl ˈnet.wɜːk/",
                    persianMeaning = "شبکه عصبی مصنوعی",
                    partOfSpeech = "اصطلاح فنی (Tech)",
                    englishContext = "Neural networks detect patterns in data.",
                    persianExample = "شبکه‌های عصبی الگوها را در داده‌ها کشف می‌کنند."
                ),
                VocabularyItem(
                    word = "Iteration",
                    phonetic = "/ˌɪt.əˈreɪ.ʃən/",
                    persianMeaning = "تکرار / چرخه یادگیری",
                    partOfSpeech = "اسم (Noun)",
                    englishContext = "With each iteration, the accuracy increases.",
                    persianExample = "با هر چرخه تکرار، دقت افزایش می‌یابد."
                ),
                VocabularyItem(
                    word = "Pattern Recognition",
                    phonetic = "/ˈpæt.ən ˌrek.əɡˈnɪʃ.ən/",
                    persianMeaning = "شناسایی و بازشناسی الگو",
                    partOfSpeech = "اصطلاح فنی (Tech)",
                    englishContext = "AI excels at pattern recognition.",
                    persianExample = "هوش مصنوعی در تشخیص الگوها سرآمد است."
                )
            )
        ),
        VideoData(
            id = "d0yGdNEWdn0",
            title = "Top 10 English Idioms for Daily Conversation",
            author = "English Fluent Life",
            thumbnailUrl = "https://img.youtube.com/vi/d0yGdNEWdn0/hqdefault.jpg",
            durationSeconds = 210,
            categoryPersian = "آموزش زبان انگلیسی",
            summaryPersian = "در این ویدیو ده تا از مهم‌ترین و پرکاربردترین اصطلاحات زبان انگلیسی که در فیلم‌ها و گفتگوهای روزمره یوتیوب استفاده می‌شوند همراه با معنی فارسی و کاربرد آنها بررسی می‌شود.",
            keyPointsPersian = listOf(
                "اصطلاحات را نباید کلمه به کلمه ترجمه کرد بلکه باید مفهوم کلی را دریافت.",
                "استفاده صحیح از Idiom ها باعث طبیعی‌تر شنیده شدن صحبت‌های انگلیسی شما می‌شود.",
                "شنیدن در زمینه (Context) بهترین روش برای یادگیری است."
            ),
            subtitles = listOf(
                SubtitleLine(
                    id = 1,
                    startSeconds = 0f,
                    endSeconds = 5f,
                    englishText = "Welcome back everyone! Today we are learning the most practical English idioms.",
                    persianText = "به همه شما خوش‌آمد می‌گویم! امروز پرکاربردترین اصطلاحات انگلیسی را یاد می‌گیریم."
                ),
                SubtitleLine(
                    id = 2,
                    startSeconds = 6f,
                    endSeconds = 12f,
                    englishText = "The first phrase is: 'Piece of cake', which means something is very easy to do.",
                    persianText = "اصطلاح اول: 'Piece of cake' است، به این معنی که کاری مثل آب خوردن آسان است."
                ),
                SubtitleLine(
                    id = 3,
                    startSeconds = 13f,
                    endSeconds = 21f,
                    englishText = "The second one is: 'Bite the bullet', meaning to face a difficult situation with courage.",
                    persianText = "دومی عبارت است از: 'Bite the bullet'، یعنی دل به دریا زدن و با شجاعت روبرو شدن با سختی."
                ),
                SubtitleLine(
                    id = 4,
                    startSeconds = 22f,
                    endSeconds = 29f,
                    englishText = "And third: 'Under the weather', which simply means you are feeling a little sick.",
                    persianText = "و سومین مورد: 'Under the weather'، که یعنی حال و احوال مساعدی ندارید یا کسل و بیمارید."
                )
            ),
            vocabulary = listOf(
                VocabularyItem(
                    word = "Piece of cake",
                    phonetic = "/piːs əv keɪk/",
                    persianMeaning = "مثل آب خوردن، خیلی ساده",
                    partOfSpeech = "اصطلاح (Idiom)",
                    englishContext = "The English exam was a piece of cake.",
                    persianExample = "امتحان زبان مثل آب خوردن آسان بود."
                ),
                VocabularyItem(
                    word = "Bite the bullet",
                    phonetic = "/baɪt ðə ˈbʊl.ɪt/",
                    persianMeaning = "دندان روی جگر گذاشتن / تن به کار سخت دادن",
                    partOfSpeech = "اصطلاح (Idiom)",
                    englishContext = "I decided to bite the bullet and finish the report.",
                    persianExample = "تصمیم گرفتم تن به سختی دهم و گزارش را تمام کنم."
                ),
                VocabularyItem(
                    word = "Under the weather",
                    phonetic = "/ˈʌn.dər ðə ˈweð.ər/",
                    persianMeaning = "کمی ناخوش‌احوال / کسل",
                    partOfSpeech = "اصطلاح (Idiom)",
                    englishContext = "I'm feeling a bit under the weather today.",
                    persianExample = "امروز کمی احساس کسالت و سرماخوردگی دارم."
                )
            )
        ),
        VideoData(
            id = "W1te_s9fIeE",
            title = "The Power of Atomic Habits & Habit Loops",
            author = "Productivity Hub",
            thumbnailUrl = "https://img.youtube.com/vi/W1te_s9fIeE/hqdefault.jpg",
            durationSeconds = 220,
            categoryPersian = "روانشناسی و پیشرفت فردی",
            summaryPersian = "بررسی کتاب پرفروش عادات اتمی و نحوه اثرگذاری تغییرات ۱ درصدی در روز. چگونه تغییرات کوچک در طول زمان نتایج خارق‌العاده‌ای رقم می‌زنند.",
            keyPointsPersian = listOf(
                "بهبود ۱ درصدی در هر روز، شما را در پایان سال ۳۷ برابر بهتر می‌کند.",
                "چرخه عادت از ۴ بخش تشکیل شده: نشانه، اشتیاق، پاسخ و پاداش.",
                "محیط اطراف مهم‌تر از انگیزه زودگذر است."
            ),
            subtitles = listOf(
                SubtitleLine(
                    id = 1,
                    startSeconds = 0f,
                    endSeconds = 6f,
                    englishText = "Small habits don't add up, they compound over weeks and months.",
                    persianText = "عادت‌های کوچک صرفاً جمع نمی‌شوند، بلکه در طول هفته‌ها و ماه‌ها رشد تصاعدی دارند."
                ),
                SubtitleLine(
                    id = 2,
                    startSeconds = 7f,
                    endSeconds = 14f,
                    englishText = "If you can get just 1 percent better each day, you will be 37 times better after one year.",
                    persianText = "اگر بتوانید هر روز تنها ۱ درصد بهتر شوید، پس از یک سال ۳۷ برابر بهتر خواهید شد."
                ),
                SubtitleLine(
                    id = 3,
                    startSeconds = 15f,
                    endSeconds = 23f,
                    englishText = "You do not rise to the level of your goals. You fall to the level of your systems.",
                    persianText = "شما به سطح اهدافتان صعود نمی‌کنید، بلکه به سطح سیستم‌های رفتاری‌تان سقوط می‌کنید."
                )
            ),
            vocabulary = listOf(
                VocabularyItem(
                    word = "Compound",
                    phonetic = "/ˈkɒm.paʊnd/",
                    persianMeaning = "رشد تصاعدی و مرکب پیدا کردن",
                    partOfSpeech = "فعل (Verb)",
                    englishContext = "Habits compound over time.",
                    persianExample = "عادات در طول زمان اثر مرکب پیدا می‌کنند."
                ),
                VocabularyItem(
                    word = "Cue",
                    phonetic = "/kjuː/",
                    persianMeaning = "نشانه / محرک رفتاری",
                    partOfSpeech = "اسم (Noun)",
                    englishContext = "Every habit starts with a clear cue.",
                    persianExample = "هر عادت با یک نشانه مشخص آغاز می‌شود."
                )
            )
        )
    )
}
