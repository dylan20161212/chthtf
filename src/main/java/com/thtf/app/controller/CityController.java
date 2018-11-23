package com.thtf.app.controller;




import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thtf.app.domains.City;
import com.thtf.app.repositories.CityRepository;

@RestController
@RequestMapping("/api")
public class CityController {

    @Autowired
	private  CityRepository cityRepository;


	@RequestMapping("/findCity")
	ResponseEntity<City> findCity() {
		   URI location = URI.create("/findCity");
		   HttpHeaders responseHeaders = new HttpHeaders();
		   responseHeaders.setLocation(location);
		   responseHeaders.set("MyResponseHeader8888888888812", "MyVatttlue");
		   City city = this.cityRepository.findByNameAndStateAllIgnoringCase("1", "2");
		   return new ResponseEntity<City>(city, responseHeaders, HttpStatus.CREATED);
	}
	
	
	@RequestMapping("/cities")
	ResponseEntity<List<City>> findAll(Pageable pageabel) {
		   URI location = URI.create("/cities");
		   HttpHeaders responseHeaders = new HttpHeaders();
		   responseHeaders.setLocation(location);
		   responseHeaders.set("MyResponseHeader8888888888812", "MyValue");
		  // PageImpl<List<City>> pageImpl = new PageImpl<List<City>>(content)
		   
		   Page<City> page = this.cityRepository.findAll(pageabel);
		   
		   responseHeaders.set("pageTotal1", page.getTotalPages()+"");
		   responseHeaders.set("totalElements", page.getTotalElements()+"");
		 
		   
		   return new ResponseEntity<List<City>>(page.getContent(), responseHeaders, HttpStatus.CREATED);
	}
	
	
	/**
     * POST  /students : Create a new student.
     *
     * @param studentDTO the studentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new studentDTO, or with status 400 (Bad Request) if the student has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/city")
    public ResponseEntity<City> createCity(@Valid @RequestBody City city) throws URISyntaxException {
      //  log.debug("REST request to save Student : {}", studentDTO);
        if (city.getId() != null) {
            throw new RuntimeException("A new city cannot already have an ID",  new Throwable("city exist!"));
        }
        City result = this.cityRepository.save(city);
        HttpHeaders responseHeaders = new HttpHeaders();
		   //responseHeaders.setLocation(location);
		   responseHeaders.set("create result", "add successfull");
        return ResponseEntity.created(new URI("/api/cities" + result.getId()))
            .headers(responseHeaders)
            .body(result);
    }


}