package simulation.Controller;

import simulation.Model.*;
import simulation.Controller.SellingStrategies.*;
import simulation.Utils.ConfigReader;
import simulation.View.FenetrePrincipale;
import simulation.Controller.SellingStrategies.ISellingStrategy;
import simulation.View.PanneauPrincipal;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SimulationController {
    private PanneauPrincipal panneauPrincipal;
    private SimulationData model;
    private Timer timer;

    //Simulation
    private double speed = 1;
    private boolean runningSimulation = false;
    private ISellingStrategy sellingStrategy;
    private ArrayList<Double> sellTime;
    private double currentSellTime = 0;

    public SimulationController() {
        this.sellingStrategy = new ConstantSellingStrategy();
        this.sellTime = new ArrayList<>();
        this.model = new SimulationData();
        new FenetrePrincipale(this);

        timer();
    }

    /**
     * Cr�ation de l'horloge de la simulation. � chaque tour, les factories sont interrog�es si elles ont une
     * ressource de termin� et � �tre envoy� sur le r�seau. La position des �l�ments graphiques est recalcul� et
     * redessin�. V�rification si une vente � �t� effectu�e et optimisation de la production des usines en fonction
     * de la vitesse de vente des avions.
     */
    private void timer() {
        timer = new Timer(1, e -> {
            factoryGenerating();
            this.panneauPrincipal.updateGraphics();
            selling();
            optimizeGeneration();
        });
        timer.start();
    }

    /**
     * Application de la strat�gie de vente. Si une vente est effectu�e 1 avion est enlev� des entrep�ts qui contiennent
     * des avions de la simulation.
     */
    private void selling() {
        if (!runningSimulation) {
            return;
        }

        this.currentSellTime++;


        if (model != null && sellingStrategy.sell(this.speed)) {
            for (SimulationData.SimulationElement simulationElement : model.getSimulationElements()) {
                if (simulationElement.factory instanceof Warehouse) {
                    this.sellTime.add(currentSellTime);
                    currentSellTime = 0;
                    double totalTime = 0;
                    for (Double time : this.sellTime) {
                        totalTime += time;
                    }
                    totalTime /= this.sellTime.size();
                    simulationElement.factory.setProductionTime(totalTime * 0.1 * speed);
                    if (simulationElement.factory.getResourcesAvailable().size() > 0) {
                        if (((Warehouse) simulationElement.factory).sellUnit()){
                            System.out.println("1 Unit sold");
                        }
                        else {
                            System.out.println("No units to sell");
                        }
                    }
                }
            }
        }
    }

    /**
     * Optimisation du rythme de production de la chaine de production en fonction du temps de vente des avions.
     * Chaque �l�ment de la chaine va interoger les �l�ment � sa sortie pour ajuster son temps de production. Si
     * plusieurs usines du m�me type pointe vers la m�me destination, la charge sera s�par�e entre le diff�rentes
     * usines.
     */
    private void optimizeGeneration() {
        for (SimulationData.Route route : model.getRoutes()) {
            for (ItemStack inputResource : route.to.factory.getInputResources()) {
                for (ItemStack outputResource : route.from.factory.getOutputResources()) {
                    String inputName = inputResource.getItem().getName();
                    String outputName = outputResource.getItem().getName();

                    if (inputName.equals(outputName)) {
                        int inputQuantity = 1;
                        if (route.to.factory instanceof ElementFactory) {
                            inputQuantity = inputResource.getQuantity();
                        }
                        int outputQuantity = outputResource.getQuantity();
                        double inputProductionTime = route.to.factory.getProductionTime();

                        if (inputProductionTime == -1) {
                            continue;
                        }
                        double outputProductionTime = inputProductionTime / inputQuantity * outputQuantity;
                        int sameTypeOutput = getSameTypeOutput(route.to.factory, route.from.factory.getType());
                        route.from.factory.setProductionTime(outputProductionTime * sameTypeOutput);
                    }
                }
            }
        }
    }

    /**
     * Cherche le nombre d'usines du m�me type dont leur la destination est la m�me.
     * @param factory usine de destination
     * @param inputType type d'usine source
     * @return nombre d'usine sources avec factory en destination
     */
    private int getSameTypeOutput(Factory factory, String inputType) {
        int sameOutput = 0;
        for (SimulationData.Route route : model.getRoutes()) {
            if (route.to.factory.equals(factory)) {
                if (route.from.factory.getType().equals(inputType)) {
                    sameOutput++;
                }
            }
        }
        return sameOutput;
    }

    /**
     * V�rification aupr�s des usines si elle ont termin� de construire leur item. Si oui, l'itemStack est ajout�
     * sur le r�seau.
     */
    private void factoryGenerating() {
        for (SimulationData.SimulationElement element : model.getSimulationElements()) {
            ItemStack itemStack = element.factory.update((float) this.speed);
            if (itemStack == null) {
                continue;
            }
            addResourceOnRoute(element, itemStack);
        }
    }


    /**
     * Ajout de l'itemStack sur la route de sortie de l'�l�ment.
     * @param element �l�ment source qui envoie l'item.
     * @param itemStack ItemStack � envoyer.
     */
    private void addResourceOnRoute(SimulationData.SimulationElement element, ItemStack itemStack) {
        for (SimulationData.Route route : model.getRoutes()) {
            if (route.from.id == element.id) {
                model.addMovingResource(itemStack, route);
            }
        }
    }

    /**
     * Ajour de l'itemStack � l'usine de destination de la route.
     * @param resource �l�ment sur la route
     */
    public void addMovingResourceOnFactory(SimulationData.Resource resource) {
        resource.moving = false;
        resource.route.to.addResource(resource.item);
        model.getResources().remove(resource);
    }


    /**
     * Ajout d'un mod�le � la simulation et r�initialisation de l'horloge.
     * @param file fichier contenant les informations de la simualtion.
     */
    public void updateModel(File file) {
        this.model = ConfigReader.parse(file);
        this.runningSimulation = true;
        this.sellTime = new ArrayList<>();
        this.currentSellTime = 0;
        this.sellingStrategy.reset();
        timer.restart();
    }

    /**
     * Appliquer une nouvelle strat�gie de vente.
     * @param strategy strat�gie de vente
     */
    public void setSellingStrategy(ISellingStrategy strategy) {
        this.sellingStrategy = strategy;
        this.panneauPrincipal.updateProgressBar();
    }

    public ISellingStrategy getSellingStrategy() {
        return sellingStrategy;
    }

    public List<SimulationData.Route> getRoutes() {
        return this.model.getRoutes();
    }

    public List<SimulationData.Resource> getResources() {
        return this.model.getResources();
    }

    public List<SimulationData.SimulationElement> getSimulationElements() {
        return this.model.getSimulationElements();
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        this.sellTime = new ArrayList<>();
    }

    public void setPanneauPrincipal(PanneauPrincipal panneauPrincipal) {
        this.panneauPrincipal = panneauPrincipal;
    }
}
