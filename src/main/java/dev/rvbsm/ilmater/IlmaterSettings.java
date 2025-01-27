package dev.rvbsm.ilmater;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.RuleCategory;
import carpet.api.settings.Validator;

import java.time.DateTimeException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import dev.rvbsm.ilmater.player.PersonalRules;
import dev.rvbsm.ilmater.time.TickDuration;

public final class IlmaterSettings {

    @Rule(categories = {IlmaterExtension.ID, RuleCategory.COMMAND, RuleCategory.EXPERIMENTAL})
    public static String commandPersonalRule = "false";

    @Rule(
        categories = {IlmaterExtension.ID, RuleCategory.SURVIVAL, RuleCategory.CREATIVE, RuleCategory.EXPERIMENTAL},
        options = {"all", "keepInventory", "doMobLoot,naturalRegeneration"},
        strict = false,
        validators = PersonalRulesValidator.class)
    public static String allowedPersonalRules = "all";
    public static Set<GameRules.Key<?>> personalRules = ImmutableSet.copyOf(PersonalRules.SUPPORTED_RULES);

    @Rule(categories = {IlmaterExtension.ID, RuleCategory.FEATURE})
    public static boolean cryingPortals = false;

    @Rule(categories = {IlmaterExtension.ID, RuleCategory.FEATURE}, validators = DimensionDisplayValidator.class)
    public static boolean dimensionDisplay = false;

    @Rule(categories = {IlmaterExtension.ID, RuleCategory.BUGFIX})
    public static Boolean loyalVoid = true;

    @Rule(categories = {IlmaterExtension.ID, RuleCategory.SURVIVAL, RuleCategory.FEATURE})
    public static Boolean raidGambit = false;

    @Rule(
        categories = {IlmaterExtension.ID, RuleCategory.SURVIVAL, RuleCategory.FEATURE},
        options = {"false", "1d6h", "1c10m", "60t", "0"},
        strict = false,
        validators = TimeValidator.class)
    public static String vaultCooldown = "false";
    public static long vaultCooldownTicks = -1;

    private static final class PersonalRulesValidator extends Validator<String> {

        @Override
        public String validate(
            @Nullable ServerCommandSource source,
            CarpetRule<String> changingRule,
            String newValue,
            String userInput
        ) {
            if (newValue.equalsIgnoreCase("all")) {
                personalRules = ImmutableSet.copyOf(PersonalRules.SUPPORTED_RULES);
                return newValue;
            }

            final Set<GameRules.Key<?>> rules = Arrays.stream(newValue.split(","))
                .filter(PersonalRules.SUPPORTED_KEYS::containsKey)
                .map(PersonalRules.SUPPORTED_KEYS::get)
                .collect(ImmutableSet.toImmutableSet());

            if (!rules.isEmpty()) {
                personalRules = rules;
                return newValue;
            }

            return null;
        }
    }

    private static final class DimensionDisplayValidator extends Validator<Boolean> {

        @Override
        public Boolean validate(
            @Nullable ServerCommandSource source,
            CarpetRule<Boolean> changingRule,
            Boolean newValue,
            String userInput
        ) {
            if (newValue && source != null) {
                final PlayerManager playerManager = source.getServer().getPlayerManager();

                playerManager.sendToAll(new PlayerListS2CPacket(
                    EnumSet.of(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME),
                    playerManager.getPlayerList()));
            }

            return newValue;
        }
    }

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
