package dev.glabay.challenges;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Glabay | Glabay-Studios
 * @project JavaDuel
 * @social Discord: Glabay
 * @since 2026-02-26
 */
@Slf4j
public class ChallengeManager {

    private static final Map<String, Challenge> CHALLENGES = new ConcurrentHashMap<>();
    private static final Random RANDOM = new Random();

    public static void loadChallenges() {
        log.info("Loading challenges from JSON...");
        try (InputStream is = ChallengeManager.class.getClassLoader().getResourceAsStream("challenges.json")) {
            if (is == null) {
                log.error("Could not find challenges.json in resources!");
                return;
            }
            Gson gson = new Gson();
            List<Challenge> challengeList = gson.fromJson(new InputStreamReader(is), new TypeToken<List<Challenge>>(){}.getType());
            
            CHALLENGES.clear();
            for (Challenge challenge : challengeList) {
                CHALLENGES.put(challenge.id(), challenge);
            }
            log.info("Loaded {} challenges.", CHALLENGES.size());
        } catch (Exception e) {
            log.error("Failed to load challenges", e);
        }
    }

    public static Challenge getChallengeById(String id) {
        return CHALLENGES.get(id);
    }

    public static Challenge getRandomChallenge() {
        if (CHALLENGES.isEmpty()) {
            return null;
        }
        List<Challenge> values = List.copyOf(CHALLENGES.values());
        return values.get(RANDOM.nextInt(values.size()));
    }

    public static List<Challenge> getAllChallenges() {
        return List.copyOf(CHALLENGES.values());
    }
}
