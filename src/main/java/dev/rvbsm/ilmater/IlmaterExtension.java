package dev.rvbsm.ilmater;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.fabricmc.api.ModInitializer;
import carpet.CarpetExtension;
import carpet.CarpetServer;

import java.util.Map;

public final class IlmaterExtension implements ModInitializer, CarpetExtension {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String ID = "ilmater";
    public static final String ASSETS_ROOT = "/assets/" + ID;

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(this);
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(IlmaterSettings.class);
    }

    @Override
    public String version() {
        return ID;
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return IlmaterTranslation.load(lang);
    }
}
