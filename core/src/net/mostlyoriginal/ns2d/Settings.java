package net.mostlyoriginal.ns2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * @author Daan van Yperen
 */
public final class Settings {
    public static final String PREF_MUSIC_ON = "musicOn";
    public static final String PREF_SFX_ON = "sfxOn";
    private static final String PREF_PERSONAL_HIGHSCORE = "highestscore";
    private static final String PREF_PERSONAL_HIGHSCORE_STARS = "highestscorestars";
    private Preferences prefs = Gdx.app.getPreferences("NS2DPreferences");

    public float sfxVolume = 0.3f;
    public float musicVolume = 0.2f;
    public boolean sfxOn;
    public boolean musicOn;

    public int personalHighscore = 0;
    public int personalHighscoreStars = 0;

    public Settings() {
        load();
    }

    public void load() {
        sfxOn = prefs.getBoolean(PREF_SFX_ON, true);
        musicOn = prefs.getBoolean(PREF_MUSIC_ON, true);
        personalHighscore = prefs.getInteger(PREF_PERSONAL_HIGHSCORE, 0);
        personalHighscoreStars = prefs.getInteger(PREF_PERSONAL_HIGHSCORE_STARS, 0);

    }

    public void save() {
        prefs.putBoolean(PREF_SFX_ON, sfxOn);
        prefs.putBoolean(PREF_MUSIC_ON, musicOn);
        prefs.putInteger(PREF_PERSONAL_HIGHSCORE, personalHighscore);
        prefs.putInteger(PREF_PERSONAL_HIGHSCORE_STARS, personalHighscoreStars);
        prefs.flush();
    }
}
