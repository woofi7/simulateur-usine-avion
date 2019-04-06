package simulation.Model;

import javafx.util.Pair;
import simulation.Utils.Observer;

import java.util.List;

public class ElementFactory extends Factory implements Observer {

    public ElementFactory(String type, List<Pair> iconList, List<ItemStack> inputItems, List<ItemStack> outputItems, int productionTime) {
        super(type, iconList, inputItems, outputItems, productionTime);
        super.productionTime = productionTime;
    }

    @Override
    public Factory clone() {
        return new ElementFactory(super.type, super.iconList, super.inputItems, super.outputItem, (int) this.productionTime);
    }

    @Override
    public ItemStack update(float speed) {
        if (!super.active) {
            return null;
        }
        this.updateIcon();

        if (canProcess()) {
            if (super.progress >= super.productionTime) {
                if (this.resourcesAvailable.size() > 0) {
                    for (ItemStack inputItemStack : super.inputItems) {
                        for (ItemStack availableItemStack : super.resourcesAvailable) {
                            String inputName = inputItemStack.getItem().getName();
                            String availableName = availableItemStack.getItem().getName();
                            int availableQuantity = availableItemStack.getQuantity();
                            int inputQuantity = inputItemStack.getQuantity();
                            if (inputName.equals(availableName) && availableQuantity >= inputQuantity) {
                                availableItemStack.setQuantity(availableQuantity - inputQuantity);
                            }
                        }
                    }
                }
                super.progress = 0;
                return super.outputItem.get(0).clone();
            }
            super.progress += 0.1 * speed;
        }
        return null;
    }

    /**
     * Vérification si l'usine peut produire avec les items qui lui sont disponible.
     * @return true si elle peut produire
     */
    private boolean canProcess() {
        if (super.inputItems.size() == 0) {
            return true;
        }
        boolean canProcess = false;
        for (ItemStack inputItemStack : super.inputItems) {
            for (ItemStack availableItemStack : super.resourcesAvailable) {
                String inputName = inputItemStack.getItem().getName();
                String availableName = availableItemStack.getItem().getName();
                if (inputName.equals(availableName) && availableItemStack.getQuantity() >= inputItemStack.getQuantity()) {
                    canProcess = true;
                    break;
                }
                else {
                    canProcess = false;
                }
            }
        }
        return canProcess;
    }

    @Override
    protected double getTotalProgress() {
        return super.productionTime;
    }

    @Override
    public void update() {
        super.active = !super.active;
    }
}
