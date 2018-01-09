import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class ImageTool {

    private BufferedImage image;

    public ImageTool(File image) throws IOException {
        this.image = ImageIO.read(image);
    }

    public BufferedImage[] getPlanes() throws Exception {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        BufferedImage[] bitPlanes = new BufferedImage[8];
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

        return bitPlanes;
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
