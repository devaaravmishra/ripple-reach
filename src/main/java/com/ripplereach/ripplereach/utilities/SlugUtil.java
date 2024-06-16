package com.ripplereach.ripplereach.utilities;

import com.github.slugify.Slugify;
import org.springframework.stereotype.Component;

@Component
public class SlugUtil {
    public static String createWithUnderscore(String string) {
        final Slugify slugify = Slugify.builder().underscoreSeparator(true).build();
        return slugify.slugify(string);
    }

    public static String create(String string) {
        final Slugify slg = Slugify.builder().build();
        return slg.slugify(string);
    }

    public static String createCaseSensitiveSlug(String string) {
        final Slugify slugify = Slugify.builder().lowerCase(false).build();
        return slugify.slugify(string);
    }
}
