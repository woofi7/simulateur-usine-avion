package simulation.View;

import simulation.Controller.SellingStrategies.ConstantSellingStrategy;
import simulation.Controller.SellingStrategies.RandomSellingStrategy;
import simulation.Controller.SimulationController;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

public class PanneauStrategie extends JPanel {
	private static final long serialVersionUID = 1L;
	private final SimulationController controller;

	public PanneauStrategie(SimulationController controller) {
		this.controller = controller;
		ButtonGroup groupeBoutons = new ButtonGroup();

		JRadioButton strategie1 = new JRadioButton("Constant");
		JRadioButton strategie2 = new JRadioButton("Aléatoire");
		groupeBoutons.add(strategie1);
		groupeBoutons.add(strategie2);
		if (this.controller.getSellingStrategy() instanceof ConstantSellingStrategy) {
			groupeBoutons.setSelected(strategie1.getModel(), true);
		}
		else if (this.controller.getSellingStrategy() instanceof RandomSellingStrategy) {
			groupeBoutons.setSelected(strategie2.getModel(), true);
		}

		JButton boutonConfirmer = new JButton("Confirmer");
		boutonConfirmer.addActionListener((ActionEvent e) -> {
			if (getSelectedButtonText(groupeBoutons).equals("Constant")) {
				this.controller.setSellingStrategy(new ConstantSellingStrategy());
			}
			else if (getSelectedButtonText(groupeBoutons).equals("Aléatoire")) {
				this.controller.setSellingStrategy(new RandomSellingStrategy());
			}

			// Fermer la fenêtre du composant
			SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();
		});

		JButton boutonAnnuler = new JButton("Annuler");

		boutonAnnuler.addActionListener((ActionEvent e) -> {
			// Fermer la fenêtre du composant
			SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();
		});

		add(strategie1);
		add(strategie2);		
		add(boutonConfirmer);
		add(boutonAnnuler);
	}

	/**
	 * Retourne le bouton sélectionné dans un groupe de boutons.
	 */
	private String getSelectedButtonText(ButtonGroup groupeBoutons) {
		for (Enumeration<AbstractButton> boutons = groupeBoutons.getElements(); boutons.hasMoreElements();) {
			AbstractButton bouton = boutons.nextElement();
			if (bouton.isSelected()) {
				return bouton.getText();
			}
		}

		return null;
	}

}
