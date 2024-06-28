package com.ripplereach.ripplereach.utilities;

import com.ripplereach.ripplereach.exceptions.RippleReachException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

@Component
public class AvatarGenerator {

    @Autowired
    private List<String> avatarListBean;

    private static List<String> avatarList;

    private static final Random random = new Random();

    private AvatarGenerator() {}

    @PostConstruct
    private void init() {
        AvatarGenerator.avatarList = avatarListBean;
    }

    public static String generateRandomAvatar() {
        if (avatarList == null || avatarList.isEmpty()) {
            throw new RippleReachException("Avatar list has not been initialized or is empty. Please initialize AvatarConfig first.");
        }

        int index = random.nextInt(avatarList.size());
        return avatarList.get(index);
    }
}
