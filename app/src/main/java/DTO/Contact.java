package DTO;

import java.io.Serializable;

/**
 * Created by lequan on 4/23/2016.
 */
public class Contact implements Serializable
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

    public Contact(String name, String number, String address)
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
