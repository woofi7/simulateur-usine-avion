package simulation.View;

import simulation.Controller.SimulationController;
import simulation.Model.SimulationData;
import simulation.Utils.ConfigReader;
import simulation.View.FenetrePrincipale;
import simulation.View.FenetreStrategie;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class MenuFenetre extends JMenuBar {

	private static final long serialVersionUID = 1L;
	private static final String MENU_FICHIER_TITRE = "Fichier";
	private static final String MENU_FICHIER_CHARGER = "Charger";
	private static final String MENU_FICHIER_QUITTER = "Quitter";
	private static final String MENU_SIMULATION_TITRE = "Simulation";
	private static final String MENU_SIMULATION_CHOISIR = "Choisir";
	private static final String MENU_AIDE_TITRE = "Aide";
	private static final String MENU_AIDE_PROPOS = "À propos de...";
	private static final String MENU_SIMULATION_VITESSE = "Vitesse de la simulation";

	private final SimulationController controller;
	private JMenuItem vitesse05;
	private JMenuItem vitesse1;
	private JMenuItem vitesse2;
	private JMenuItem vitesse5;
	private JMenuItem vitesse10;

	public MenuFenetre(SimulationController controller) {
		this.controller = controller;
		ajouterMenuFichier();
		ajouterMenuSimulation();
		ajouterMenuAide();
	}

	/**
	 * Créer le menu de Fichier
	 */
	private void ajouterMenuFichier() {
		JMenuItem menuCharger = new JMenuItem(MENU_FICHIER_CHARGER);
		menuCharger.addActionListener((ActionEvent e) -> {
			JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			fileChooser.setDialogTitle("Sélectionnez un fichier de configuration");
			fileChooser.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filtre = new FileNameExtensionFilter(".xml", "xml");
			fileChooser.addChoosableFileFilter(filtre);

			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				this.controller.updateModel(fileChooser.getSelectedFile());
			}
		});

		JMenuItem menuQuitter = new JMenuItem(MENU_FICHIER_QUITTER);
		menuQuitter.addActionListener((ActionEvent e) -> System.exit(0));

		JMenu menuFichier = new JMenu(MENU_FICHIER_TITRE);
		menuFichier.add(menuCharger);
		menuFichier.add(menuQuitter);
		add(menuFichier);
	}

	/**
	 * Créer le menu de SimulationController
	 */
	private void ajouterMenuSimulation() {
		JMenuItem menuChoisir = new JMenuItem(MENU_SIMULATION_CHOISIR);
		menuChoisir.addActionListener((ActionEvent e) -> {
			new FenetreStrategie(this.controller);
		});

		JMenu menuVitesse = new JMenu(MENU_SIMULATION_VITESSE);
		this.vitesse05 = new JMenuItem("0.5");
		menuVitesse.add(this.vitesse05);
		vitesse05.addActionListener((ActionEvent e) -> {
			this.controller.setSpeed(0.5);
			setSelectedSpeed();
		});

		this.vitesse1 = new JMenuItem("1");
		menuVitesse.add(this.vitesse1);
		vitesse1.addActionListener((ActionEvent e) -> {
			this.controller.setSpeed(1);
			setSelectedSpeed();
		});

		this.vitesse2 = new JMenuItem("2");
		menuVitesse.add(this.vitesse2);
		vitesse2.addActionListener((ActionEvent e) -> {
			this.controller.setSpeed(2);
			setSelectedSpeed();
		});

		this.vitesse5 = new JMenuItem("5");
		menuVitesse.add(this.vitesse5);
		vitesse5.addActionListener((ActionEvent e) -> {
			this.controller.setSpeed(5);
			setSelectedSpeed();
		});

		this.vitesse10 = new JMenuItem("10");
		menuVitesse.add(this.vitesse10);
		vitesse10.addActionListener((ActionEvent e) -> {
			this.controller.setSpeed(10);
			setSelectedSpeed();
		});

		setSelectedSpeed();

		JMenu menuSimulation = new JMenu(MENU_SIMULATION_TITRE);
		menuSimulation.add(menuChoisir);
		menuSimulation.add(menuVitesse);
		add(menuSimulation);
	}

	private void setSelectedSpeed() {
		vitesse05.setBackground(UIManager.getColor ( "Panel.background" ));
		vitesse1.setBackground(UIManager.getColor ( "Panel.background" ));
		vitesse2.setBackground(UIManager.getColor ( "Panel.background" ));
		vitesse5.setBackground(UIManager.getColor ( "Panel.background" ));
		vitesse10.setBackground(UIManager.getColor ( "Panel.background" ));
		if (this.controller.getSpeed() == 0.5) {
			vitesse05.setBackground(Color.lightGray);
		}
		else if (this.controller.getSpeed() == 1) {
			vitesse1.setBackground(Color.lightGray);
		}
		else if (this.controller.getSpeed() == 2) {
			vitesse2.setBackground(Color.lightGray);
		}
		else if (this.controller.getSpeed() == 5) {
			vitesse5.setBackground(Color.lightGray);
		}
		else if (this.controller.getSpeed() == 10) {
			vitesse10.setBackground(Color.lightGray);
		}
	}

	/**
	 * Créer le menu Aide
	 */
	private void ajouterMenuAide() {
		JMenu menuAide = new JMenu(MENU_AIDE_TITRE);
		JMenuItem menuPropos = new JMenuItem(MENU_AIDE_PROPOS);
		menuAide.add(menuPropos);

		menuPropos.addActionListener((ActionEvent e) -> JOptionPane.showMessageDialog(null,
				"<html><p>Application simulant une chaine de production d'avions.</p>" + "<br>"
						+ "<p>&copy; &nbsp; 2017 &nbsp; Ghizlane El Boussaidi</p>"
						+ "<p>&copy; &nbsp; 2017 &nbsp; Dany Boisvert</p>"
						+ "<p>&copy; &nbsp; 2017 &nbsp; Vincent Mattard</p>" + ""
						+ "<p>&copy; &nbsp; 2019 &nbsp; Nicolas Signori</p>" + "<br>"
						+ "<p>&Eacute;cole de technologie sup&eacute;rieure</p></html>"));
		add(menuAide);
	}

}
