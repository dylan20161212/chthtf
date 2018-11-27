package com.thtf.app.repositories;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.stereotype.Repository;

/**
 * An implementation of Spring Boot's AuditEventRepository.
 */
@Repository
public class CustomAuditEventRepository implements AuditEventRepository {

	private static final String AUTHORIZATION_FAILURE = "AUTHORIZATION_FAILURE";
	
	protected static final int EVENT_DATA_COLUMN_MAX_LENGTH = 255;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private List<AuditEvent> eventList = new ArrayList<AuditEvent>();
	@Override
	public void add(AuditEvent event) {
		// TODO Auto-generated method stub
		this.eventList.add(event);
		System.out.println("add event occured : "+event.getType());
		System.out.println("事件列表包括:【");
		this.eventList.forEach(System.out::println);
		System.out.println("】");
	}
	@Override
	public List<AuditEvent> find(String principal, Instant after, String type) {
	
		List<AuditEvent> list = eventList.stream()
		.filter(event ->principal != null && event.getPrincipal().equals(principal))
		.filter(event ->after != null && event.getTimestamp().isAfter(after))
		.filter(event ->type != null && event.getType().equals(type))
		.collect(Collectors.toList());
		System.out.println("过滤后的event列表--->");
		list.stream().forEach(System.out::println);
		
		return list;
		
	}

	

}
