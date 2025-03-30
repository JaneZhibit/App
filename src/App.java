/*
Данный класс занимается отображением панелей из других классов
и работает с конфигурационным файлом
 */

import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class App extends JFrame {
    private Properties config;
    private File configFile = new File("config.properties");
    private static App proxy;
    public int w = 1280, h = 720;

    private App() {
        super("Harry Potter and the Programmer's Pain");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(w, h);
        this.setLayout(null);
        setResizable(false);
        loadConfig();
        setVisible(true);
    }

    public static App getProxy() {
        if (proxy == null) {
            proxy = new App();
        }
        return proxy;
    }

    // данная функция нужна для передачи экрана для надписи, которую ты добавила(:
    public JFrame getFrame(){
        return this;
    }

    public void showMenu() {
        getContentPane().removeAll();
        getContentPane().add(Menu.getProxy().getPanel());
        Menu.getProxy().playMusic();
        revalidate();
        repaint();
    }

    public void showPlay() {
        getContentPane().removeAll();
        Play newPlayInstance = new Play(); // Создаем новую игру
        getContentPane().add(newPlayInstance.getPanel());
        newPlayInstance.playMusic();
        revalidate();
        repaint();
        newPlayInstance.requestFocus();
    }


    public void showSettings() {
        getContentPane().removeAll();
        getContentPane().add(Settings.getProxy().getPanel());
        revalidate();
        repaint();
    }

    public void showSorting() {
        getContentPane().removeAll();

        // Кнопка "Сортировать"
        JButton genButton = new JButton("Генерация");
        genButton.setBounds(210, 20, 200, 30);
        add(genButton);

        JButton sortButton = new JButton("Сортировать");
        sortButton.setBounds(420, 20, 200, 30);
        add(sortButton);

        // Панель сортировки
        Sorting sorting = new Sorting(300);
        sorting.setBounds(0, 50, w, h-50);
        add(sorting);

        // Обработчик для кнопок
        genButton.addActionListener(e -> sorting.gen());

        sortButton.addActionListener(e -> sorting.startSorting());

        // Кнопка "Назад"
        JButton backButton = new JButton("Назад");
        backButton.setBounds(20, 20, 100, 30);
        backButton.addActionListener(e -> showMenu());
        add(backButton);

        revalidate();
        repaint();
    }

    private void loadConfig() {
        config = new Properties();
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Properties getConfig() {
        return config;
    }

    public void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            config.store(fos, "Game settings file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getProxy().showMenu();
    }
}
