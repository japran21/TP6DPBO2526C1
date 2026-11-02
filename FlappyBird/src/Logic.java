import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    // Player settings
    int playerStartX = frameWidth / 8;
    int playerStartY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    // Pipe settings
    int pipeWidth = 110;
    int pipeHeight = 600;
    int pipeStartX = frameWidth;
    int pipeGap = frameHeight / 4; // celah antara pipa atas dan bawah

    Timer gameLoop;
    Timer pipeSpawner;
    int gravity = 1;
    int velocityX = -4; // kecepatan pipa bergerak ke kiri

    View view;
    Image birdImage;
    Image upperPipeImage;
    Image lowerPipeImage;
    Image backgroundImage;
    Player player;
    ArrayList<Pipe> pipes;
    Clip soundtrack;

    boolean gameStarted = false;
    boolean gameOver = false;
    int score = 0;

    public Logic() {
        birdImage = new ImageIcon(getClass().getResource("/Assets/tengkorak.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("/Assets/metal2-trimmed-flipped.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("/Assets/metal1-trimmed.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getResource("/Assets/avanged.jpeg")).getImage();

        URL soundtrackURL = getClass().getResource("/Assets/Avenged Sevenfold - Hail to the King (Audio).wav");
        if (soundtrackURL == null) {
            throw new IllegalStateException("Soundtrack not found: /Assets/Avenged Sevenfold - Hail to the King (Audio).wav");
        }
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundtrackURL)) {
            soundtrack = AudioSystem.getClip();
            soundtrack.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new IllegalStateException("Could not load soundtrack: /Assets/Avenged Sevenfold - Hail to the King (Audio).wav", e);
        }

        player = new Player(playerStartX, playerStartY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<>();

        // Game loop 60 FPS, tapi BELUM mulai sampai pemain tekan SPACE
        gameLoop = new Timer(1000 / 60, this);

        // Spawn pipa setiap 1.5 detik
        pipeSpawner = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawnPipe();
            }
        });
    }

    public void setView(View view) {
        this.view = view;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getScore() {
        return score;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void startGame() {
        gameStarted = true;
        gameOver = false;
        score = 0;
        player.setPosX(playerStartX);
        player.setPosY(playerStartY);
        player.setVelocityY(0);
        pipes.clear();
        if (view != null) {
            view.updateScoreLabel(score);
        }
        startSoundtrack();
        gameLoop.start();
        pipeSpawner.start();
    }

    public void resetGame() {
        // Reset semua state untuk restart
        player.setPosX(playerStartX);
        player.setPosY(playerStartY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        gameOver = false;
        gameStarted = true;
        if (view != null) {
            view.updateScoreLabel(score);
        }
        startSoundtrack();
        gameLoop.start();
        pipeSpawner.start();
    }

    private void startSoundtrack() {
        soundtrack.stop();
        soundtrack.setFramePosition(0);
        soundtrack.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void spawnPipe() {
        // Posisi Y acak untuk pipa atas (agar celah berpindah-pindah)
        int randomPipeY = (int) (0 - pipeHeight / 4.0 - Math.random() * (pipeHeight / 2.0));

        // Pipa atas (scorable = true, hanya pipa atas yang dihitung skor)
        Pipe upperPipe = new Pipe(pipeStartX, randomPipeY, pipeWidth, pipeHeight, upperPipeImage, true);
        pipes.add(upperPipe);

        // Pipa bawah (scorable = false)
        Pipe lowerPipe = new Pipe(pipeStartX, randomPipeY + pipeHeight + pipeGap, pipeWidth, pipeHeight, lowerPipeImage, false);
        pipes.add(lowerPipe);
    }

    public void move() {
        // Gerakkan burung
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0)); // batas atas layar

        // Gerakkan semua pipa ke kiri
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + velocityX);

            // Hitung skor: hanya pipa yang scorable (pipa atas) yang menambah skor
            if (!pipe.isPassed() && pipe.isScorable() && pipe.getPosX() + pipe.getWidth() < player.getPosX()) {
                pipe.setPassed(true);
                score += 1; // +1 setiap melewati satu pasang pipa
                if (view != null) {
                    view.updateScoreLabel(score);
                }
            }
            // Tandai pipa bawah juga sebagai passed (agar tidak dihitung ulang)
            if (!pipe.isPassed() && !pipe.isScorable() && pipe.getPosX() + pipe.getWidth() < player.getPosX()) {
                pipe.setPassed(true);
            }

            // Cek tabrakan dengan pipa
            if (collision(player, pipe)) {
                gameOver = true;
            }
        }

        // Hapus pipa yang sudah keluar layar
        pipes.removeIf(pipe -> pipe.getPosX() + pipe.getWidth() < 0);

        // Cek apakah burung jatuh ke bawah layar (batas bawah JFrame)
        if (player.getPosY() + player.getHeight() >= frameHeight) {
            gameOver = true;
        }

        // Hentikan game jika Game Over
        if (gameOver) {
            gameLoop.stop();
            pipeSpawner.stop();
            soundtrack.stop();
        }
    }

    public boolean collision(Player player, Pipe pipe) {
        // Cek overlap antara rectangle burung dan rectangle pipa
        return player.getPosX() < pipe.getPosX() + pipe.getWidth() &&
               player.getPosX() + player.getWidth() > pipe.getPosX() &&
               player.getPosY() < pipe.getPosY() + pipe.getHeight() &&
               player.getPosY() + player.getHeight() > pipe.getPosY();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if (view != null) {
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver && gameStarted) {
                // Lompat saat game sedang berjalan
                player.setVelocityY(-10);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_R) {
            if (gameOver) {
                // Restart game saat Game Over dengan menekan tombol R
                resetGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}