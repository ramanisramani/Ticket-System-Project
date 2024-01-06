//Shreya S Ramani - ssr210006
class Gold extends Customer {
    private int discount;

     public Gold(String id, String name, double amountSpent, int extra) {
        super(id, name, amountSpent);
        this.discount = extra;
    }
    
    public Gold(Customer customer)
    {
        super(customer);
        
        //checking for which discount
        if(amountSpent >= 150)
       {
          discount = 15;
       }
       else if(amountSpent >= 100)
       {
          discount = 10;
       }
       else
       {
          discount = 5;
       }
    }


    public int getDiscount() {
        return discount;
    }
    
    public void applyDiscount(double cost) {
       //applying appropriate discount
       if(amountSpent >= 150)
       {
          discount = 15;
       }
       else if(amountSpent >= 100)
       {
          discount = 10;
       }
       else
       {
          discount = 5;
       }
       double thisDiscount = cost * (discount / 100.0);
       this.amountSpent -= thisDiscount;
       
       if(amountSpent >= 150)
       {
          discount = 15;
       }
       else if(amountSpent >= 100)
       {
          discount = 10;
       }
       else
       {
          discount = 5;
       }
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
    
}
