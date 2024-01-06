//Shreya S Ramani - ssr210006
class Customer {
    protected String id;
    protected String name;
    protected double amountSpent;

    public Customer(String id, String name, double amountSpent) {
        this.id = id;
        this.name = name;
        this.amountSpent = amountSpent;
    }
    
    public Customer(Customer customer)
    {
        this.id = customer.getId();
        this.name = customer.getName();
        this.amountSpent = customer.getAmountSpent();
    }

    // Getters and setters for common attributes
    public double getAmountSpent() {
        return amountSpent;
    }


    public void setAmountSpent(double amountSpent) {
        this.amountSpent = amountSpent;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
}
