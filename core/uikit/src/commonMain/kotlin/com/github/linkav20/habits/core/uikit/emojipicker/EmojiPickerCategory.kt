package com.github.linkav20.habits.core.uikit.emojipicker

import androidx.compose.runtime.Immutable
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.emoji_category_activities
import com.github.linkav20.habits.core.uikit.resources.emoji_category_flags
import com.github.linkav20.habits.core.uikit.resources.emoji_category_food
import com.github.linkav20.habits.core.uikit.resources.emoji_category_habits
import com.github.linkav20.habits.core.uikit.resources.emoji_category_nature
import com.github.linkav20.habits.core.uikit.resources.emoji_category_objects
import com.github.linkav20.habits.core.uikit.resources.emoji_category_people
import com.github.linkav20.habits.core.uikit.resources.emoji_category_places
import com.github.linkav20.habits.core.uikit.resources.emoji_category_smileys
import com.github.linkav20.habits.core.uikit.resources.emoji_category_symbols
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.StringResource
import org.kodein.emoji.Emoji
import org.kodein.emoji.activities.allActivities
import org.kodein.emoji.animals_nature.allAnimalsNature
import org.kodein.emoji.flags.allFlags
import org.kodein.emoji.food_drink.allFoodDrink
import org.kodein.emoji.objects.allObjects
import org.kodein.emoji.people_body.allPeopleBody
import org.kodein.emoji.smileys_emotion.allSmileysEmotion
import org.kodein.emoji.symbols.allSymbols
import org.kodein.emoji.travel_places.allTravelPlaces

@Immutable
internal data class EmojiPickerCategory(
    val title: StringResource,
    val emojis: ImmutableList<String>,
)

internal val emojiPickerCategories: ImmutableList<EmojiPickerCategory> by lazy {
    val groups = listOf(
        Res.string.emoji_category_activities to Emoji.allActivities(),
        Res.string.emoji_category_objects to Emoji.allObjects(),
        Res.string.emoji_category_food to Emoji.allFoodDrink(),
        Res.string.emoji_category_nature to Emoji.allAnimalsNature(),
        Res.string.emoji_category_places to Emoji.allTravelPlaces(),
        Res.string.emoji_category_symbols to Emoji.allSymbols(),
        Res.string.emoji_category_flags to Emoji.allFlags(),
        Res.string.emoji_category_people to Emoji.allPeopleBody(),
        Res.string.emoji_category_smileys to Emoji.allSmileysEmotion(),
    ).map { (title, emojis) ->
        EmojiPickerCategory(title, emojis.map { it.toString() }.distinct().toImmutableList())
    }
    val suggestions = persistentListOf(
        "💧", "🥗", "🥦", "🍎", "🍋", "🍒",
        "💪", "🏋️", "🏃", "👟", "🚶", "👣", "⚡", "🔥",
        "😴", "🌙", "⏰", "☀️", "🧘", "🪷", "🐈",
        "💊", "🩺", "❤️‍🩹", "🦷", "🫁", "🚭", "🫧", "🧽", "🌸", "🩷", "📵",
        "📖", "📚", "🎓", "🧠", "💬", "🌍", "✍️", "📓", "🎯", "🧩",
        "💰", "💵", "💎",
        "🌳", "🌱", "🍀", "🦋", "☁️", "🌊",
        "✨", "💫", "🌈", "🔮", "🪐", "🪄", "🎈", "🚀",
    )
    (listOf(EmojiPickerCategory(Res.string.emoji_category_habits, suggestions)) + groups).toImmutableList()
}
