package com.ripplereach.ripplereach.utilities;

import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.services.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UsernameGenerator {

  private static UserService userService;
  private static final Set<String> cache = new HashSet<>();

  @Autowired
  public UsernameGenerator(UserService userService) {
    UsernameGenerator.userService = userService;
  }

  private static final String[] ADJECTIVES = {
    "quick",
    "lazy",
    "sleepy",
    "happy",
    "sad",
    "angry",
    "funny",
    "brave",
    "calm",
    "eager",
    "fierce",
    "gentle",
    "jolly",
    "kind",
    "lively",
    "mighty",
    "noble",
    "proud",
    "silly",
    "witty",
    "zany",
    "bold",
    "bright",
    "cheerful",
    "clever",
    "daring",
    "elegant",
    "fancy",
    "glamorous",
    "graceful",
    "mysterious",
    "polite",
    "quirky",
    "sassy",
    "shiny",
    "snazzy",
    "sparkly",
    "spunky",
    "sturdy",
    "swift",
    "thrifty",
    "witty",
    "zippy"
  };

  private static final String[] NOUNS = {
    "cat",
    "dog",
    "fish",
    "bird",
    "lion",
    "tiger",
    "bear",
    "wolf",
    "fox",
    "owl",
    "bat",
    "frog",
    "mouse",
    "rabbit",
    "panda",
    "zebra",
    "koala",
    "eagle",
    "dolphin",
    "shark",
    "whale",
    "penguin",
    "horse",
    "unicorn",
    "dragon",
    "phoenix",
    "griffin",
    "sprite",
    "gnome",
    "fairy",
    "elf",
    "wizard",
    "witch",
    "goblin",
    "troll",
    "giant",
    "dwarf",
    "mermaid",
    "centaur",
    "minotaur",
    "pegasus",
    "sphinx",
    "basilisk",
    "chimera"
  };

  private static final Random RANDOM = new Random();
  private static final int BATCH_SIZE = 10;

  public static String generateUsername() {
    try {
      Set<String> baseUsernames = generateBaseUsernames(BATCH_SIZE);
      Set<String> existingUsernames = userService.findExistingUsernames(baseUsernames);
      cache.addAll(existingUsernames);

      for (String baseUsername : baseUsernames) {
        if (!cache.contains(baseUsername)) {
          cache.add(baseUsername);
          return baseUsername;
        }
      }

      return generateUsernameWithSuffix(existingUsernames);
    } catch (RuntimeException ex) {
      log.error("Error while generating usernames!", ex);
      throw new RippleReachException("Error while generating usernames!");
    }
  }

  private static Set<String> generateBaseUsernames(int count) {
    return RANDOM
        .ints(count, 0, ADJECTIVES.length)
        .mapToObj(
            i ->
                StringUtils.capitalize(ADJECTIVES[i])
                    + StringUtils.capitalize(NOUNS[RANDOM.nextInt(NOUNS.length)]))
        .collect(Collectors.toSet());
  }

  private static String generateUsernameWithSuffix(Set<String> existingUsernames) {
    String baseUsername;
    int suffix;
    String username;

    do {
      baseUsername =
          StringUtils.capitalize(ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)])
              + StringUtils.capitalize(NOUNS[RANDOM.nextInt(NOUNS.length)]);
      suffix = 10 + RANDOM.nextInt(990); // 990 is used to include 999 in the range
      username = baseUsername + suffix;
    } while (existingUsernames.contains(username)
        || cache.contains(username)
        || userService.existsByUsername(username));

    cache.add(username);
    return username;
  }

  public static List<String> generateUsernames(int count) {
    count = Math.min(count, 50);
    return RANDOM
        .ints(count, 0, ADJECTIVES.length)
        .parallel()
        .mapToObj(i -> generateUsername())
        .collect(Collectors.toList());
  }
}
