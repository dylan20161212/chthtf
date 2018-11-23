package com.thtf.app.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.thtf.app.repositories.CityRepository;


@Service
@Transactional
public class CityService{
	
  final  private  CityRepository cityRepository;


   public CityService(CityRepository cityRepository){
   	    this.cityRepository = cityRepository;
   }
   
   

}