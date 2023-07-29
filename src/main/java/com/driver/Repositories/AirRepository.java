package com.driver.Repositories;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
@Repository
public class AirRepository
{
    Map<String, Hotel> hotelDB=new HashMap<>();
    Map<Integer, User> userDB=new HashMap<>();
    Map<String, Booking> bookingDB=new HashMap<>();

    public String addHotel(Hotel hotel)
    {
        if(hotelDB.containsKey(hotel.getHotelName()))return "FAILURE";
        if(hotel.getHotelName()==null)return "an empty a FAILURE";
        hotelDB.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user)
    {
        userDB.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public List<Hotel>getAllHotels(){
        List<Hotel>list=new ArrayList<>();
        for(Hotel hotel:hotelDB.values()){
            list.add(hotel);
        }
        return list;
    }

    public int bookARoom(Booking booking)
    {
        int availableRooms=hotelDB.get(booking.getHotelName()).getAvailableRooms();
        int noOfRooms=booking.getNoOfRooms();
        if(availableRooms<booking.getNoOfRooms())return -1;

        //else you'll book the room availabilty will decrease...
        //here we go.. thanks gotch
        else hotelDB.get(booking.getHotelName()).setAvailableRooms(availableRooms-noOfRooms);
        //else i'll uudi..
        UUID uuid=UUID.randomUUID();
        String bookingId=uuid.toString();
        bookingDB.put(bookingId,booking);

        //let's calculate total bill..
        int totalAmountToBePaid=noOfRooms*hotelDB.get(booking.getHotelName()).getPricePerNight();
        booking.setAmountToBePaid(totalAmountToBePaid);

        return totalAmountToBePaid;
    }

    public List<Booking> getAllBookings(){
        List<Booking>list=new ArrayList<>();
        Collection<Booking>bookings=bookingDB.values();
        list.addAll(bookings);
        return list;

    }
    //We are having a new facilites that a hotel is planning to bring.
    //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
    //return the final updated List of facilities and also update that in your hotelDb
    //Note that newFacilities can also have duplicate facilities possible

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName)
    {
        List<Facility>list=hotelDB.get(hotelName).getFacilities();
        Set<String>set=new HashSet<>();
        for(Facility facility:list)set.add(facility.name());
        for(Facility facility:newFacilities)
        {
            if(!set.contains(facility.name()))list.add(facility);
        }
        return hotelDB.get(hotelName);
    }



}
