//Shreya S Ramani 
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Math;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        Customer[] array_regular = null;
        Customer[] array_preferred = null;

         Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the file names
        String fileRegular = scanner.nextLine();
        String filePreferred = scanner.nextLine();
        String orders = scanner.nextLine();

        // Close the scanner after reading the input
        scanner.close();
        
        // Read customer data from files
        array_regular = readCustomerData(fileRegular, array_regular);
        array_preferred = readCustomerData(filePreferred, array_preferred);
        

        // Process orders from the "orders.dat" file
        array_preferred = processOrders(orders, array_regular, array_preferred);

        // Write customer data back to files
        writeCustomerData("customer.dat", array_regular);
        writePreferredCustomerData("preferred.dat", array_preferred);
    }

    private static Customer[] readCustomerData(String fileName, Customer[] customers) {
        try {
            File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File " + fileName + " does not exist.");
            return customers; // Return the original array if the file is missing
        }

        Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] customerData = line.split(" ");
                if (customerData.length >= 4) {
                    String id = customerData[0]; //store customer ID
                    String name = customerData[1] + " " + customerData[2]; //store customer name
                    double amountSpent = Double.parseDouble(customerData[3]); //store amount spent

                    if (customerData.length == 4) {
                        Customer customer = new Customer(id, name, amountSpent);
                        customers = increaseArraySize(customers); //add to regular array
                        customers[customers.length - 1] = customer; //create regular customer
                    } else if (customerData.length == 5) {
                        int extra = Integer.parseInt(customerData[4].replace("%", ""));
                        if (amountSpent >= 200) {
                            Platinum platinumCustomer = new Platinum(id, name, amountSpent, extra);
                            customers = increaseArraySize(customers); //add to preferred array
                            customers[customers.length - 1] = platinumCustomer; //create platinum customer
                        } else {
                            Gold goldCustomer = new Gold(id, name, amountSpent, extra);
                            customers = increaseArraySize(customers);
                            customers[customers.length - 1] = goldCustomer; //create gold customer
                        }
                    }
                } else {
                    System.out.println("Error in customer data: " + line);
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customers;
    }

   //function to resize the array when new customers are added
    private static Customer[] increaseArraySize(Customer[] customers) {
        Customer[] newArray; 
        if (customers == null) {
            newArray = new Customer[1];
        } else {
            newArray = new Customer[customers.length + 1];
            System.arraycopy(customers, 0, newArray, 0, customers.length);
        }
        return newArray;
    }

   //function to process the orders file for each customer
    private static Customer[] processOrders(String fileName, Customer[] regularCustomers, Customer[] preferredCustomers) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            int lineNumber = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] orderData = line.split(" ");
                if (orderData.length != 5) {
                    System.out.println("Error in line " + lineNumber + ": Incorrect number of items.");
                    lineNumber++;
                    continue;
                }
    
                String customerId = orderData[0]; //find customer order ID
                String drinkSize = orderData[1]; //find drink size for order
                String drinkType = orderData[2]; //find type of drink for order
                
                //drink type error handling
                if (!drinkType.equalsIgnoreCase("soda") && !drinkType.equalsIgnoreCase("tea") && !drinkType.equalsIgnoreCase("punch")) {
                    System.out.println("Error in line " + lineNumber + ": Invalid drink type.");
                    lineNumber++;
                    continue;
                }
                // Error handling for non-numeric input
                double int_p;
                double quantity;
                try {
                    int_p = Double.parseDouble(orderData[3]);
                    quantity = Double.parseDouble(orderData[4]);
    
                    if (Double.isNaN(int_p) || Double.isNaN(quantity) || Double.isInfinite(int_p) || Double.isInfinite(quantity)) {
                        // Handle invalid input (e.g., display an error message or skip the order)
                        System.out.println("Error in line " + lineNumber + ": Invalid numeric format.");
                        lineNumber++;
                        continue;
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where the input cannot be parsed as a double
                    System.out.println("Error in line " + lineNumber + ": Invalid numeric format.");
                    lineNumber++;
                    continue;
                }
    
                Customer customer = check_id(customerId, regularCustomers, preferredCustomers);
    
                if (customer != null) {
                   //calculate order price
                    double orderPrice = calculate_price(drinkSize, drinkType, int_p, quantity);
    
               
                    if(customer instanceof Customer && !(customer instanceof Gold) && !(customer instanceof Platinum))
                {
                   //when they are still in the regular customer array
                    double newAmountSpent = customer.getAmountSpent() + orderPrice;
                     customer.setAmountSpent(newAmountSpent);
                   
                   if (newAmountSpent >= 200)
                   {
                       if (newAmountSpent - (orderPrice * 0.15) < 200)
                       {
                          //create a new gold customer
                           Gold goldCustomer = new Gold(customer);
                           goldCustomer.applyDiscount(orderPrice);
                           
                           preferredCustomers = increaseArraySize(preferredCustomers);
                           preferredCustomers[preferredCustomers.length - 1] = goldCustomer;
                          
                          //remove from regular array
                           regularCustomers[findIndexOf(regularCustomers, customer.getId())] = null;
                       }
                       else
                       {
                          //create new Platinum customer
                           Platinum platinumCustomer = new Platinum(customer);
                           platinumCustomer.setAmountSpent(newAmountSpent - (orderPrice * 0.15));
                       
                           preferredCustomers = increaseArraySize(preferredCustomers);
                           preferredCustomers[preferredCustomers.length - 1] = platinumCustomer;
                           
                           //remove from regular array
                           regularCustomers[findIndexOf(regularCustomers, customer.getId())] = null;
                       }
                   }
                   else if (newAmountSpent >= 50)
                   {
                      //create new gold customer
                       Gold goldCustomer = new Gold(customer);
                       goldCustomer.applyDiscount(orderPrice);
                     
                       
                       preferredCustomers = increaseArraySize(preferredCustomers);
                       preferredCustomers[preferredCustomers.length - 1] = goldCustomer;
                       regularCustomers[findIndexOf(regularCustomers, customer.getId())] = null;
                   }
                   
                   
                }
                else if(customer instanceof Gold)
                {
                   //when customer is an instance of type gold
                   double newAmountSpent = customer.getAmountSpent() + orderPrice;
                   Gold goldCustomer = (Gold)customer;
                   goldCustomer.setAmountSpent(newAmountSpent);
                   goldCustomer.applyDiscount(orderPrice);
                   
                   if (goldCustomer.getAmountSpent() >= 200)
                   {
                       Platinum newPlatinum = new Platinum(goldCustomer);
                       preferredCustomers[findIndexOf(preferredCustomers, goldCustomer.getId())] = newPlatinum;
                   }
                   
                }
                else if(customer instanceof Platinum)
                {
                   //when customer is an instance of type platinum
                   Platinum platinumCustomer = (Platinum)customer;
                   if(platinumCustomer.getBonusBucks() > orderPrice)
                   {
                      platinumCustomer.setBonusBucks(platinumCustomer.getBonusBucks() - (int)orderPrice);
                      orderPrice = 0.0;
                   }
                   else
                   {
                      orderPrice -= platinumCustomer.getBonusBucks();
                      platinumCustomer.setBonusBucks((int)(orderPrice / 5));
                   }
                    double newAmountSpent = customer.getAmountSpent() + orderPrice;
                    platinumCustomer.setAmountSpent(newAmountSpent);
                    
                }

                    lineNumber++;
                } else {
                    System.out.println("Error in line " + lineNumber + ": Customer not found.");
                    lineNumber++;
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return preferredCustomers;
    }
    

   //function to check customer ID
    private static Customer check_id(String id, Customer[] regularCustomers, Customer[] preferredCustomers) {
        if (regularCustomers != null)
        {
            for (Customer customer : regularCustomers) 
            {
                if (customer != null)
                {
                    if (customer.getId().equals(id)) {
                    return customer;
                }   
                }
            }
        }
        
        if (preferredCustomers != null)
        {
            for (Customer customer : preferredCustomers) 
            {
                if (customer.getId().equals(id)) {
                    return customer;
                }
            }
        }
        return null;
    }
    
    //function to find the index of the customer in the array
    public static int findIndexOf(Customer[] customers, String key)
    {
        for (int i = 0; i < customers.length; i++)
        {
            if (customers[i] != null)
            {
                if (key.equals(customers[i].getId()))
                {
                    return i;
                }   
            }
        }
        return -1;
    }

//function to calculate the price of the order
    private static double calculate_price(String drinkSize, String drinkType, double inchP, double quantity) {
    // Define the base prices per ounce for different drink types
    final double sodaPricePerOunce = 0.20;  // Example price for soda per ounce
    final double teaPricePerOunce = 0.12;   // Example price for tea per ounce
    final double punchPricePerOunce = 0.15; // Example price for punch per ounce

    // Define the dimensions for different drink sizes
    final double smallDiameter = 4.0;    // Example diameter for small drinks in inches
    final double smallHeight = 4.5;      // Example height for small drinks in inches
    final double mediumDiameter = 4.5;   // Example diameter for medium drinks in inches
    final double mediumHeight = 5.75;     // Example height for medium drinks in inches
    final double largeDiameter = 5.5;    // Example diameter for large drinks in inches
    final double largeHeight = 7.0;      // Example height for large drinks in inches

    // Define the quantity for different drink sizes
    final int smallQuantity = 12;
    final int mediumQuantity = 20;
    final int largeQuantity = 32;

    double pricePerOunce = 0;
    double diameter = 0;
    double height = 0;
    int liquidQuantity = 0;

    // Determine the price per ounce based on the drink type
    if ("soda".equalsIgnoreCase(drinkType)) {
        pricePerOunce = sodaPricePerOunce;
    } else if ("tea".equalsIgnoreCase(drinkType)) {
        pricePerOunce = teaPricePerOunce;
    } else if ("punch".equalsIgnoreCase(drinkType)) {
        pricePerOunce = punchPricePerOunce;
    }

    // Determine the dimensions and quantity based on the drink size
    if ("S".equalsIgnoreCase(drinkSize)) {
        diameter = smallDiameter;
        height = smallHeight;
        liquidQuantity = smallQuantity;
    } else if ("M".equalsIgnoreCase(drinkSize)) {
        diameter = mediumDiameter;
        height = mediumHeight;
        liquidQuantity = mediumQuantity;
    } else if ("L".equalsIgnoreCase(drinkSize)) {
        diameter = largeDiameter;
        height = largeHeight;
        liquidQuantity = largeQuantity;
    }

    // Calculate the price of the drink (size * price per ounce)
    double drinkPrice = liquidQuantity * pricePerOunce;

    // Calculate the price of the graphic on the cup based on the inchP
    double graphicPrice = (diameter * height * Math.PI) * inchP;

    // Calculate the total price for the given quantity
    double totalPrice = drinkPrice + graphicPrice;

    return totalPrice * quantity;
}



// REGULAR CUSTOMERS
private static void writeCustomerData(String fileName, Customer[] customers) {
    try(FileWriter writer = new FileWriter(fileName)) {
        if (customers != null)
        {
            for (Customer customer : customers) {
           
                if (customer != null)
                {
                    String data = String.format("%s %s %.2f", customer.getId(), customer.getName(), (float)customer.getAmountSpent());
                    writer.write(data + "\n");
                }
            }
        }
    }
        
     catch (IOException e) {
        e.printStackTrace();
    }
}

//PREFERRED CUSTOMERS
private static void writePreferredCustomerData(String fileName, Customer[] customers) {
    try (FileWriter writer = new FileWriter(fileName)) {
        for (Customer person : customers) {
            if (person instanceof Gold) {
                Gold customer = (Gold) person;
                if (customer.getAmountSpent() <= 200) {
                    if (customer.getDiscount() > 0) {
                        String data = String.format("%s %s %.2f %d%%", customer.getId(), customer.getName(), customer.getAmountSpent(), customer.getDiscount());
                        writer.write(data + "\n");
                    } else {
                        // Check if the amount spent is exactly 0.00
                        if (customer.getAmountSpent() == 0.00) {
                            String data = String.format("%s %s %.2f", customer.getId(), customer.getName(), 0.00);
                            writer.write(data + "\n");
                        } else {
                            String data = String.format("%s %s %.2f", customer.getId(), customer.getName(), customer.getAmountSpent());
                            writer.write(data + "\n");
                        }
                    }
                }
            } else if (person instanceof Platinum) {
                Platinum customer = (Platinum) person;
                String data = String.format("%s %s %.2f %d", customer.getId(), customer.getName(), customer.getAmountSpent(), customer.getBonusBucks());
                writer.write(data + "\n");
            }
            // Check if the customer has a discount
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


}
