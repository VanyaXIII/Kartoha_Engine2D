package climber_example.level_creator;

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

    public void setUI(){

        JButton changeModeButton = new JButton("Изменить режим добавления");
        changeModeButton.addActionListener(e -> container.changeAddingMode());
        changeModeButton.setBounds(0, 0, 250, 40);

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
        saveLevelButton.setBounds(0,40,250,40);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(500, 500));

        panel.add(changeModeButton);
        panel.add(setLevelButton);
        panel.add(saveLevelButton);

        JFrame frame = new JFrame("Управление создателем уровней");
        frame.setSize(500, 500);
        frame.add(panel);
        frame.setVisible(true);
    }
}
