import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame implements KeyListener, ActionListener {
    private final int width = 400;
    private final int height = 400;
    private final int unitSize = 20;
    private final int gameUnits = (width * height) / (unitSize * unitSize);
    private final int delay = 100;

    private ArrayList<Point> snake;
    private Point food;
    private boolean isRunning = false;
    private int direction;
    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        snake = new ArrayList<>();
        startGame();
        timer = new Timer(delay, this);
        timer.start();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(0, 0));
        generateFood();
        direction = KeyEvent.VK_RIGHT;
        isRunning = true;
    }

    public void generateFood() {
        Random rand = new Random();
        int x = rand.nextInt(width / unitSize) * unitSize;
        int y = rand.nextInt(height / unitSize) * unitSize;
        food = new Point(x, y);
    }

    public void paint(Graphics g) {
        super.paint(g);

        if (!isRunning) {
            gameOver(g);
            return;
        }

        // Draw the snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x, point.y, unitSize, unitSize);
        }

        // Draw the food
        g.setColor(Color.RED);
        g.fillRect(food.x, food.y, unitSize, unitSize);
    }

    public void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.y -= unitSize;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y += unitSize;
                break;
            case KeyEvent.VK_LEFT:
                newHead.x -= unitSize;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x += unitSize;
                break;
            default:
                return;
        }

        // Check if the new head collides with walls or itself
        if (newHead.x < 0 || newHead.x >= width || newHead.y < 0 || newHead.y >= height ||
                snake.contains(newHead)) {
            isRunning = false;
            return;
        }

        snake.add(0, newHead);

        // Check if snake eats food
        if (newHead.equals(food)) {
            generateFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over!", width / 2 - 120, height / 2 - 20);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press 'Space' to Restart", width / 2 - 130, height / 2 + 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!isRunning && key == KeyEvent.VK_SPACE) {
            startGame();
        } else if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
            direction = KeyEvent.VK_UP;
        } else if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
            direction = KeyEvent.VK_DOWN;
        } else if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
            direction = KeyEvent.VK_LEFT;
        } else if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
            direction = KeyEvent.VK_RIGHT;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        new SnakeGame().setVisible(true);
    }
}
