import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PicturePanel extends JPanel {

    private BufferedImage picture;

    public PicturePanel(BufferedImage picture) {
        this.picture = picture;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g;
        if (picture == null) {
            return;
        }

        graphics2D.drawImage(picture, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() {
        if (picture == null) {
            return new Dimension(200, 200);
        }
        return new Dimension(picture.getWidth(), picture.getHeight());
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(200, 200);
    }
}
