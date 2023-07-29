package com.driver.Services;

import com.driver.Repositories.AirRepository;
import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Service

public class AirService
{

    @Autowired
    private AirRepository airRepository;
    public String addHotel(Hotel hotel){
        return airRepository.addHotel(hotel);
    }
    public Integer addUser(User user)
    {
        return airRepository.addUser(user);
    }

    //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
    //Incase there is a tie return the lexicographically smaller hotelName
    //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
    public String getHotelWithMostFacilities()
    {
        List<Hotel> list=airRepository.getAllHotels();
        int max=0;
        String pans="";
        for(Hotel hotel:list) {
            if (hotel.getFacilities().size() > max) {
                max = Math.max(hotel.getFacilities().size(), max);
                pans=hotel.getHotelName();
            }
        }
        //we have max...
        if(max==0)return "";
        String ans=findLexo(list,max,pans);
        return ans;
    }

    public String findLexo(List<Hotel>list,int max,String pans)
    {
        String ans=pans;
        for(Hotel hotel:list)
        {
            if(hotel.getFacilities().size()==max)
            {
               if(hotel.getHotelName().compareToIgnoreCase(ans)<0)
               {
                   ans=hotel.getHotelName();
               }
            }
        }
        return ans;
    }

    public int bookARoom(Booking booking){
        return airRepository.bookARoom(booking);
    }

    public int getBookings(Integer aadharCard)
    {
        int count=0;
        List<Booking>list=airRepository.getAllBookings();
        for(Booking booking:list)
        {
          if(aadharCard.equals(booking.getBookingAadharCard())){
              count++;
          }
        }
        return count;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName){
        return airRepository.updateFacilities(newFacilities,hotelName);
    }
}
