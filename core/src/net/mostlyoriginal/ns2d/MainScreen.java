package net.mostlyoriginal.ns2d;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;

import net.mostlyoriginal.api.utils.builder.WorldConfigurationBuilder;
import net.mostlyoriginal.ns2d.system.active.AfterPhysicsSystem;
import net.mostlyoriginal.ns2d.system.active.AimSystem;
import net.mostlyoriginal.ns2d.system.active.AttachmentSystem;
import net.mostlyoriginal.ns2d.system.active.BuildableSystem;
import net.mostlyoriginal.ns2d.system.active.CameraFocusSystem;
import net.mostlyoriginal.ns2d.system.active.CameraShakeSystem;
import net.mostlyoriginal.ns2d.system.active.CombatSystem;
import net.mostlyoriginal.ns2d.system.active.DirectorSystem;
import net.mostlyoriginal.ns2d.system.active.EntitySpawnerSystem;
import net.mostlyoriginal.ns2d.system.active.GravitySystem;
import net.mostlyoriginal.ns2d.system.active.HarvesterSystem;
import net.mostlyoriginal.ns2d.system.active.HomingSystem;
import net.mostlyoriginal.ns2d.system.active.InbetweenSystem;
import net.mostlyoriginal.ns2d.system.active.MapCollisionSystem;
import net.mostlyoriginal.ns2d.system.active.MouseCursorSystem;
import net.mostlyoriginal.ns2d.system.active.ParticleSystem;
import net.mostlyoriginal.ns2d.system.active.PhysicsSystem;
import net.mostlyoriginal.ns2d.system.active.PlayerControlSystem;
import net.mostlyoriginal.ns2d.system.active.ScriptSystem;
import net.mostlyoriginal.ns2d.system.active.SkulkControlSystem;
import net.mostlyoriginal.ns2d.system.active.TerminalSystem;
import net.mostlyoriginal.ns2d.system.active.WallSensorSystem;
import net.mostlyoriginal.ns2d.system.active.WeaponSystem;
import net.mostlyoriginal.ns2d.system.collide.BulletCollisionSystem;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;
import net.mostlyoriginal.ns2d.system.passive.CollisionSystem;
import net.mostlyoriginal.ns2d.system.passive.FramebufferManager;
import net.mostlyoriginal.ns2d.system.passive.MapSystem;
import net.mostlyoriginal.ns2d.system.render.AnimRenderSystem;
import net.mostlyoriginal.ns2d.system.render.DialogRenderSystem;
import net.mostlyoriginal.ns2d.system.render.LightRenderSystem;
import net.mostlyoriginal.ns2d.system.render.MultipassRenderBatchingSystem;
import net.mostlyoriginal.ns2d.system.render.UIAlertActiveSpawnerSystem;
import net.mostlyoriginal.ns2d.system.render.UIAlertBuildableUnderAttack;
import net.mostlyoriginal.ns2d.system.render.UIAlertTechpointUnderAttack;
import net.mostlyoriginal.ns2d.system.render.UIRenderSystem;
import net.mostlyoriginal.ns2d.system.render.UIStageRenderSystem;
import net.mostlyoriginal.ns2d.system.render.UIStopwatchRenderSytem;

/**
 * @author Daan van Yperen
 */
public final class MainScreen extends ScreenAdapter {
    public MainScreen() {
        G.screen = this;

        WorldConfigurationBuilder builder = new WorldConfigurationBuilder();

        builder.with(new GroupManager());
        builder.with(new TagManager());
        builder.with(new FramebufferManager());

        // Active - Cleanup
        builder.with(new TerminalSystem());
        builder.with(new EntitySpawnerSystem());
        builder.with(new ScriptSystem());

        // Passive System, loader helpers.
        builder.with(new AssetSystem());
        builder.with(new MapSystem());
        builder.with(new CameraSystem());
        builder.with(new CollisionSystem());
        builder.with(new ParticleSystem());

        // Active - Input/Logic
        builder.with(new PlayerControlSystem());
        builder.with(new SkulkControlSystem());
        builder.with(new WeaponSystem());

        // Active - Interactions
        builder.with(new BuildableSystem());
        builder.with(new CombatSystem());
        builder.with(new HarvesterSystem());

        // Active - Physics. Order is important! Alter velocity, then constrain.
        builder.with(new PhysicsSystem());
        builder.with(new GravitySystem());
        builder.with(new HomingSystem());
        builder.with(new InbetweenSystem());
        builder.with(new MapCollisionSystem());
        builder.with(new AfterPhysicsSystem());

        // Active - Fixed movement
        builder.with(new AttachmentSystem());
        builder.with(new MouseCursorSystem());
        builder.with(new AimSystem());

        // Active - Post Movement Calculations.
        builder.with(new WallSensorSystem());

        builder.with(new BulletCollisionSystem());

        // Active - Camera
        builder.with(new CameraFocusSystem());
        builder.with(new CameraShakeSystem());

        // Active - Render
        // G.world.setSystem(new MapRenderSystem());
        // G.world.setSystem(new CostRenderSystem());
        // G.world.setSystem(new HealthRenderSystem());

        MultipassRenderBatchingSystem multipassRenderBatchingSystem = new MultipassRenderBatchingSystem();
        builder.with(multipassRenderBatchingSystem);
        builder.with(new AnimRenderSystem(multipassRenderBatchingSystem));

        // G.world.setSystem(new MapRenderSystemInFront());

        builder.with(new DialogRenderSystem());
        builder.with(new UIRenderSystem());
        builder.with(new UIAlertActiveSpawnerSystem());
        builder.with(new UIAlertBuildableUnderAttack());

        builder.with(new UIAlertTechpointUnderAttack());
        builder.with(new UIStageRenderSystem());
        builder.with(new UIStopwatchRenderSytem());

        builder.with(new LightRenderSystem());

        builder.with(new DirectorSystem());

        G.world = new World(builder.build());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        G.world.setDelta(MathUtils.clamp(delta, 0, 1 / 15f));
        G.world.process();
    }
}
