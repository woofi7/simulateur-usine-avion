package simulation.View;

import simulation.Controller.SimulationController;
import simulation.Model.SimulationData;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

public class FenetrePrincipale extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String TITRE_FENETRE = "Laboratoire 1 : LOG121 - SimulationController";
	private static final Dimension DIMENSION = new Dimension(700, 700);

	public FenetrePrincipale(SimulationController controller) {
		PanneauPrincipal panneauPrincipal = new PanneauPrincipal(controller);
		MenuFenetre menuFenetre = new MenuFenetre(controller);
		add(panneauPrincipal);
		add(menuFenetre, BorderLayout.NORTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(TITRE_FENETRE);
		setSize(DIMENSION);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);

		controller.setPanneauPrincipal(panneauPrincipal);
	}
}
