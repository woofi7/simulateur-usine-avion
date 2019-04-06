package simulation.Model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class SimulationData {
    private ArrayList<Factory> factories;

    private ArrayList<Route> routes = new ArrayList<>();
    private ArrayList<SimulationElement> simulationElements = new ArrayList<>();
    private ArrayList<Resource> resources = new ArrayList<>();

    public SimulationData(ArrayList<Factory> factories) {
        this.factories = factories;
    }

    public SimulationData() {
        this(null);
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public ArrayList<SimulationElement> getSimulationElements() {
        return simulationElements;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void addRoute(SimulationElement from, SimulationElement to) {
        this.routes.add(new Route(from, to));
    }

    /**
     * Ajouter une usine à la liste simulationElements. L'usine à ajouter doit être présente dans la liste des usines
     * disponible: factories.
     * @param type type de l'usine
     * @param id identifiant de l'usine dans la simulation
     * @param x position x dans la fenêtre
     * @param y position y dans la fenêtre
     */
    public void addFactory(String type, int id, int x, int y) {
        for (Factory factory : factories) {
            if (factory.getType().equals(type)) {
                simulationElements.add(new SimulationElement(factory.clone(), id, x, y));
                return;
            }
        }
        throw new InvalidParameterException("Factory not found:" + type);
    }

    public SimulationElement getElementById(int id) {
        for (SimulationElement simulationElement : simulationElements) {
            if (simulationElement.id == id) {
                return simulationElement;
            }
        }
        return null;
    }

    /**
     * Ajouter un ItemStack sur une route du réseau.
     * @param item Item à ajouter.
     * @param route Route que l'item doit utiliser.
     */
    public void addMovingResource(ItemStack item, Route route) {
        Resource r = new Resource(route);
        r.item = item;
        resources.add(r);
    }

    public class Route {
        public SimulationElement from;
        public SimulationElement to;

        Route(SimulationElement from, SimulationElement to) {
            this.from = from;
            this.to = to;
        }

        public int fromX() {
            return from.x + from.factory.getIcon().getIconWidth() / 2;
        }

        public int fromY() {
            return from.y + from.factory.getIcon().getIconHeight() / 2;
        }

        public int toX() {
            return to.x + from.factory.getIcon().getIconWidth() / 2;
        }

        public int toY() {
            return to.y + from.factory.getIcon().getIconHeight() / 2;
        }
    }

    public class SimulationElement {
        public Factory factory;
        public int id;
        public int x;
        public int y;

        SimulationElement(Factory factory, int id, int x, int y) {
            this.factory = factory;
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public void addResource(ItemStack itemStack) {
            this.factory.addResource(itemStack);
        }
    }

    public class Resource {
        public Route route;
        public ItemStack item;
        public double x;
        public double y;
        public boolean moving;

        Resource(ItemStack item, Route route) {
            this.item = item;
            this.route = route;
            this.x = route.fromX() - item.getItem().getIcon().getIconWidth() / 2;
            this.y = route.fromY() - item.getItem().getIcon().getIconHeight() / 2;
            this.moving = true;
        }

        Resource(Route route) {
            this(new ItemStack(Item.DEFAULT), route);
        }
    }
}
