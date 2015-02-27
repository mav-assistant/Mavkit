package com.gameminers.mav.firstrun;

import com.gameminers.mav.render.RenderState;
import com.gameminers.mav.screen.Screen;

public class TeachSphinxScreen extends Screen {

	public TeachSphinxScreen() {
		RenderState.setText("\u00A7LGetting ready...");
		new TeachSphinxThread().start();
	}
	
	@Override
	public void onKeyDown(int k, char c, long nanos) {
		
	}

	@Override
	public void onKeyUp(int k, char c, long nanos) {

	}

	@Override
	public void onMouseMove(int x, int y, long nanos) {

	}

	@Override
	public void onMouseDown(int x, int y, int button, long nanos) {

	}

	@Override
	public void onMouseUp(int x, int y, int button, long nanos) {

	}

	@Override
	public void onMouseWheel(int x, int y, int dWheel, long nanos) {

	}

	@Override
	public void doRender() {

	}

	@Override
	public void preRender() {

	}

}
