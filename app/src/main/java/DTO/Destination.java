package DTO;

import java.io.Serializable;

/**
 * Created by lequan on 4/23/2016.
 */
public class Destination implements Serializable
{
    String name;
    String number;

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public Destination(String name, String address)
    {
        this.name = name;
        this.address = address;
    }

    public Destination(String name, String number, String address)
    {

        this.name = name;
        this.number = number;
        this.address = address;
    }

    String address;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

}
