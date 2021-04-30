package climber_example.level_creator;

import Kartoha_Engine2D.physics.Material;
import Kartoha_Engine2D.ui.Scene;
import Kartoha_Engine2D.utils.JsonReader;
import climber_example.Level;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class UI {

    private final Container container;

    public UI(Container container) {
        this.container = container;
    }

    public void setUI() {

        JButton changeModeButton = new JButton("Изменить режим добавления");
        changeModeButton.addActionListener(e -> container.changeAddingMode());
        changeModeButton.setBounds(0, 0, 250, 40);

        JButton deleteButton = new JButton("Удалить объекты");
        deleteButton.addActionListener(e -> container.deleteAllObjects());
        deleteButton.setBounds(250, 40, 250, 40);

        JButton setLevelButton = new JButton("Открыть уровень");
        setLevelButton.addActionListener(e -> {
            FileDialog dialog = new FileDialog((Frame) null);
            dialog.setVisible(true);
            String directory = dialog.getDirectory();
            String filename = dialog.getFile();
            dialog.dispose();
            if (directory != null && filename != null) {
                String path = directory + filename;
                try {
                    container.setLevel((Level) (new JsonReader(path).read(Level.class)));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        setLevelButton.setBounds(250, 0, 250, 40);

        JButton saveLevelButton = new JButton("Сохранить уровень");
        saveLevelButton.addActionListener(e -> {
            FileDialog dialog = new FileDialog((Frame) null);
            dialog.setVisible(true);
            String directory = dialog.getDirectory();
            String filename = dialog.getFile();
            dialog.dispose();
            if (directory != null && filename != null) {
                String path = directory + filename;
                try {
                    File file = new File(path);
                    PrintWriter writer = new PrintWriter(file);
                    writer.println(container.getLevel().toJson());
                    writer.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        saveLevelButton.setBounds(0, 40, 250, 40);

        JPanel materialsPanel = new JPanel();
        materialsPanel.setLayout(null);
        materialsPanel.setBounds(0, 80, 500, 720);
        int y = 0;
        for (Material material : Material.values()) {
            JButton button = new JButton(material.getName());
            button.setBackground(material.outlineColor);
            button.setBounds(0, y, 468, 40);
            button.addActionListener(e -> container.setCurrentMaterial(material));
            materialsPanel.add(button);
            y += 40;
        }
        JScrollPane scroll = new JScrollPane(materialsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(0, 80, 485, 682);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(500, 800));

        panel.add(changeModeButton);
        panel.add(setLevelButton);
        panel.add(saveLevelButton);
        panel.add(deleteButton);
        panel.add(scroll);

        JFrame frame = new JFrame("Управление создателем уровней");
        frame.setSize(500, 800);
        frame.add(panel);
        frame.setVisible(true);
    }
}
