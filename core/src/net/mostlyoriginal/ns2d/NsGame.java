package net.mostlyoriginal.ns2d;

import com.badlogic.gdx.Game;

public class NsGame extends Game {

	@Override
	public void create () {
        G.game = this;
        restart();
    }

    public void restart() {
        setScreen(new MainScreen());
    }
}
