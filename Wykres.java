import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;

public class Wykres extends JPanel {
	final int marginX = 50;
	final int marginY = 50;
	
	int screenWidth = 800;
	int screenHeight = 400;

	double currentTime = 0;
	double timeStep = 0.05;
	double imageScale = 1;

	RzutUkosny obliczenia = null;
	BufferedImage image = null;

    JLabel epLabel = new JLabel();
	
	public Wykres() {
		image = newImage(screenWidth, screenHeight);
		drawAxesAndLegend(image);
		setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.add(epLabel);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

	BufferedImage newImage(int w, int h) {
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		img.createGraphics();
		return img;
	}
	
	void drawAxesAndLegend(BufferedImage img) {
		Graphics2D g = (Graphics2D) img.getGraphics();
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		
		g.setColor(Color.black);
		g.drawLine(marginX, screenHeight - marginY, screenWidth - marginX, screenHeight - marginY);
		g.drawLine(marginX, marginY, marginX, screenHeight - marginY);
		
		g.drawLine(marginX - 10, marginY, marginX + 10, marginY);
		g.drawLine(screenWidth - marginX, screenHeight - marginY - 10, screenWidth - marginX, screenHeight - marginY + 10);
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		
		g.drawString(nf.format(imageScale * (screenHeight - 2 * marginY)) + " [m]", marginX, marginY - 10);
		g.drawString(nf.format(imageScale * (screenWidth - 2 * marginX)) + " [m]", screenWidth - 2 * marginX, screenHeight - marginY / 2);
		
		g.dispose();
	}

	public void newSimulation(RzutUkosny obliczenia) {
		this.obliczenia = obliczenia;
		scaleImage(obliczenia);
		image = newImage(screenWidth, screenHeight);
		drawAxesAndLegend(image);
		currentTime = 0;
		repaint();

		draw();
	}

	public void draw() {
		Graphics2D g = (Graphics2D) image.getGraphics();
        g.setStroke(new BasicStroke(1));
		g.setColor(Color.BLUE);
        for (double screenX = 0; screenX <= obliczenia.zasieg()/imageScale; screenX += 1.0) {
            int x = (int) (screenX*imageScale);
            int y1 = (int) (obliczenia.getYForX(x)/imageScale);
            int y2 = (int) (obliczenia.getYForX(x+1)/imageScale);
            g.drawLine((int) screenX+marginX, screenHeight-y1-marginY, (int)screenX+1+marginX, screenHeight-y2-marginY);
        }

        g.setStroke(new BasicStroke(3));
        g.setColor(Color.RED);
        for (currentTime = 0; currentTime < obliczenia.czasRzutu(); currentTime += timeStep) {
            int x1 = (int) (obliczenia.getXForT(currentTime)/imageScale + marginX);
            int y1 = (int) (obliczenia.getYForT(currentTime)/imageScale);
            int x2 = (int) (obliczenia.getXForT(currentTime + timeStep)/imageScale + marginX);
            int y2 = (int) (obliczenia.getYForT(currentTime + timeStep)/imageScale);
            g.drawLine(x1, screenHeight - marginY - y1, x2, screenHeight - marginY - y2);

            drawEp(obliczenia.energiaPotencjalna(obliczenia.getYForT(currentTime)));

            paintComponent(getGraphics());
            try {
                Thread.sleep((long) (1000 * timeStep));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

		g.dispose();
	}

    private void drawEp(double ep) {
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.blue);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        epLabel.setText("EP = " + nf.format(ep) + " J");
    }

    void scaleImage(RzutUkosny obliczenia) {
        double x = Math.ceil(obliczenia.zasieg() / (screenWidth- 2*marginX));
        double y = Math.ceil(obliczenia.wysokoscMaksymalna() / (screenHeight - 2*marginY));
        imageScale = Math.max(x, y);
	}
}
