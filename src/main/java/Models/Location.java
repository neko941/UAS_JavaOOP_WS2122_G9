/**
 * Author: jatenderjossan
 * Created on: Dec. 25, 2021
 *
 * The Location.java class represents a location which a user can create
 *      in order to save the location of an event
 */

package Models;

import java.util.ArrayList;
import java.util.List;

public class Location {

    // location ID
    private int locationID;
    // street of a location
    private String street;
    // house Number of a location
    private int houseNumber;
    // zip of a location
    private String zip;
    // city of a location
    private String city;
    // country of a location
    private String country;
    // if there are multiple building with different letters e.g. building b (Gebäude b)
    private int building;
    // room of the building
    private int room;

    /**
     * Constructor for creating a location.
     *
     * @param street - street of a location
     * @param houseNumber - house Number of a location
     * @param zip - zip of a location
     * @param city - city of a location
     * @param country - country of a location
     * @param building - if there are multiple building with different letters e.g. building b (Gebäude b)
     * @param room -  room of the building
     */
    public Location(String street, int houseNumber, String zip, String city, String country, int building, int room) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.zip = zip;
        this.city = city;
        this.country = country;
        this.building = building;
        this.room = room;
    }

    /**
     * Constructor for fetching a location from the database
     *
     * @param locationID - ID of the location
     * @param street - street of a location
     * @param houseNumber - house Number of a location
     * @param zip - zip of a location
     * @param city - city of a location
     * @param country - country of a location
     * @param building - if there are multiple building with different letters e.g. building b (Gebäude b)
     * @param room -  room of the building
     */
    public Location(int locationID, String street, int houseNumber, String zip, String city, String country, int building, int room) {
        this.locationID = locationID;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zip = zip;
        this.city = city;
        this.country = country;
        this.building = building;
        this.room = room;
    }

    public boolean checkNotEmpty(String string)
    {
        return string != "" && string != null;
    }

    @Override
    public String toString()
    {
        String temp1;
        if (checkNotEmpty(this.getStreet()) && this.getStreetNumber() != -1)
        {
            temp1 = this.getStreet() + " " + this.getStreetNumber();
        }
        else if (checkNotEmpty(this.getStreet()) && this.getStreetNumber() == -1)
        {
            temp1 = this.getStreet();
        }
        else
        {
            temp1 = String.valueOf(this.getStreetNumber());
        }

        String temp2;
        if (checkNotEmpty(this.getCity()) && checkNotEmpty(this.getZip()))
        {
            temp2 = this.getCity() + " " + this.getZip();
        }
        else if  (checkNotEmpty(this.getCity()) && !checkNotEmpty(this.getZip()))
        {
            temp2 = this.getCity();
        }
        else
        {
            temp2 = this.getZip();
        }

        String[] temp = {
                temp1,
                temp2,
                (this.getCountry() != null) ? this.getCountry() : null,
                (this.getBuilding() != -1) ? "building " + this.getBuilding() : null,
                (this.getRoom() != -1) ? "room " + this.getRoom() : null};

        List<String> result = new ArrayList<String>();
        for(String string : temp) {
            if(checkNotEmpty(string)) {
                result.add(string);
            }
        }

        return String.join(", ", result);
    }
    //###############################################
    //                   Setter
    //###############################################

    public void setLocationID (int locationID) {
        this.locationID = locationID;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    //###############################################
    //                   Getter
    //###############################################

    public int getLocationID() {
        return locationID;
    }

    public String getStreet() {
        return street;
    }

    public int getStreetNumber() {
        return houseNumber;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public int getBuilding() {
        return building;
    }

    public int getRoom() {
        return room;
    }
}
