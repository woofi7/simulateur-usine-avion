package simulation.Model;

import javafx.util.Pair;
import simulation.Utils.Observer;
import simulation.Utils.Subject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Factory extends Subject {
    protected String type;
    protected ImageIcon icon;
    protected List<Pair> iconList;

    protected List<ItemStack> inputItems;
    protected List<ItemStack> outputItem;
    protected List<ItemStack> resourcesAvailable;

    protected double progress;
    private double totalProgress;
    protected double productionTime;
    protected boolean active = true;

    public Factory(String type, List<Pair> iconList, List<ItemStack> inputItems, List<ItemStack> outputItems, int totalProgress) {
        this.progress = 0;
        this.type = type;
        this.iconList = iconList;
        this.inputItems = inputItems;
        this.outputItem = outputItems;
        this.totalProgress = totalProgress;
        this.resourcesAvailable = new ArrayList<>();

        this.updateIcon();
    }

    @Override
    public abstract Factory clone();

    @Override
    protected List<Observer> getObservers() {
        return super.getObservers();
    }

    public abstract ItemStack update(float speed);

    protected double getTotalProgress() {
        return totalProgress;
    }

    public double getProductionTime() {
        return productionTime;
    }

    /**
     * Mise à jour de l'icone de l'usine en fonction de son progrès ou de sa capacité.
     */
    protected void updateIcon() {
        if (progress < this.getTotalProgress() / 3) {
            for (Pair pair : iconList) {
                if (pair.getKey().equals("vide")) {
                    icon = new ImageIcon((String) pair.getValue());
                }
            }
        }
        else if (progress > this.getTotalProgress() / 3 && progress < this.getTotalProgress() / 3 * 2) {
            for (Pair pair : iconList) {
                if (pair.getKey().equals("un-tiers")) {
                    icon = new ImageIcon((String) pair.getValue());
                }
            }
        }
        else if (progress > this.getTotalProgress() / 3 * 2 && progress < this.getTotalProgress() - this.getTotalProgress() * 0.1) {
            for (Pair pair : iconList) {
                if (pair.getKey().equals("deux-tiers")) {
                    icon = new ImageIcon((String) pair.getValue());
                }
            }
        }
        else {
            for (Pair pair : iconList) {
                if (pair.getKey().equals("plein")) {
                    icon = new ImageIcon((String) pair.getValue());
                }
            }
        }
    }

    /**
     * Ajout d'une ressource à la liste l'items disponible à l'usine.
     * @param itemStack item à être ajouté
     */
    public void addResource(ItemStack itemStack) {
        boolean found = false;
        for (ItemStack stack : this.resourcesAvailable) {
            if (itemStack.getItem().getName().equals(stack.getItem().getName())) {
                stack.setQuantity(stack.getQuantity() + itemStack.getQuantity());
                found = true;
                break;
            }
        }

        if (!found) {
            for (ItemStack inputItem : this.inputItems) {
                if (itemStack.getItem().getName().equals(inputItem.getItem().getName())) {
                    this.resourcesAvailable.add(itemStack);
                }
            }
        }
    }

    public boolean inactive() {
        return !this.active;
    }

    public String getType() {
        return this.type;
    }

    public ImageIcon getIcon() {
        return this.icon;
    }

    public List<ItemStack> getResourcesAvailable() {
        return this.resourcesAvailable;
    }

    public List<ItemStack> getInputResources() {
        return this.inputItems;
    }

    public void setProductionTime(double productionTime) {
        this.productionTime = productionTime;
    }

    public List<ItemStack> getOutputResources() {
        return this.outputItem;
    }

    public double getProgress() {
        return progress;
    }
}
