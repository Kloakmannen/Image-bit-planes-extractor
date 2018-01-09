import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Main {

    static private ImageTool imageTool;
    static private JPanel imagesPanel;
    static private JPanel buttonsPanel;
    static private ArrayList<ImagePanel> imagePanels = new ArrayList<>(8);

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.getContentPane().setLayout(new BorderLayout());
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addImagesJPanelTo(window);
        addButtonsToJFrame(window);

        window.setVisible(true);
        window.setSize(800, 600);
    }

    static private void showImages() throws Exception {
        if (imageTool == null) {
            return;
        }

        BufferedImage[] planes = imageTool.getPlanes();

        for (int i = 0; i < planes.length; i++) {
            ImagePanel temp = imagePanels.get(i);
            temp.setImage(planes[i].getScaledInstance(temp.getImageWidth(),temp.getImageHeight(),Image.SCALE_SMOOTH));
        }
    }

    static private void addImagesJPanelTo(JFrame frame) {
        imagesPanel = new JPanel();
        imagesPanel.setLayout(new GridLayout(2, 4, 5, 5));
        imagesPanel.setMinimumSize(new Dimension(1000, 400));

        for (int i = 0; i < 8; i++) {
            ImagePanel temp = new ImagePanel("Bit layer #" + i);
            imagePanels.add(temp);
            imagesPanel.add(temp);
        }
        frame.getContentPane().add(imagesPanel, BorderLayout.CENTER);
    }

    static private void addButtonsToJFrame(JFrame frame) {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 10, 10));

        JButton loadImage = new JButton("Load 8-bit image");
        JButton saveToFile = new JButton("Save to file");
        buttonsPanel.add(loadImage);
        buttonsPanel.add(saveToFile);

        loadImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.setDialogTitle("Select 8-bit image");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "bmp"));

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        imageTool = new ImageTool(fileChooser.getSelectedFile());
                        showImages();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        //TODO: Implement save to file
        loadImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        frame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }
}
