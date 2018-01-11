import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;

public class ImageTool {

    private static BufferedImage image;
    private static BufferedImage[] bitPlanes;
    private static String imageName;

    public static BufferedImage getImage() {
        return image;
    }

    public static BufferedImage[] getBitPlanes() {
        return bitPlanes;
    }

    public static String getImageName() {
        return imageName;
    }

    private static void extractFileNameWithoutExtension(String fileName){
        int extensionIndex = fileName.lastIndexOf('.');
        imageName = fileName.substring(0,extensionIndex);
    }

    public static void processBitPlanesFromImage(File imageFile) throws Exception {
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
                String planes = decimalTo8BitBinary(pixelValue);
                addBitsToPlanes(bitPlanes, planes, i, j);
            }
        }
    }

    private static String decimalTo8BitBinary(int number) throws Exception {
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


    private static void addBitsToPlanes(BufferedImage[] bitPlanes, String planesForPixel, int x, int y) throws Exception {
        int planesNo = 8;
        if (planesForPixel.length() != planesNo) {
            throw new Exception("Invalid planes length");
        }

        for (int i = 0; i < planesNo; i++) {
            int binaryValue = Integer.valueOf(String.valueOf(planesForPixel.charAt(planesNo - 1 - i)));
            bitPlanes[i].getRaster().setSample(x, y, 0, binaryValue);
        }
    }
}
