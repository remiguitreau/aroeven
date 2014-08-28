package fr.remiguitreau.aroeven.xls.list.exporter.gui;

import org.apache.commons.httpclient.HttpClient;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import lombok.extern.slf4j.Slf4j;
import fr.remiguitreau.aroeven.lucine.api.AroevenSite;
import fr.remiguitreau.aroeven.lucine.api.AroevenStay;
import fr.remiguitreau.aroeven.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeven.lucine.api.LucineAccessDescriptor;
import fr.remiguitreau.aroeven.lucine.api.StaySeason;
import fr.remiguitreau.aroeven.lucine.api.dsl.AroevenStaysDescriptorBuilder;
import fr.remiguitreau.aroeven.lucine.impl.DefaultLucineAPIDriver;
import fr.remiguitreau.aroeven.lucine.impl.LucineAPIConnectorByHttpClient;
import fr.remiguitreau.aroeven.lucine.impl.LucineAPIRequestFactoryImpl;
import fr.remiguitreau.aroeven.lucine.impl.StaysExtractorFromCSVFormat;
import fr.remiguitreau.aroeven.xls.list.exporter.StaysExporterInXlsxFile;
import fr.remiguitreau.aroeven.xls.list.exporter.StaysExporterListener;

@Slf4j
public class AroevenPanel extends JPanel implements StaysExporterListener {

    private JComboBox<AroevenSite> site;

    private JComboBox<StaySeason> season;

    private JComboBox<Integer> year;

    private JProgressBar progressBar;

    private JButton cancelButton;

    private JButton openButton;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final LucineAccessDescriptor lucineAccessDescriptor;

    private Future<?> currentFuture;

    private TitledBorder progressBarBorder;

    private File lastExportFile;

    public AroevenPanel(final LucineAccessDescriptor lucineAccessDescriptor) {
        super(new GridBagLayout());
        this.lucineAccessDescriptor = lucineAccessDescriptor;
        initComponents();
    }

    private void initComponents() {
        site = new JComboBox<AroevenSite>();
        for (final AroevenSite aroevenSite : AroevenSite.values()) {
            site.addItem(aroevenSite);
        }
        int y = 0;
        add(new JLabel("Site :"), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        add(site, new GridBagConstraints(1, y, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        y++;
        season = new JComboBox<StaySeason>();
        for (final StaySeason staySeason : StaySeason.values()) {
            season.addItem(staySeason);
        }
        add(new JLabel("Saison :"), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        add(season, new GridBagConstraints(1, y, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        y++;
        year = new JComboBox<Integer>();
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        year.addItem(currentYear);
        year.addItem(currentYear - 1);
        year.addItem(currentYear - 2);
        year.addItem(currentYear - 3);
        year.addItem(currentYear - 4);
        add(new JLabel("Année :"), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        add(year, new GridBagConstraints(1, y, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        y++;
        add(new JLabel("      "), new GridBagConstraints(0, y, 2, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        y++;

        final JButton button = new JButton("Exporter la liste des séjours");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                currentFuture = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        stopCurrentExport();
                        showExportPendingElements();
                        try {
                            final AroevenStaysDescriptor staysDescriptor = AroevenStaysDescriptorBuilder.newStayDescriptor().organizedBy(
                                    (AroevenSite) site.getSelectedItem()).onSeason(
                                    (StaySeason) season.getSelectedItem()).fromYear(
                                    ((Integer) year.getSelectedItem()).intValue()).build();
                            log.info("Will export stays with descriptor: site={} ; season={} ; year={}",
                                    staysDescriptor.aroevenSite, staysDescriptor.season, staysDescriptor.year);
                            progressBar.setStringPainted(false);
                            progressBarBorder.setTitle("Interrogation Lucine...");
                            final List<AroevenStay> stays = new DefaultLucineAPIDriver(
                                    new LucineAPIConnectorByHttpClient(new HttpClient(),
                                            lucineAccessDescriptor, new LucineAPIRequestFactoryImpl()),
                                    new StaysExtractorFromCSVFormat()).retrieveStays(staysDescriptor);
                            progressBar.setStringPainted(true);
                            progressBar.setIndeterminate(false);
                            progressBar.setMaximum(stays.size());
                            progressBarBorder.setTitle("Export des séjours...");
                            lastExportFile = new StaysExporterInXlsxFile().exportStays(stays,
                                    AroevenPanel.this);
                            log.info("{} stays exported to {}", stays.size(), lastExportFile);
                        } catch (final Exception ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Une erreur est survenue pendant la récupération des séjours.", "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        } finally {
                            currentFuture = null;
                            hideExportPendingElements();
                            if (lastExportFile != null) {
                                openButton.setVisible(true);
                            }
                        }
                    }
                });
            }
        });
        add(button, new GridBagConstraints(0, y, 2, 1, 0, 0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        y++;
        final JPanel exportPanel = new JPanel();
        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(200, 50));
        progressBar.setSize(new Dimension(200, 50));
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        progressBarBorder = BorderFactory.createTitledBorder("Reading...");
        progressBar.setBorder(progressBarBorder);
        exportPanel.add(progressBar);
        cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                stopCurrentExport();
            }

        });
        cancelButton.setVisible(false);
        exportPanel.add(cancelButton);
        openButton = new JButton("Ouvrir export");
        openButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent evt) {
                try {
                    Runtime.getRuntime().exec("cmd /c start " + lastExportFile.getAbsolutePath());
                } catch (final IOException ex) {
                    log.info("Unable to open file " + lastExportFile, ex);
                }
            }
        });
        openButton.setVisible(false);
        exportPanel.add(openButton);
        add(exportPanel, new GridBagConstraints(0, y, 2, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }

    @Override
    public void newStayExported() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (progressBar != null && progressBar.isVisible()) {
                    progressBar.setValue(progressBar.getValue() + 1);
                    ((JFrame) SwingUtilities.getWindowAncestor(AroevenPanel.this)).setTitle(progressBar.getString()
                            + " - Aroeven Lucine");
                }
            }
        });
    }

    private void stopCurrentExport() {
        if (currentFuture != null) {
            currentFuture.cancel(true);
            currentFuture = null;
        }
        lastExportFile = null;
        openButton.setVisible(false);
        hideExportPendingElements();
    }

    private void showExportPendingElements() {
        progressBar.setValue(0);
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
        cancelButton.setVisible(true);
    }

    private void hideExportPendingElements() {
        progressBar.setVisible(false);
        cancelButton.setVisible(false);
        ((JFrame) SwingUtilities.getWindowAncestor(this)).setTitle("Aroeven Lucine");
    }
}
