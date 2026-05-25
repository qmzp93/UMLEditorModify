package mod.instance;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import javax.swing.JPanel;

import Define.AreaDefine;
import Pack.DragPack;
import bgWork.handler.CanvasPanelHandler;
import mod.IFuncComponent;
import mod.ILinePainter;

public class DependencyLine extends JPanel implements IFuncComponent, ILinePainter {
    JPanel from;
    int fromSide;
    Point fp = new Point(0, 0);
    JPanel to;
    int toSide;
    Point tp = new Point(0, 0);
    int arrowSize = 8;
    int panelExtendSize = 10;
    boolean isSelect = false;
    int selectBoxSize = 5;
    CanvasPanelHandler cph;

    public DependencyLine(CanvasPanelHandler cph) {
        this.setOpaque(false);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(1, 1));
        this.cph = cph;
    }

    @Override
    public void paintComponent(Graphics g) {
        Point fpPrime;
        Point tpPrime;
        renewConnect();
        fpPrime = new Point(fp.x - this.getLocation().x, fp.y - this.getLocation().y);
        tpPrime = new Point(tp.x - this.getLocation().x, tp.y - this.getLocation().y);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);

        // 功能 1 核心：設定虛線樣式 (Dashed Stroke)
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 6 }, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(fpPrime.x, fpPrime.y, tpPrime.x, tpPrime.y);
        g2d.dispose(); // 釋放 Graphics2D 資源

        // 繪製開口箭頭
        paintArrow(g, tpPrime);

        if (isSelect == true) {
            paintSelect(g);
        }
    }

    @Override
    public void reSize() {
        Dimension size = new Dimension(
                Math.abs(fp.x - tp.x) + panelExtendSize * 2,
                Math.abs(fp.y - tp.y) + panelExtendSize * 2);
        this.setSize(size);
        this.setLocation(Math.min(fp.x, tp.x) - panelExtendSize, Math.min(fp.y, tp.y) - panelExtendSize);
    }

    @Override
    public void paintArrow(Graphics g, Point point) {
        g.setColor(Color.BLACK);
        // 根據 toSide 的方位，繪製兩條斜線形成開口箭頭
        switch (toSide) {
            case 3: // TOP
                g.drawLine(point.x, point.y, point.x - arrowSize, point.y - arrowSize);
                g.drawLine(point.x, point.y, point.x + arrowSize, point.y - arrowSize);
                break;
            case 2: // RIGHT
                g.drawLine(point.x, point.y, point.x + arrowSize, point.y - arrowSize);
                g.drawLine(point.x, point.y, point.x + arrowSize, point.y + arrowSize);
                break;
            case 1: // LEFT
                g.drawLine(point.x, point.y, point.x - arrowSize, point.y - arrowSize);
                g.drawLine(point.x, point.y, point.x - arrowSize, point.y + arrowSize);
                break;
            case 0: // BOTTOM
                g.drawLine(point.x, point.y, point.x - arrowSize, point.y + arrowSize);
                g.drawLine(point.x, point.y, point.x + arrowSize, point.y + arrowSize);
                break;
            default:
                break;
        }
    }

    @Override
    public void setConnect(DragPack dPack) {
        Point mfp = dPack.getFrom();
        Point mtp = dPack.getTo();
        from = (JPanel) dPack.getFromObj();
        to = (JPanel) dPack.getToObj();
        fromSide = new AreaDefine().getArea(from.getLocation(), from.getSize(), mfp);
        toSide = new AreaDefine().getArea(to.getLocation(), to.getSize(), mtp);
        renewConnect();
    }

    void renewConnect() {
        try {
            fp = getConnectPoint(from, fromSide);
            tp = getConnectPoint(to, toSide);
            this.reSize();
        } catch (NullPointerException e) {
            System.out.println("DependencyLine 連線失敗，原因: " + e.getMessage());
            this.setVisible(false);
            cph.removeComponent(this);
        }
    }

    Point getConnectPoint(JPanel jp, int side) {
        Point temp = new Point(0, 0);
        Point jpLocation = cph.getAbsLocation(jp);
        if (side == new AreaDefine().TOP) {
            temp.x = (int) (jpLocation.x + jp.getSize().getWidth() / 2);
            temp.y = jpLocation.y;
        } else if (side == new AreaDefine().RIGHT) {
            temp.x = (int) (jpLocation.x + jp.getSize().getWidth());
            temp.y = (int) (jpLocation.y + jp.getSize().getHeight() / 2);
        } else if (side == new AreaDefine().LEFT) {
            temp.x = jpLocation.x;
            temp.y = (int) (jpLocation.y + jp.getSize().getHeight() / 2);
        } else if (side == new AreaDefine().BOTTOM) {
            temp.x = (int) (jpLocation.x + jp.getSize().getWidth() / 2);
            temp.y = (int) (jpLocation.y + jp.getSize().getHeight());
        } else {
            temp = null;
        }
        return temp;
    }

    @Override
    public void paintSelect(Graphics gra) {
        gra.setColor(Color.BLACK);
        gra.fillRect(fp.x, fp.y, selectBoxSize, selectBoxSize);
        gra.fillRect(tp.x, tp.y, selectBoxSize, selectBoxSize);
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}