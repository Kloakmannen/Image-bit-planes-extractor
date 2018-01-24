import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Main {

    static private ImagePanel originalImage;
    static private JPanel processedImagesPanel;
    static private ArrayList<ImagePanel> imagePanels = new ArrayList<>(8);
    static private StringBuilder stringBuilder = new StringBuilder();
    static private JFrame window;

    public static void main(String[] args) {
        window = new JFrame("Bit planes slicer");
        window.getContentPane().setLayout(new GridLayout(1, 2, 20, 0));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initializeFrame(window);

        window.setVisible(true);
        window.setResizable(false);
        window.setSize(1400, 600);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(dim.width / 2 - window.getSize().width / 2, dim.height / 2 - window.getSize().height / 2);
    }

    private static void initializeFrame(JFrame frame){
        addControlPanelTo(window);
        addImagesJPanelTo(window);
    }

    static private void addControlPanelTo(JFrame window) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        originalImage = new ImagePanel();
        container.add(originalImage);

        addButtonsToJFrame(container);
        window.getContentPane().add(container);
    }

    static private void showImages() throws Exception {
        originalImage.setTile("Original image");

        int imageWidth = originalImage.getImageWidth();
        int imageHeight = originalImage.getImageHeight();
        if (imageWidth > imageHeight) {
            originalImage.setImage(ImageTool.getInstance().getImage().getScaledInstance(-1, imageHeight, Image.SCALE_SMOOTH));
        } else {
            originalImage.setImage(ImageTool.getInstance().getImage().getScaledInstance(imageWidth, -1, Image.SCALE_SMOOTH));
        }

        BufferedImage[] planes = ImageTool.getInstance().getBitPlanes();
        for (int i = 0; i < planes.length; i++) {
            ImagePanel temp = imagePanels.get(i);
            temp.setTile("Bit layer #" + i);
            temp.setImage(planes[i].getScaledInstance(temp.getImageWidth(), -1, Image.SCALE_SMOOTH));
        }
    }

    static private void addImagesJPanelTo(JFrame frame) {
        JPanel container = new JPanel(new BorderLayout());

        processedImagesPanel = new JPanel();
        processedImagesPanel.setLayout(new GridLayout(2, 4, 5, 5));
        processedImagesPanel.setMinimumSize(new Dimension(1000, 400));

        for (int i = 0; i < 8; i++) {
            ImagePanel temp = new ImagePanel();
            imagePanels.add(temp);
            processedImagesPanel.add(temp, BorderLayout.EAST);
        }
        container.add(processedImagesPanel);
        frame.getContentPane().add(container);
    }

    static private void addButtonsToJFrame(JPanel frame) {
        JButton loadImage = new JButton("Load 8-bit image");
        JButton saveToFile = new JButton("Save to file");

        JPanel buttonsContainer = new JPanel(new GridLayout(2, 1, 0, 5));

        buttonsContainer.add(loadImage);
        buttonsContainer.add(saveToFile);
        frame.add(buttonsContainer);

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
                        ImageTool.getInstance().processBitPlanesFromImage(fileChooser.getSelectedFile());
                        showImages();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        saveToFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedImage[] bitPlanes = ImageTool.getInstance().getBitPlanes();
                    if (bitPlanes == null) {
                        JOptionPane.showMessageDialog(window, "Select an image first.", "Warning", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    stringBuilder.setLength(0);
                    FileWriter fileWriter = new FileWriter(ImageTool.getInstance().getImageName() + " - bit planes.txt");
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);


                    for (int i = 0; i < bitPlanes.length; i++) {
                        stringBuilder.append(System.getProperty("line.separator"));
                        stringBuilder.append("Bit player #" + i);
                        stringBuilder.append(System.getProperty("line.separator"));

                        for (int y = 0; y < bitPlanes[i].getHeight(); y++) {
                            for (int x = 0; x < bitPlanes[i].getWidth(); x++) {
                                stringBuilder.append(bitPlanes[i].getRaster().getSample(x, y, 0) + " ");
                            }
                            stringBuilder.append(System.getProperty("line.separator"));
                        }
                    }

                    bufferedWriter.write(stringBuilder.toString());
                    bufferedWriter.flush();
                    bufferedWriter.close();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
