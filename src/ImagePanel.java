import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private JLabel titleLabel;
    private JLabel imageLabel;

    public ImagePanel() {
        setLayout(new BorderLayout());

        titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(titleLabel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        repaint();
    }

    public int getImageWidth() {
        return imageLabel.getWidth();
    }

    public int getImageHeight() {
        return imageLabel.getHeight();
    }

    public void setImage(Image image) {
        imageLabel.setIcon(new ImageIcon(image));
    }

    public void setTile(String title) {titleLabel.setText(title);}

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(200, 200);
    }
}
