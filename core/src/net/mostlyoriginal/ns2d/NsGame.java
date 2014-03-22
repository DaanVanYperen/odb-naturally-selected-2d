package net.mostlyoriginal.ns2d;

import com.badlogic.gdx.Game;

public class NsGame extends Game {

	@Override
	public void create () {

        setScreen(new MainScreen());
	}
}
