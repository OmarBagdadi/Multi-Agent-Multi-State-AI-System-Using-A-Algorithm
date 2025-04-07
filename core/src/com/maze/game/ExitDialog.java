/**
 * 
 */
package com.maze.game;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author omarb
 *
 */
public class ExitDialog extends Dialog {

	public ExitDialog(String title, Skin skin) {
		super(title, skin);
		// TODO Auto-generated constructor stub
	}
	
	public ExitDialog(String title, WindowStyle windowStyle) {
		super(title, windowStyle);
		// TODO Auto-generated constructor stub
	}
	
	{
		text("Congratulations the Treasure Hunting Agent has collected all the Treasure :)");
		button("exit", "Exit");
		
	}
	
	@Override
	protected void result(Object object) {
		System.out.println(object);
	}
	
}
