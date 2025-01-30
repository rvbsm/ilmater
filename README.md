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

| Status     | Gamerule                         | Note                                                       |
|------------|----------------------------------|------------------------------------------------------------|
| ✅          | keepInventory                    | the player, or the attacker                                |
| ✅          | doMobLoot                        |                                                            |
| ✅          | projectilesCanBreakBlocks        | owner of the projectile                                    |
| ✅          | doTileDrops                      |                                                            |
| ✅          | doEntityDrops                    |                                                            |
| ✅          | naturalRegeneration              |                                                            |
| ✅          | disableRaids                     | the raid will be invalidated if all players disabled raids |
| ✅          | doInsomnia                       |                                                            |
| ✅          | doImmediateRespawn               |                                                            |
| ✅          | playersNetherPortalDefaultDelay  |                                                            |
| ✅          | playersNetherPortalCreativeDelay |                                                            |
| ✅          | drowningDamage                   |                                                            |
| ✅          | fallDamage                       |                                                            |
| ✅          | fireDamage                       |                                                            |
| ✅          | freezeDamage                     |                                                            |
| not tested | doPatrolSpawning                 |                                                            |
| not tested | doTraderSpawning                 |                                                            |
| ✅          | doWardenSpawning                 |                                                            |
| ✅          | forgiveDeadPlayers               |                                                            |
| ✅          | enderPearlsVanishOnDeath         |                                                            |
| ❌          | doFireTick                       | should not be available as personal                        |
| ❌          | mobGriefing                      | should not be available as personal                        |
| ❌          | doMobSpawning                    | don't wanna                                                |
| ❌          | commandBlockOutput               | should not be available as personal                        |
| ❌          | doDaylightCycle                  | no? i mean, send different daytime?                        |
| ❌          | logAdminCommands                 | should not be available as personal                        |
| ❌          | showDeathMessages                | should not be available as personal                        |
| ❌          | randomTickSpeed                  | should not be available as personal                        |
| ❌          | sendCommandFeedback              | should not be available as personal                        |
| ❌          | reducedDebugInfo                 | should not be available as personal                        |
| ❌          | spectatorsGenerateChunks         | don't wanna                                                |
| ❌          | spawnRadius                      | should not be available as personal                        |
| ❌          | disablePlayerMovementCheck       | should not be available as personal                        |
| ❌          | disableElytraMovementCheck       | should not be available as personal                        |
| ❌          | maxEntityCramming                | should not be available as personal                        |
| ❌          | doWeatherCycle                   | same as `doDaylightCycle`                                  |
| ❌          | doLimitedCrafting                | should not be available as personal                        |
| ❌          | maxCommandChainLength            | should not be available as personal                        |
| ❌          | maxCommandForkCount              | should not be available as personal                        |
| ❌          | commandModificationBlockLimit    | should not be available as personal                        |
| ❌          | announceAdvancements             | should not be available as personal                        |
| ❌          | universalAnger                   | should not be available as personal                        |
| ❌          | playersSleepingPercentage        | should not be available as personal                        |
| ❌          | blockExplosionDropDecay          | should not be available as personal                        |
| ❌          | mobExplosionDropDecay            | is not possible?                                           |
| ❌          | tntExplosionDropDecay            | is not possible?                                           |
| ❌          | snowAccumulationHeight           | should not be available as personal                        |
| ❌          | waterSourceConversion            | should not be available as personal                        |
| ❌          | lavaSourceConversion             | should not be available as personal                        |
| ❌          | globalSoundEvents                | should not be available as personal                        |
| ❌          | doVinesSpread                    | should not be available as personal                        |
| ❌          | minecartMaxSpeed                 | don't wanna / experimental                                 |
| ❌          | spawnChunkRadius                 | should not be available as personal                        |

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
