package com.ripplereach.ripplereach.utilities;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UsernameGenerator {

    private static final String[] ADJECTIVES = {
            "quick", "lazy", "sleepy", "happy", "sad", "angry", "funny", "brave", "calm",
            "eager", "fierce", "gentle", "jolly", "kind", "lively", "mighty", "noble",
            "proud", "silly", "witty", "zany", "bold", "bright", "cheerful", "clever",
            "daring", "elegant", "fancy", "glamorous", "graceful", "mysterious", "polite",
            "quirky", "sassy", "shiny", "snazzy", "sparkly", "spunky", "sturdy", "swift",
            "thrifty", "witty", "zippy"
    };

    private static final String[] NOUNS = {
            "cat", "dog", "fish", "bird", "lion", "tiger", "bear", "wolf", "fox", "owl",
            "bat", "frog", "mouse", "rabbit", "panda", "zebra", "koala", "eagle", "dolphin",
            "shark", "whale", "penguin", "horse", "unicorn", "dragon", "phoenix", "griffin",
            "sprite", "gnome", "fairy", "elf", "wizard", "witch", "goblin", "troll", "giant",
            "dwarf", "mermaid", "centaur", "minotaur", "pegasus", "sphinx", "basilisk", "chimera"
    };

    private static final Random RANDOM = new Random();

    public static String generateUsername() {
        String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[RANDOM.nextInt(NOUNS.length)];
        int number = RANDOM.nextInt(1000);
        return StringUtils.capitalize(adjective) + StringUtils.capitalize(noun) + number;
    }

    public static List<String> generateUsernames(int count) {
        count = Math.min(count, 50);

        List<String> usernames = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            usernames.add(generateUsername());
        }

        return usernames;
    }
}
