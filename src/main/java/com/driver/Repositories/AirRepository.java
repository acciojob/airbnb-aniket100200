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
        if(hotel==null || hotel.getHotelName()==null)return "an empty a FAILURE";

        if(hotelDB.containsKey(hotel.getHotelName()))return "FAILURE";

        hotelDB.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }


    public Integer addUser(User user)
    {
        if(user==null)return -1;
        userDB.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public List<Hotel>getAllHotels(){
        List<Hotel>list=new ArrayList<>();
        Collection<Hotel>hotels=hotelDB.values();
        list.addAll(hotels);
        return list;
    }
    //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
    //Have bookingId as a random UUID generated String
    //save the booking Entity and keep the bookingId as a primary key
    //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
    //If there aren't enough rooms available in the hotel that we are trying to book return -1
    //in other case return total amount paid
    public int bookARoom(Booking booking)
    {
        if(booking==null)return -1;
        String hotel=booking.getHotelName();
        if(hotel==null || !hotelDB.containsKey(hotel))return -1;
        int noOfRooms=booking.getNoOfRooms();
        int availableRooms=hotelDB.get(hotel).getAvailableRooms();
        if(availableRooms<noOfRooms)return -1;

        //else i'll uudi..
        UUID uuid=UUID.randomUUID();

        String bookingId=uuid.toString();
        bookingDB.put(bookingId,booking);
        hotelDB.get(hotel).setAvailableRooms(availableRooms-noOfRooms);

        //let's calculate total bill..
        int totalAmountToBePaid=noOfRooms*hotelDB.get(booking.getHotelName()).getPricePerNight();
        booking.setAmountToBePaid(totalAmountToBePaid);

        return totalAmountToBePaid;
    }

    public List<Booking> getAllBookings()
    {
        List<Booking>list=new ArrayList<>();
        if(bookingDB.size()==0)return list;
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
        if(hotelDB.containsKey(hotelName)==false)return null;

        if(newFacilities==null)return hotelDB.get(hotelName);

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
