package dev.rvbsm.ilmater;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.RuleCategory;
import carpet.api.settings.Validator;

import java.time.DateTimeException;
import java.util.EnumSet;

import dev.rvbsm.ilmater.time.TickDuration;

public final class IlmaterSettings {

    @Rule(categories = {IlmaterExtension.ID, RuleCategory.FEATURE})
    public static boolean cryingPortals = false;

    @Rule(categories = {IlmaterExtension.ID, RuleCategory.FEATURE})
    public static boolean dimensionDisplay = false;

    @Rule(categories = {IlmaterExtension.ID, RuleCategory.BUGFIX})
    public static boolean loyalVoid = true;

    @Rule(categories = {IlmaterExtension.ID, RuleCategory.SURVIVAL, RuleCategory.FEATURE})
    public static boolean raidGambit = false;

    @Rule(categories = {IlmaterExtension.ID, RuleCategory.SURVIVAL, RuleCategory.FEATURE})
    public static boolean recoveryItems = false;

    @Rule(
        categories = {IlmaterExtension.ID, RuleCategory.SURVIVAL, RuleCategory.FEATURE},
        options = {"false", "2d", "1c10m", "60t", "0"},
        strict = false,
        validators = TimeValidator.class)
    public static String vaultCooldown = "false";
    public static long vaultCooldownTicks = -1;

    private static final class TimeValidator extends Validator<String> {

        @Override
        public String validate(
            @Nullable ServerCommandSource source,
            CarpetRule<String> changingRule,
            String newValue,
            String userInput
        ) {
            if (newValue.equalsIgnoreCase("false")) {
                vaultCooldownTicks = -1;
                return newValue;
            }

            if (!newValue.isEmpty() && !newValue.startsWith("-")) {
                try {
                    vaultCooldownTicks = TickDuration.fromString(newValue);
                    return newValue;
                } catch (DateTimeException ignored) {
                }
            }

            return null;
        }
    }
}
