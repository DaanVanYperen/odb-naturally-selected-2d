package net.mostlyoriginal.ns2d.system.active;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.component.EntitySpawner;
import net.mostlyoriginal.ns2d.system.render.DialogRenderSystem;

/**
 * Tries to make an interesting fight for the player.
 *
 * @author Daan van Yperen
 */
@Wire
public class DirectorSystem extends VoidEntitySystem {

    public static final int WARMUP_SECONDS = 5;
    public static final int STAGE_WARMUP = 15;
    private ComponentMapper<EntitySpawner> sm;

    GroupManager groupManager;
    private ImmutableBag<Entity> ducts;
    private float nextStageCooldown = 0;
    private float stageStartCooldown = 0;

    public int activeStage = -1;
    private Stage[] stages = new Stage[] {

            // stage duration, cooldown, active vents, skulks per wave per vent, wave interval
            new Stage(20, 1, 1, 1, 5f, "skulk", "skulk"),
            new Stage(20, STAGE_WARMUP, 1, 1, 1.0f, "babbler", "babbler"),
            new Stage(20, STAGE_WARMUP, 1, 1, 1.0f, "skulk", "skulk"),
            new Stage(20, STAGE_WARMUP, 1, 1, 9.5f, "gorge", "gorge"),
            new Stage(20, STAGE_WARMUP, 1, 1, 0.8f, "skulk", "gorge"),
            new Stage(20, STAGE_WARMUP, 1, 1, 0.7f, "skulk", "gorge"),
            new Stage(20, STAGE_WARMUP, 2, 1, 0.6f, "skulk", "gorge"),
            new Stage(20, STAGE_WARMUP, 2, 1, 0.5f, "skulk", "gorge"),
            new Stage(20, STAGE_WARMUP, 2, 1, 0.45f, "skulk", "gorge"),
            new Stage(20, STAGE_WARMUP, 2, 1, 0.4f, "skulk", "gorge"),
            new Stage(20, STAGE_WARMUP, 3, 1, 0.35f, "skulk", "gorge"),
            new Stage(20, STAGE_WARMUP, 4, 1, 0.3f, "skulk", "gorge"),
            new Stage(20, STAGE_WARMUP, 4, 1, 0.2f, "skulk", "gorge"),
            new Stage(20, STAGE_WARMUP, 4, 1, 0.1f, "skulk", "gorge"),
    };
    private DialogRenderSystem dialogRenderSystem;

    @Override
    protected void initialize() {
        super.initialize();
        ducts = groupManager.getEntities("duct");
    }

    @Override
    protected void processSystem() {
        if ( stageStartCooldown > 0 )
        {
            stageStartCooldown -= world.delta;
            if ( stageStartCooldown <= 0 )
            {
                dialogRenderSystem.randomSay(DialogRenderSystem.STAGE_ACTION_MESSAGES);
            }
        }

        nextStageCooldown -= world.delta;
        if ( nextStageCooldown <= 0 )
        {
            if ( activeStage >= stages.length-1 )
                return;
            activeStage++;
            Stage active = stages[activeStage];
            stageStartCooldown = active.stageWarmup;
            nextStageCooldown = active.stageWarmup + active.stageDuration;
            activateRandomSpawners(
                    active.activeVents,
                    active.skulksPerWave,
                    active.waveInterval,
                    active.stageWarmup,
                    active.id1,
                    active.id2);

            dialogRenderSystem.randomSay(DialogRenderSystem.STAGE_COOLDOWN_MESSAGES);
        }
    }

    private void activateRandomSpawners(int activeVents, int skulksPerWave, float waveInterval, float stageWarmup, String id1, String id2) {

        // disable all spawners.
        for (int i = 0; ducts.size() > i; i++) {
            final Entity spawner = ducts.get(i);
            if (spawner != null) {
                final EntitySpawner entitySpawner = sm.get(spawner);
                entitySpawner.enabled = false;
            }
        }

        for (int c = 0; c < activeVents; c++) {
            Entity spawner = ducts.get(MathUtils.random(0, ducts.size() - 1));
            if (spawner != null) {
                final EntitySpawner entitySpawner = sm.get(spawner);
                entitySpawner.enabled = true;
                entitySpawner.cooldown = stageWarmup;
                entitySpawner.minCount = skulksPerWave;
                entitySpawner.maxCount = skulksPerWave;
                entitySpawner.minInterval = waveInterval * 0.8f;
                entitySpawner.maxInterval = waveInterval;
                entitySpawner.entityId = id1;
                entitySpawner.entityId2 = id2;
                entitySpawner.entityId2Chance = 30;
            }
        }
    }

    private static final class Stage {
        public int stageDuration;
        private final float stageWarmup;
        public int activeVents;
        public int skulksPerWave;
        public float waveInterval;
        public final String id1;
        public final String id2;

        private Stage(int stageDuration, float stageWarmup, int activeVents, int skulksPerWave, float waveInterval, String id1, String id2) {
            this.stageDuration = stageDuration;
            this.stageWarmup = stageWarmup;
            this.activeVents = activeVents;
            this.skulksPerWave = skulksPerWave;
            this.waveInterval = waveInterval;
            this.id1 = id1;
            this.id2 = id2;
        }
    }

}
