import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;

public class ImageTool {

    private static ImageTool instance = null;
    private BufferedImage image;
    private BufferedImage[] bitPlanes;
    private String imageName;
    private String[] pixelBinaryLUT;
    private static final int planesNo = 8;

    private ImageTool() {
        pixelBinaryLUT = new String[256];
        try {
            for (int i = 0; i < 256; i++) {
                pixelBinaryLUT[i] = decimalTo8BitBinary(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ImageTool getInstance() {
        if (instance == null) {
            instance = new ImageTool();
        }
        return instance;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage[] getBitPlanes() {
        return bitPlanes;
    }

    public String getImageName() {
        return imageName;
    }

    private void extractFileNameWithoutExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf('.');
        imageName = fileName.substring(0, extensionIndex);
    }

    public void processBitPlanesFromImage(File imageFile) throws Exception {
        image = ImageIO.read(imageFile);
        extractFileNameWithoutExtension(imageFile.getName());

//        image.getColorModel().getPixelSize();

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        bitPlanes = new BufferedImage[8];
        for (int i = 0; i < bitPlanes.length; i++) {
            bitPlanes[i] = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_BINARY);
        }

        Raster imageData = image.getData();
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                int pixelValue = imageData.getSample(i, j, 0);
                String planesOfPixel = pixelBinaryLUT[pixelValue];
                addBitsToPlanes(bitPlanes, planesOfPixel, i, j);
            }
        }
    }

    private String decimalTo8BitBinary(int number) throws Exception {
        if (number < 0 || number > 255) {
            throw new Exception("Number out of range");
        }

        if (number == 0) {
            return "00000000";
        }

        StringBuilder stringBuilder = new StringBuilder(8);
        int reminder;

        while (number != 0) {
            reminder = number % 2;
            number /= 2;
            stringBuilder.append(reminder);
        }

        while (stringBuilder.length() < 8) {
            stringBuilder.append(0);
        }

        return stringBuilder.reverse().toString();
    }


    private void addBitsToPlanes(BufferedImage[] bitPlanes, String planesForPixel, int x, int y) throws Exception {
        if (planesForPixel.length() != planesNo) {
            throw new Exception("Invalid planes length");
        }

        for (int i = 0; i < planesNo; i++) {
            int binaryValue = Integer.valueOf(String.valueOf(planesForPixel.charAt(planesNo - 1 - i)));
            bitPlanes[i].getRaster().setSample(x, y, 0, binaryValue);
        }
    }
}
