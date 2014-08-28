package fr.remiguitreau.aroeven.xls.list.exporter;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;

import fr.remiguitreau.aroeven.lucine.api.LucineAccessDescriptor;
import fr.remiguitreau.aroeven.xls.list.exporter.gui.AroevenPanel;

public class Launcher {

    public static void main(final String[] args) throws FileNotFoundException, IOException {
        final LucineAccessDescriptor lucineAccessDescriptor = extractFromFile();
        final JFrame frame = new JFrame("Aroeven Lucine");
        frame.setSize(new Dimension(400, 400));
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setContentPane(new AroevenPanel(lucineAccessDescriptor));
        frame.pack();
        frame.validate();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static LucineAccessDescriptor extractFromFile() throws FileNotFoundException, IOException {
        final Properties props = new Properties();
        props.load(new FileInputStream("aroeven.properties"));
        return new LucineAccessDescriptor(props.getProperty("lucine-login"),
                props.getProperty("lucine-password"), props.getProperty("lucine-url"));
    }
}
