package Listener;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import Pack.DragPack;
import bgWork.handler.CanvasPanelHandler;
import bgWork.handler.PanelHandler;

public class CPHActionListener extends HandlerActionListener
		implements MouseMotionListener, MouseListener
{
	Point	from		= new Point(0, 0);
	Object	fromObj;
	Point	to			= new Point(0, 0);
	Object	toObj;
	int		clickShift	= 3;

	public CPHActionListener(PanelHandler h)
	{
		super(h);
		clear();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		from = e.getPoint();
		fromObj = e.getComponent();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		to = e.getPoint();
		toObj = e.getComponent();
		try
		{
			DragPack dp = new DragPack(from, to);
			dp.setFromObj(fromObj);
			dp.setToObj(toObj);
			((CanvasPanelHandler) handler).ActionPerformed(dp);
		}
		catch (Exception excp)
		{
			// TODO: handle exception
		}
		clear();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		handler.ActionPerformed(e);
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		CanvasPanelHandler cph = (CanvasPanelHandler) handler;
		int currMode = cph.getCurrentMode();
		
		// 規格要求：僅在任何建立 line 的模式下觸發 (1, 2, 3, 6)
		if (currMode == 1 || currMode == 2 || currMode == 3 || currMode == 6)
		{
			cph.mouseHoverPoint = arg0.getPoint(); // 直接記錄滑鼠在畫布上的絕對座標
			cph.repaintComp(); // 觸發畫布重繪
		}
		else
		{
			if (cph.mouseHoverPoint != null)
			{
				cph.clearHover();
				cph.repaintComp();
			}
		}
	}

	void clear()
	{
		to = new Point(0, 0);
		toObj = new Object();
		from = new Point(0, 0);
		fromObj = new Object();
	}
}
