package mod.instance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

import bgWork.handler.CanvasPanelHandler;
import mod.IClassPainter;
import mod.IFuncComponent;

public class UseCase extends JPanel implements IFuncComponent, IClassPainter
{
	Vector <String>		texts			= new Vector <>();
	Dimension			defSize			= new Dimension(150, 40);
	int					maxLength		= 20;
	boolean				isSelect		= false;
	int					selectBoxSize	= 5;
	CanvasPanelHandler	cph;

	public UseCase(CanvasPanelHandler cph)
	{
		texts.add("New Use Case");
		reSize();
		this.setVisible(true);
		this.setLocation(0, 0);
		this.setOpaque(true);
		this.cph = cph;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		reSize();
		for (int i = 0; i < texts.size(); i ++)
		{
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, defSize.width, defSize.height);
			g.setColor(Color.BLACK);
			g.drawOval(0, 0, defSize.width - 1, defSize.height - 1);
			if (texts.elementAt(i).length() > maxLength)
			{
				g.drawString(texts.elementAt(i).substring(0, maxLength) + "...",
						3, (int) (0 + (i + 0.8) * defSize.getHeight()));
			}
			else
			{
				g.drawString(texts.elementAt(i), (int) (defSize.width * 0.2),
						(int) (defSize.getHeight()) / 2);
			}
		}
		if (isSelect == true)
		{
			paintSelect(g);
		}
		if (cph.mouseHoverPoint != null && cph.isInside(this, cph.mouseHoverPoint))
		{
			// 利用原專案的 AreaDefine，傳入自己的 Location、Size 與滑鼠絕對座標點
			int side = new Define.AreaDefine().getArea(this.getLocation(), this.getSize(), cph.mouseHoverPoint);
			
			if (side != -1)
			{
				g.setColor(Color.RED);
				int r = 10; // 紅點直徑
				int w = this.getWidth();
				int h = this.getHeight();
				
				// 依據 side 繪製於該 Port 的內部相對座標，並扣除半徑 r/2 讓圓心完美對齊邊線
				if (side == 3) g.fillOval(w / 2 - r / 2, 0 - r / 2, r, r);         // TOP
				else if (side == 2) g.fillOval(w - r / 2, h / 2 - r / 2, r, r);    // RIGHT
				else if (side == 1) g.fillOval(0 - r / 2, h / 2 - r / 2, r, r);    // LEFT
				else if (side == 0) g.fillOval(w / 2 - r / 2, h - r / 2, r, r);    // BOTTOM
			}
		}
	}

	public boolean isSelect()
	{
		return isSelect;
	}

	public void setSelect(boolean isSelect)
	{
		this.isSelect = isSelect;
	}

	@Override
	public void reSize()
	{
		switch (texts.size())
		{
			case 0:
				this.setSize(defSize);
				break;
			default:
				this.setSize(defSize.width, defSize.height);
				break;
		}
	}

	@Override
	public void setText(String text)
	{
		texts.clear();
		texts.add(text);
		this.repaint();
	}

	@Override
	public void paintSelect(Graphics gra)
	{
		gra.setColor(Color.BLACK);
		gra.fillRect(this.getWidth() / 2 - selectBoxSize, 0, selectBoxSize * 2,
				selectBoxSize);
		gra.fillRect(this.getWidth() / 2 - selectBoxSize,
				this.getHeight() - selectBoxSize, selectBoxSize * 2,
				selectBoxSize);
		gra.fillRect(0, this.getHeight() / 2 - selectBoxSize, selectBoxSize,
				selectBoxSize * 2);
		gra.fillRect(this.getWidth() - selectBoxSize,
				this.getHeight() / 2 - selectBoxSize, selectBoxSize,
				selectBoxSize * 2);
	}
}
