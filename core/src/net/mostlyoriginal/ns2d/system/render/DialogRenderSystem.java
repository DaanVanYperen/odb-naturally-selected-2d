package net.mostlyoriginal.ns2d.system.render;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import net.mostlyoriginal.ns2d.component.*;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class DialogRenderSystem extends VoidEntitySystem {

    private ComponentMapper<Pos> pm;
    private ComponentMapper<Anim> sm;
    private ComponentMapper<Buildable> bm;
    private ComponentMapper<Bounds> om;
    private ComponentMapper<Wallet> wm;

    private Array<String> messages = new Array<String>();
    private String activeMessage = null;
    private float activeMessageCooldown = 0;

    private static final Color HOLO_COLOR = Color.valueOf("73BCC9");
    private static final Color HOLO_COLOR_RED = Color.valueOf("FF7799");

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private TagManager tagManager;

    private SpriteBatch batch = new SpriteBatch();
    private int walletCash;
    public TextureRegion west;
    public TextureRegion middle;
    public TextureRegion east;
    public Pos pos;

    public void say(String queuedMessage) {
        messages.add(queuedMessage);
    }

    public final static String[] STAGE_ACTION_MESSAGES = {
            "Lock and load!",
            "Heh, time to strafe like a skulk!",
            "Where are all my teammates!",
            "I hope that isn't an onos!",
            "Something in the vents!",
            "What's that rumbling!",
    };

    public static final String[] SLOW_KILL_BEAST =
            {
                    "*Splat!* *Splat!*",
                    "*Splat!*",
                    "*Splat!* *Splat!* *Splat!*",
                    "Gooey sounds are the best!",
                    "Dieeeeeeeeeeeeeeeeeeeeeeeee!",
                    "For Mommyyyyyyyyyyyyy!",
                    "Meet painless!",
                    "I found an outlet for my aggression!",
            };

    public static final String[] JETPACK_ROLLING =
            {
                    "I puked in my mouth.",
                    "I'm getting dizzy.",
                    "*Wooooooooosh*",
                    "Whooaaaaaahhoaaahaoah stooooop.",
            };

    public static final String[] JETPACK_USAGE =
            {
                    "*Wheeeeeeeeeeeeeeeeeeeeeeeeeee*",
                    "Fasterrrrrrrrrrrr!",
                    "Up up and awayyyyyyyyyyy.",
                    "I can see my house from here!",
                    "I just got a speeding ticket.",
                    "Flyyy like a biiiiiiird"
            };

    public static final String[] NON_CRITICAL_ALERT_MESSAGES =
            {
                    "Something's gnawing on the merchendise",
                    "Sharp teeth make idle work!",
                    "I hear the nibblies!"
            };


    public final static String[] CRITICAL_ALERT_MESSAGES = {
            "If that techpoint goes down, i'm fired!",
            "Techpoint under attack!",
            "Leave the commander alooooooone!",
            "Techpoint! No!",
            "If I build some of these turrets they can help defend."
    };

    public final static String[] BUILD_MORE_HARVESTERS = {
            "Better build some more extractors!",
            "I'd farm more cash with some extra extractors!",
            "Commander, we need more extractors!",
            "Ho ho ho, extractoring we go!",
            "Extractors equals cash. Cash equals weapons!",
    };


    public final static String[] BUILDING_DESTROYED_MESSAGES = {
            "Crap! Something broke.",
            "Why is nobody welding!",
            "*sigh*",
            "I was nowhere near that!"
    };


    public final static String[] WEAPON_READY_MESSAGES = {
            "Did you hear the beep? Weapon's done!",
            "Delivery!",
            "Spanky!",
    };

    public final static String[] STAGE_COOLDOWN_MESSAGES = {
            "Vents got quiet!",
            "Calm before the storm.",
            "Pfew!",
            "Time for a spaceburger.",
            "This suit sure is tight!",
    };

    @Override
    protected void initialize() {
        super.initialize();
    }

    public void randomSay(final String[] messages) {
        say(messages[MathUtils.random(0, messages.length - 1)]);
    }

    @Override
    protected void processSystem() {


        activeMessageCooldown -= world.delta;
        if (activeMessageCooldown <= 0) {
            activeMessageCooldown = 0.25f;
            if (messages.size > 0) {
                activeMessage = messages.first();
                messages.removeIndex(0);
                activeMessageCooldown = 0.8f + (activeMessage.length() * 0.1f);
            } else activeMessage = null;
        }

        if (activeMessage != null) {
            batch.setProjectionMatrix(cameraSystem.camera.combined);
            batch.begin();
            batch.setColor(1f, 1f, 1f, 1f);

            final Entity player = tagManager.getEntity("player");
            pos = pm.get(player);
            walletCash = wm.has(player) ? wm.get(player).resources : 0;

            float alpha = MathUtils.clamp(activeMessageCooldown, 0, 1);

            west = assetSystem.get("speech-bubble-left").getKeyFrame(0);
            middle = assetSystem.get("speech-bubble-middle").getKeyFrame(0);
            east = assetSystem.get("speech-bubble-right").getKeyFrame(0);

            final int dialogX = (int) pos.x + 8;
            final int dialogY = (int) pos.y + 32;
            final int middleWidth = (int) assetSystem.font.getBounds(activeMessage).width + 1;

            batch.getColor().a = alpha;
            batch.draw(west, dialogX, dialogY);
            batch.draw(middle, dialogX + 16, dialogY, middleWidth, 38);
            batch.draw(east, dialogX + 16 + middleWidth, dialogY, 16, 38);
            assetSystem.font.setColor(Color.BLACK);
            assetSystem.font.getColor().a = alpha;
            assetSystem.font.draw(batch, activeMessage, dialogX + 17, dialogY + 31);
            assetSystem.font.setColor(Color.WHITE);
            assetSystem.font.getColor().a = alpha;
            assetSystem.font.draw(batch, activeMessage, dialogX + 16, dialogY + 32);

            batch.end();
        }
    }
}
