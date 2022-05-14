/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;

/**
 *
 * @author Dave
 */
public class InHouse extends Part{
    private int machineId;
    
    public InHouse(){
        super(0,"",0,0,0,0);
        this.machineId = 0;
    }
    
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    
    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }
    
    /**
     * @return the machineId
     */
    public int getMachineId(){
        return this.machineId;
    }
}
