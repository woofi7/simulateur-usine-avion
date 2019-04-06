package simulation.Model;

public class ItemStack {
    private int quantity;
    private Item item;

    public ItemStack(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public ItemStack(Item item) {
        this(item, 0);
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public ItemStack clone() {
        return new ItemStack(new Item(this.item.getName()), this.quantity);
    }

    @Override
    public String toString() {
        return this.item.getName() + "-" + this.quantity;
    }
}
