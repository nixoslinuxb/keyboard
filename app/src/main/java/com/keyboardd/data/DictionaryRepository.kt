package com.keyboardd.data

import java.util.ArrayDeque
import java.util.PriorityQueue

class DictionaryRepository {

    private class TrieNode {
        val children = HashMap<Char, TrieNode>()
        var isWord: Boolean = false
        var frequency: Int = 0
    }

    private val root = TrieNode()

    init {
        loadCommonWords()
    }

    fun addWord(word: String, frequency: Int) {
        val cleanWord = word.lowercase().trim()
        if (cleanWord.isEmpty()) return
        var curr = root
        for (char in cleanWord) {
            curr = curr.children.getOrPut(char) { TrieNode() }
        }
        curr.isWord = true
        curr.frequency = frequency
    }

    fun getSuggestions(prefix: String, maxResults: Int = 5): List<String> {
        val cleanPrefix = prefix.lowercase().trim()
        if (cleanPrefix.isEmpty()) return emptyList()

        var curr = root
        for (char in cleanPrefix) {
            val next = curr.children[char] ?: return emptyList()
            curr = next
        }

        val topMatches = PriorityQueue<Pair<String, Int>>(compareBy { it.second })
        val queue = ArrayDeque<Pair<TrieNode, String>>()
        queue.add(Pair(curr, cleanPrefix))

        var visitedNodes = 0
        val maxVisitedNodes = 100 // Limit node traversal to protect UI thread performance

        while (queue.isNotEmpty() && visitedNodes < maxVisitedNodes) {
            val (node, currentPath) = queue.removeFirst()
            visitedNodes++

            if (node.isWord) {
                topMatches.add(Pair(currentPath, node.frequency))
                if (topMatches.size > maxResults) {
                    topMatches.poll()
                }
            }

            for ((char, child) in node.children) {
                queue.add(Pair(child, currentPath + char))
            }
        }

        val result = mutableListOf<String>()
        while (topMatches.isNotEmpty()) {
            topMatches.poll()?.let {
                result.add(0, it.first)
            }
        }
        return result
    }

    private fun loadCommonWords() {
        val wordsWithFreq = listOf(
            // English Words
            "the" to 1000, "be" to 990, "to" to 980, "of" to 970, "and" to 960, "a" to 950,
            "in" to 940, "that" to 930, "have" to 920, "i" to 910, "it" to 900, "for" to 890,
            "not" to 880, "on" to 870, "with" to 860, "he" to 850, "as" to 840, "you" to 830,
            "do" to 820, "at" to 810, "this" to 800, "but" to 790, "his" to 780, "by" to 770,
            "from" to 760, "they" to 750, "we" to 740, "say" to 730, "her" to 720, "she" to 710,
            "or" to 700, "an" to 690, "will" to 680, "my" to 670, "one" to 660, "all" to 650,
            "would" to 640, "there" to 630, "their" to 620, "what" to 610, "so" to 600, "up" to 590,
            "out" to 580, "if" to 570, "about" to 560, "who" to 550, "get" to 540, "which" to 530,
            "go" to 520, "me" to 510, "when" to 500, "make" to 490, "can" to 480, "like" to 470,
            "time" to 460, "no" to 450, "just" to 440, "him" to 430, "know" to 420, "take" to 410,
            "people" to 400, "into" to 390, "year" to 380, "your" to 370, "good" to 360, "some" to 350,
            "could" to 340, "them" to 330, "see" to 320, "other" to 310, "than" to 300, "then" to 290,
            "now" to 280, "look" to 270, "only" to 260, "come" to 250, "its" to 240, "over" to 230,
            "think" to 220, "also" to 210, "back" to 200, "after" to 190, "use" to 180, "two" to 170,
            "how" to 160, "our" to 150, "work" to 140, "first" to 130, "well" to 120, "way" to 110,
            "even" to 100, "new" to 99, "want" to 98, "because" to 97, "any" to 96, "these" to 95,
            "give" to 94, "day" to 93, "most" to 92, "us" to 91, "great" to 90, "world" to 89,
            "offline" to 250, "keyboard" to 300, "gboard" to 200, "android" to 220, "smart" to 210,
            "mobile" to 180, "typing" to 190, "smooth" to 170, "modern" to 210, "fast" to 220,
            "privacy" to 230, "secure" to 240, "message" to 190, "hello" to 500, "thanks" to 480,
            "thank" to 490, "welcome" to 300, "please" to 400, "awesome" to 250, "perfect" to 260,
            "morning" to 300, "night" to 280, "today" to 350, "tomorrow" to 320, "yesterday" to 290,
            "friend" to 310, "family" to 300, "phone" to 270, "app" to 350, "application" to 220,
            "device" to 200, "system" to 210, "code" to 180, "design" to 190, "beautiful" to 210,

            // Turkish Words (Türkçe Sözcükler)
            "bir" to 1000, "ve" to 990, "bu" to 980, "da" to 970, "de" to 960, "için" to 950,
            "ne" to 940, "ile" to 930, "o" to 920, "var" to 910, "gün" to 900, "güzel" to 890,
            "merhaba" to 880, "teşekkürler" to 870, "evet" to 860, "hayır" to 850, "tamam" to 840,
            "nasıl" to 830, "iyi" to 820, "oldu" to 810, "gibi" to 800, "dünya" to 790, "türkiye" to 780,
            "klavye" to 770, "çok" to 760, "şimdi" to 750, "sonra" to 740, "insan" to 730, "zaman" to 720,
            "büyük" to 710, "küçük" to 700, "her" to 690, "kadar" to 680, "olan" to 670, "daha" to 660,
            "ben" to 650, "sen" to 640, "biz" to 630, "siz" to 620, "onlar" to 610, "diye" to 600,
            "yok" to 590, "önce" to 580, "kendi" to 570, "böyle" to 560, "sadece" to 550, "aşağı" to 540,
            "yukarı" to 530, "yarın" to 520, "bugün" to 510, "akşam" to 500, "sabah" to 490, "saat" to 480,
            "dakika" to 470, "ses" to 460, "mesaj" to 450, "arkadaş" to 440, "ailem" to 430, "telefon" to 420,
            "uygulama" to 410, "cihaz" to 400, "sistem" to 390, "kod" to 380, "tasarım" to 370, "harika" to 360,
            "mükemmel" to 350, "kolay" to 340, "hızlı" to 330, "güvenli" to 320, "gizli" to 310, "çevrimdışı" to 300
        )

        for ((word, freq) in wordsWithFreq) {
            addWord(word, freq)
        }
    }
}
