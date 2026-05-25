package mod.instance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

import bgWork.handler.CanvasPanelHandler;
import mod.IClassPainter;
import mod.IFuncComponent;

public class BasicClass extends JPanel implements IFuncComponent, IClassPainter
{
	Vector <String>		texts			= new Vector <>();
	Dimension			defSize			= new Dimension(150, 25);
	int					maxLength		= 20;
	int					textShiftX		= 5;
	boolean				isSelect		= false;
	int					selectBoxSize	= 5;
	CanvasPanelHandler	cph;

	public BasicClass(CanvasPanelHandler cph)
	{
		texts.add("New Class");
		texts.add("<empty>");
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
			g.fillRect(0, (int) (0 + i * defSize.getHeight()),
					(int) defSize.getWidth() - 1, (int) defSize.height - 1);
			g.setColor(Color.BLACK);
			g.drawRect(0, (int) (0 + i * defSize.getHeight()),
					(int) defSize.getWidth() - 1, (int) defSize.height - 1);
			if (texts.elementAt(i).length() > maxLength)
			{
				g.drawString(texts.elementAt(i).substring(0, maxLength) + "...",
						textShiftX,
						(int) (0 + (i + 0.8) * defSize.getHeight()));
			}
			else
			{
				g.drawString(texts.elementAt(i), textShiftX,
						(int) (0 + (i + 0.8) * defSize.getHeight()));
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
			
			// int  side = 3;
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

	@Override
	public void reSize()
	{
		switch (texts.size())
		{
			case 0:
				this.setSize(defSize);
				break;
			default:
				this.setSize(defSize.width, defSize.height * texts.size());
				break;
		}
	}

	@Override
	public void setText(String text)
	{
		texts.clear();
		texts.add(text);
		texts.add("<empty>");
		this.repaint();
	}

	public void addText(String text)
	{
		texts.add(text);
		this.repaint();
	}

	public void removeText(int index)
	{
		if (index < texts.size() && index >= 0)
		{
			texts.remove(index);
			this.repaint();
		}
	}

	public boolean isSelect()
	{
		return isSelect;
	}

	public void setSelect(boolean isSelect)
	{
		System.out.println(isSelect);
		this.isSelect = isSelect;
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

	public void setCustomSize(Dimension size)
	{
		this.defSize = size;
		reSize(); // 呼叫原本既有的 reSize 來同步更新 Swing 的 setSize
	}
}
