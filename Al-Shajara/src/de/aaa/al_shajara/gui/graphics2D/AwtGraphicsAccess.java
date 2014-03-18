package de.aaa.al_shajara.gui.graphics2D;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Paint;

public class AwtGraphicsAccess implements GraphicsAccess {

	private Graphics g;
	public AwtGraphicsAccess(Graphics g){
		this.g = g;
	}
	
	@Override
	public void drawString(String string, float x, float y) {
		g.drawString(string, (int)x,(int)y);
	}

	@Override
	public void drawRect(int top, int left, int width, int height) {
		g.drawRect(top, left, width, height);
	}

	@Override
	public FontMetrics getFontMetrics() {
		return g.getFontMetrics();
	}

	@Override
	public void setPaint(Paint paint) {
//		g.setColor(paint.)
//		TODO
	}

  @Override
  public void setFont(Font font) {
    g.setFont(font);
  }

}
