package dev.rvbsm.ilmater;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import carpet.utils.Translations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public final class IlmaterTranslation {

    private static final Logger LOGGER = LoggerFactory.getLogger(IlmaterTranslation.class);
    private static final Gson GSON = new GsonBuilder().setStrictness(Strictness.LENIENT).create();
    private static final String LANG_FORMAT = IlmaterExtension.ASSETS_ROOT + "/lang/%s.json";

    public static Map<String, String> load(String lang) {
        final String langPath = LANG_FORMAT.formatted(lang);
        try (final InputStream input = IlmaterTranslation.class.getResourceAsStream(langPath)) {
            if (input == null) {
                return Collections.emptyMap();
            }

            try (final Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                return GSON.fromJson(reader, new TypeToken<>() {});
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load translations", e);
            return Collections.emptyMap();
        }
    }

    public static MutableText translatable(String type, String key, Object... args) {
        final String translation = Translations.tr("%s.%s.%s".formatted(IlmaterExtension.ID, type, key));
        return Text.literal(translation.formatted(args));
    }
}
