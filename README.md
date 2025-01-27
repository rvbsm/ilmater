# erm, hi

silly carpet extension for a private server

## Features

### `commandPersonalRule`

Enables /personalrule command to set per-player gamerules.

* Type: `string`
* Default: `false`
* Options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
* Categories: `Ilmater`, `command`, `experimental`

#### `allowedPersonalRules`

List of allowed gamerules for players to use.
Use csv, like 'keepInventory,naturalRegeneration' for multiple rules, or 'all' for everything

* Type: `string`
* Default: `all`
* Options: `all`, `keepInventory`, `doMobLoot,naturalRegeneration`, any other supported gamerules
* Categories: `Ilmater`, `survival`, `creative`, `experimental`

<details>
<summary>List of supported personal rules</summary>

| Status                        | Gamerule                         | Note                                                       |
|-------------------------------|----------------------------------|------------------------------------------------------------|
| :white_check_mark:            | keepInventory                    | the player, or the attacker                                |
| :white_check_mark:            | doMobLoot                        |                                                            |
| :white_check_mark:            | projectilesCanBreakBlocks        | owner of the projectile                                    |
| :white_check_mark:            | doTileDrops                      |                                                            |
| :white_check_mark:            | doEntityDrops                    |                                                            |
| :white_check_mark:            | naturalRegeneration              |                                                            |
| :white_check_mark:            | disableRaids                     | the raid will be invalidated if all players disabled raids |
| :white_check_mark:            | doInsomnia                       |                                                            |
| :white_check_mark:            | doImmediateRespawn               |                                                            |
| :white_check_mark:            | playersNetherPortalDefaultDelay  |                                                            |
| :white_check_mark:            | playersNetherPortalCreativeDelay |                                                            |
| :white_check_mark:            | drowningDamage                   |                                                            |
| :white_check_mark:            | fallDamage                       |                                                            |
| :white_check_mark:            | fireDamage                       |                                                            |
| :white_check_mark:            | freezeDamage                     |                                                            |
| :ballot_box_with_check:       | doPatrolSpawning                 |                                                            |
| :ballot_box_with_check:       | doTraderSpawning                 |                                                            |
| :white_check_mark:            | doWardenSpawning                 |                                                            |
| :white_check_mark:            | forgiveDeadPlayers               |                                                            |
| :white_check_mark:            | enderPearlsVanishOnDeath         |                                                            |
| :negative_squared_cross_mark: | doFireTick                       | should not be available as personal                        |
| :negative_squared_cross_mark: | mobGriefing                      | should not be available as personal                        |
| :negative_squared_cross_mark: | doMobSpawning                    | don't wanna                                                |
| :negative_squared_cross_mark: | commandBlockOutput               | should not be available as personal                        |
| :negative_squared_cross_mark: | doDaylightCycle                  | no? i mean, send different daytime?                        |
| :negative_squared_cross_mark: | logAdminCommands                 | should not be available as personal                        |
| :negative_squared_cross_mark: | showDeathMessages                | should not be available as personal                        |
| :negative_squared_cross_mark: | randomTickSpeed                  | should not be available as personal                        |
| :negative_squared_cross_mark: | sendCommandFeedback              | should not be available as personal                        |
| :negative_squared_cross_mark: | reducedDebugInfo                 | should not be available as personal                        |
| :negative_squared_cross_mark: | spectatorsGenerateChunks         | don't wanna                                                |
| :negative_squared_cross_mark: | spawnRadius                      | should not be available as personal                        |
| :negative_squared_cross_mark: | disablePlayerMovementCheck       | should not be available as personal                        |
| :negative_squared_cross_mark: | disableElytraMovementCheck       | should not be available as personal                        |
| :negative_squared_cross_mark: | maxEntityCramming                | should not be available as personal                        |
| :negative_squared_cross_mark: | doWeatherCycle                   | same as `doDaylightCycle`                                  |
| :negative_squared_cross_mark: | doLimitedCrafting                | should not be available as personal                        |
| :negative_squared_cross_mark: | maxCommandChainLength            | should not be available as personal                        |
| :negative_squared_cross_mark: | maxCommandForkCount              | should not be available as personal                        |
| :negative_squared_cross_mark: | commandModificationBlockLimit    | should not be available as personal                        |
| :negative_squared_cross_mark: | announceAdvancements             | should not be available as personal                        |
| :negative_squared_cross_mark: | universalAnger                   | should not be available as personal                        |
| :negative_squared_cross_mark: | playersSleepingPercentage        | should not be available as personal                        |
| :negative_squared_cross_mark: | blockExplosionDropDecay          | should not be available as personal                        |
| :negative_squared_cross_mark: | mobExplosionDropDecay            | is not possible?                                           |
| :negative_squared_cross_mark: | tntExplosionDropDecay            | is not possible?                                           |
| :negative_squared_cross_mark: | snowAccumulationHeight           | should not be available as personal                        |
| :negative_squared_cross_mark: | waterSourceConversion            | should not be available as personal                        |
| :negative_squared_cross_mark: | lavaSourceConversion             | should not be available as personal                        |
| :negative_squared_cross_mark: | globalSoundEvents                | should not be available as personal                        |
| :negative_squared_cross_mark: | doVinesSpread                    | should not be available as personal                        |
| :negative_squared_cross_mark: | minecartMaxSpeed                 | don't wanna / experimental                                 |
| :negative_squared_cross_mark: | spawnChunkRadius                 | should not be available as personal                        |

</details>

### `cryingPortals`

Allows Crying Obsidian to be used in Nether portals frame.

* Type: `boolean`
* Default: `false`
* Categories: `Ilmater`, `feature`

### `dimensionDisplay`

Shows players' current dimension in playerlist.

* Type: `boolean`
* Default: `false`
* Categories: `Ilmater`, `feature`

### `loyalVoid`

Tridents with Loyalty enchantment will not be consumed by the void and will return

* Type: `boolean`
* Default: `true`
* Categories: `Ilmater`, `bugfix`

### `raidGambit`

Using a goat horn applies the Glowing effect to raiders and makes a user their target

* Type: `boolean`
* Default: `false`
* Categories: `Ilmater`, `survival`, `feature`

### `vaultCooldown`

Adds rewards cooldown to vaults in Trial chambers.
Complex time like 2h30m is supported.
1d equals to the realtime day, 1c equals to the daylight cycle (20 minutes)

* Type: `string`
* Default: `false`
* Options: `false`, `1d6h`, `1c10m`, `10m10t`, `100`, `0`, any other cooldown duration
* Categories: `Ilmater`, `survival`, `feature`
