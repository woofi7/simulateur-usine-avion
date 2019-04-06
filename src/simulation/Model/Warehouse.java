package simulation.Model;

import javafx.util.Pair;

import java.util.List;

public class Warehouse extends Factory {
    private int capacity;

    public Warehouse(String type, List<Pair> iconList, List<ItemStack> inputItems) {
        super(type, iconList, inputItems, null, inputItems.get(0).getQuantity());
        this.capacity = inputItems.get(0).getQuantity();
        super.productionTime = 1000;
    }

    private Warehouse(String type, List<Pair> iconList, List<ItemStack> inputItems, int capacity) {
        super(type, iconList, inputItems, null, capacity);
        this.capacity = capacity;
        super.productionTime = 1000;
    }

    @Override
    public Factory clone() {
        return new Warehouse(super.type, super.iconList, super.inputItems, this.capacity);
    }

    @Override
    public ItemStack update(float speed) {
        if (super.inactive()) {
            return null;
        }
        this.updateIcon();

        if (this.resourcesAvailable.size() > 0) {
            for (ItemStack inputItemStack : super.inputItems) {
                for (ItemStack availableItemStack : super.resourcesAvailable) {
                    String inputName = inputItemStack.getItem().getName();
                    String availableName = availableItemStack.getItem().getName();
                    if (inputName.equals(availableName)) {
                        super.progress = availableItemStack.getQuantity();
                    }
                }
            }
        }

        if (super.progress >= this.capacity) {
            super.active = false;
            super.updateIcon();
            super.notifyObservers();
        }
        return null;
    }

    @Override
    public double getProductionTime() {
        return productionTime;
    }

    public boolean sellUnit() {
        boolean sold = false;
        for (ItemStack itemStack : super.resourcesAvailable) {
            if (itemStack.getQuantity() > 0) {
                itemStack.setQuantity(itemStack.getQuantity() - 1);
                sold = true;

                if (super.inactive()) {
                    super.active = true;
                    super.notifyObservers();
                }
            }
        }

        return sold;
    }
}
