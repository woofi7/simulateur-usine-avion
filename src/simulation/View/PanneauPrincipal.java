package simulation.View;

import simulation.Controller.SellingStrategies.ConstantSellingStrategy;
import simulation.Controller.SellingStrategies.ISellingStrategy;
import simulation.Controller.SellingStrategies.RandomSellingStrategy;
import simulation.Controller.SimulationController;
import simulation.Model.*;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class PanneauPrincipal extends JPanel {
	private static final long serialVersionUID = 1L;
	private final SimulationController controller;

	private JProgressBar progressBar;

	public PanneauPrincipal(SimulationController controller) {
		this.controller = controller;
		this.progressBar = new JProgressBar(0, this.controller.getSellingStrategy().sellingInterval()); //TODO: Déplacer dasn undateProgressBar
		this.progressBar.setStringPainted(true);
		this.add(this.progressBar);
		this.setFocusable(true);
	}

	public void updateProgressBar() {
		ISellingStrategy strategy = this.controller.getSellingStrategy();
		if (strategy instanceof ConstantSellingStrategy) {
			this.progressBar.setIndeterminate(false);
		}
		else if (strategy instanceof RandomSellingStrategy) {
			this.progressBar.setIndeterminate(true);
		}
		this.progressBar.setString(strategy.toString());
	}

	/**
	 * Dessiner les différents éléments de la simulation. Les éléments sont dessiné dans l'ordre: Routes, Resources,
	 * Factories;
	 *
	 * Pour avoir de l'information sur ces éléments peser sur F3.
	 * @param g Élément graphique.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//Draw routes
		for (SimulationData.Route route : this.controller.getRoutes()) {
			g.drawLine(route.fromX(), route.fromY(), route.toX(), route.toY());
		}

		//Draw resources
		for(SimulationData.Resource resource : this.controller.getResources()) {
			ImageIcon i = resource.item.getItem().getIcon();
			i.paintIcon(this, g, (int) resource.x, (int) resource.y);
		}

		//Draw factories
		for (SimulationData.SimulationElement element : this.controller.getSimulationElements()) {
			ImageIcon i = element.factory.getIcon();
			i.paintIcon(this, g, element.x, element.y);
			if (element.factory.inactive()) {
				g.setColor(Color.RED);
			}
			DecimalFormat df = new DecimalFormat("##0");
			g.drawString(df.format(element.factory.getProgress()) + "/" + df.format(element.factory.getProductionTime()), element.x, element.y);
			g.setColor(Color.BLACK);

			int offSet = g.getFontMetrics().getHeight();
			for (ItemStack itemStack : element.factory.getResourcesAvailable()) {
				g.drawString(itemStack.toString(), element.x, element.y + element.factory.getIcon().getIconHeight() + offSet);
				offSet += g.getFontMetrics().getHeight();
			}
		}

		//Draw selling bar
		this.progressBar.setValue((int) (this.controller.getSellingStrategy().getProgress() * this.controller.getSpeed()));
	}

	public void updateGraphics() {
		for(SimulationData.Resource resource : this.controller.getResources()) {
			if (resource.moving) {
				int iconHeight = resource.item.getItem().getIcon().getIconHeight();
				int iconWidth = resource.item.getItem().getIcon().getIconWidth();

				double xDiff = resource.route.toX() - resource.route.fromX();
				double yDiff = resource.route.toY() - resource.route.fromY();
				resource.x = resource.x + 0.5 * this.controller.getSpeed() * Math.cos(Math.atan2(yDiff, xDiff));
				resource.y = resource.y + 0.5 * this.controller.getSpeed() * Math.sin(Math.atan2(yDiff, xDiff));

				double xDistance = Math.abs(resource.route.toX() - resource.route.fromX() + iconWidth / 2);
				double yDistance = Math.abs(resource.route.toY() - resource.route.fromY() + iconHeight / 2);
				double xActual = Math.abs(resource.x - resource.route.fromX() + iconWidth);
				double yActual = Math.abs(resource.y - resource.route.fromY() + iconHeight);

				if (xActual >= xDistance && yActual >= yDistance) {
					this.controller.addMovingResourceOnFactory(resource);
					break;
				}
			}
		}
		repaint();
	}
}