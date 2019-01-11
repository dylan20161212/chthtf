package [basePackage].service;

import [basePackage].service.*;
import [basePackage].domain.*;
import [basePackage].repository.*;

import [basePackage].service.dto.*;
import [basePackage].service.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing [domainName].
 */
@Service
@Transactional
public class [domainName]ServiceImpl implements [domainName]Service {

    private final Logger log = LoggerFactory.getLogger([domainName]Service.class);

    private final [domainName]Repository [domainNameVar]Repository;

    private final [domainName]Mapper [domainNameVar]Mapper;


    public [domainName]ServiceImpl([domainName]Repository [domainNameVar]Repository, [domainName]Mapper [domainNameVar]Mapper) {
        this.[domainNameVar]Repository = [domainNameVar]Repository;
        this.[domainNameVar]Mapper = [domainNameVar]Mapper;
    }

    /**
     * Save a [domainNameVar].
     *
     * @param [domainNameVar]DTO the entity to save
     * @return the persisted entity
     */
    
    public [domainName]DTO save([domainName]DTO [domainNameVar]DTO) {
        log.debug("Request to save [domainName] : {}", [domainNameVar]DTO);
        [domainName] [domainNameVar] = [domainNameVar]Mapper.toEntity([domainNameVar]DTO);
        [domainNameVar] = [domainNameVar]Repository.save([domainNameVar]);
        [domainName]DTO result = [domainNameVar]Mapper.toDto([domainNameVar]);
        [domainNameVar]SearchRepository.save([domainNameVar]);
        return result;
    }

    /**
     * Get all the [domainNameVar]s.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    
    @Transactional(readOnly = true)
    public Page<[domainName]DTO> findAll(Pageable pageable) {
        log.debug("Request to get all [domainName]s");
        return [domainNameVar]Repository.findAll(pageable)
            .map([domainNameVar]Mapper::toDto);
    }
    
    
    
	@Transactional(readOnly = true)
	public Page<[domainName]DTO> findAll(Map<String, Object> filters) {
        log.debug("Request to get all [domainName]s");
        return new PageImpl<>( [domainNameVar]Repository.findAll().stream().map([domainNameVar]Mapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new)), null, [domainNameVar]Repository.getTotalRows(filters));
	}

    /**
     * Get one [domainNameVar] by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    
    @Transactional(readOnly = true)
    public [domainName]DTO findOne(Long id) {
        log.debug("Request to get [domainName] : {}", id);
        [domainName] [domainNameVar] = [domainNameVar]Repository.findOne(id);
        return [domainNameVar]Mapper.toDto([domainNameVar]);
    }

    /**
     * Delete the [domainNameVar] by id.
     *
     * @param id the id of the entity
     */
    
    public void delete(Long id) {
        log.debug("Request to delete [domainName] : {}", id);
        [domainNameVar]Repository.delete(id);
    }

 
}
