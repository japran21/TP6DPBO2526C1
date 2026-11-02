import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class View extends JPanel {
    int width = 360;
    int height = 640;

    private Logic logic;
    private JLabel scoreLabel;
    private JButton startButton;
    private JButton exitButton;
    private JLabel titleLabel;

    public View(Logic logic) {
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);
        setLayout(null); // null layout supaya komponen bisa diposisikan bebas

        // Komponen Main Menu

        // Judul game
        titleLabel = new JLabel("Flappy Bird", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 150, width, 50);
        add(titleLabel);

        // Tombol Mulai Permainan
        startButton = new JButton("Mulai Permainan");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setBounds(width / 2 - 100, 300, 200, 50);
        startButton.setBackground(new Color(76, 175, 80));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(startButton);

        // Tombol Keluar
        exitButton = new JButton("Keluar");
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setBounds(width / 2 - 100, 380, 200, 50);
        exitButton.setBackground(new Color(244, 67, 54));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(exitButton);

        // Action: Mulai Permainan
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideMenu();
                logic.startGame();
                showScoreLabel();
                requestFocus(); // kembalikan fokus ke panel untuk KeyListener
            }
        });

        // Action: Keluar dari permainan
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // JLabel Skor (tersembunyi sampai game mulai)
        scoreLabel = new JLabel("Skor: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(0, 10, width, 30);
        add(scoreLabel);
        scoreLabel.setVisible(false);

        // Key Listener untuk menerima input keyboard
        setFocusable(true);
        addKeyListener(logic);
    }

    // Sembunyikan komponen menu
    private void hideMenu() {
        titleLabel.setVisible(false);
        startButton.setVisible(false);
        exitButton.setVisible(false);
    }

    // Tampilkan komponen menu
    private void showMenu() {
        titleLabel.setVisible(true);
        startButton.setVisible(true);
        exitButton.setVisible(true);
    }

    // Update teks skor dari Logic
    public void updateScoreLabel(int score) {
        scoreLabel.setText("Skor: " + score);
    }

    // Sembunyikan skor
    public void hideScoreLabel() {
        scoreLabel.setVisible(false);
    }

    // Tampilkan skor
    public void showScoreLabel() {
        scoreLabel.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Gambar background
        Image bg = logic.getBackgroundImage();
        if (bg != null) {
            g.drawImage(bg, 0, 0, width, height, null);
        }

        // Jika game belum dimulai (masih di menu), cukup gambar background saja
        if (!logic.isGameStarted()) {
            return;
        }

        // Gambar burung
        Player player = logic.getPlayer();
        if (player != null) {
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(),
                    player.getWidth(), player.getHeight(), null);
        }

        // Gambar semua pipa
        ArrayList<Pipe> pipes = logic.getPipes();
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(),
                    pipe.getWidth(), pipe.getHeight(), null);
        }

        // Tampilkan pesan Game Over
        if (logic.isGameOver()) {
            // Kotak semi-transparan sebagai latar pesan
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRoundRect(width / 2 - 120, height / 2 - 60, 240, 120, 20, 20);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            String msg = "GAME OVER";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(msg);
            g.drawString(msg, (width - textWidth) / 2, height / 2 - 15);

            g.setFont(new Font("Arial", Font.PLAIN, 16));
            String restart = "Tekan R untuk restart";
            fm = g.getFontMetrics();
            textWidth = fm.stringWidth(restart);
            g.drawString(restart, (width - textWidth) / 2, height / 2 + 20);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            String finalScore = "Skor: " + logic.getScore();
            fm = g.getFontMetrics();
            textWidth = fm.stringWidth(finalScore);
            g.drawString(finalScore, (width - textWidth) / 2, height / 2 + 50);
        }
    }
}