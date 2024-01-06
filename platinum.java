//Shreya S Ramani - ssr210006
class Platinum extends Customer {
    private int bonusBucks;

    public Platinum(String id, String name, double amountSpent, int extra) {
        super(id, name, amountSpent);
        this.bonusBucks = extra;
    }

    public int getBonusBucks() {
        return bonusBucks;
    }
    
    public Platinum(Customer customer)
    {
        super(customer);
        bonusBucks = (int)((customer.getAmountSpent() - 200) / 5);
    }
    
    public Platinum(Gold gold)
    {
        super(gold.id, gold.name, gold.amountSpent);
        bonusBucks = (int)((gold.getAmountSpent() - 200) / 5);
    }

    public void setBonusBucks(int bonusBucks) {
        this.bonusBucks = bonusBucks;
    }
    
}
