import java.awt.*;

public class Pipe {
    private int posX;
    private int posY;
    private int width;
    private int height;
    private Image image;
    private boolean passed = false; // untuk menghitung skor
    private boolean scorable; // hanya pipa atas yang dihitung skornya

    public Pipe(int posX, int posY, int width, int height, Image image, boolean scorable) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;
        this.scorable = scorable;
    }

    // Getter dan Setter
    public int getPosX() { return posX; }
    public void setPosX(int posX) { this.posX = posX; }

    public int getPosY() { return posY; }
    public void setPosY(int posY) { this.posY = posY; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public Image getImage() { return image; }

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    public boolean isScorable() { return scorable; }
}
