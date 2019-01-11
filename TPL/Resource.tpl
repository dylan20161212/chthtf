package [basePackage].web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import [basePackage].web.rest.errors.BadRequestAlertException;
import [basePackage].web.rest.util.HeaderUtil;
import [basePackage].web.rest.util.PaginationUtil;
import [basePackage].service.[domainName]Service;
import [basePackage].service.dto.[domainName]DTO;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing [domainName].
 */
@RestController
@RequestMapping("/api")
public class [domainName]Resource {

    private final Logger log = LoggerFactory.getLogger([domainName]Resource.class);

    private static final String ENTITY_NAME = "[domainNameVar]";

    private final [domainName]Service [domainNameVar]Service;

    public [domainName]Resource([domainName]Service [domainNameVar]Service) {
        this.[domainNameVar]Service = [domainNameVar]Service;
    }

    /**
     * POST  /[domainNameVarHythen]s : Create a new [domainNameVar].
     *
     * @param [domainNameVar]DTO the [domainNameVar]DTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new [domainNameVar]DTO, or with status 400 (Bad Request) if the [domainNameVar] has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/[domainNameVarHythen]s")
    @Timed
    public ResponseEntity<[domainName]DTO> create[domainName](@RequestBody [domainName]DTO [domainNameVar]DTO) throws URISyntaxException {
        log.debug("REST request to save [domainName] : {}", [domainNameVar]DTO);
        if ([domainNameVar]DTO.getId() != null) {
            throw new BadRequestAlertException("A new [domainNameVar] cannot already have an ID", ENTITY_NAME, "idexists");
        }
        [domainName]DTO result = [domainNameVar]Service.save([domainNameVar]DTO);
        return ResponseEntity.created(new URI("/api/[domainNameVarHythen]s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /[domainNameVarHythen]s : Updates an existing [domainNameVar].
     *
     * @param [domainNameVar]DTO the [domainNameVar]DTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated [domainNameVar]DTO,
     * or with status 400 (Bad Request) if the [domainNameVar]DTO is not valid,
     * or with status 500 (Internal Server Error) if the [domainNameVar]DTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/[domainNameVarHythen]s")
    @Timed
    public ResponseEntity<[domainName]DTO> update[domainName](@RequestBody [domainName]DTO [domainNameVar]DTO) throws URISyntaxException {
        log.debug("REST request to update [domainName] : {}", [domainNameVar]DTO);
        if ([domainNameVar]DTO.getId() == null) {
            return create[domainName]([domainNameVar]DTO);
        }
        [domainName]DTO result = [domainNameVar]Service.save([domainNameVar]DTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, [domainNameVar]DTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /[domainNameVarHythen]s : get all the dutySalaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dutySalaries in body
     */
    @GetMapping("/[domainNameVarHythen]s")
    @Timed
    public ResponseEntity<List<[domainName]DTO>> getAllDutySalaries(Pageable pageable) {
        log.debug("REST request to get a page of DutySalaries");
        Page<[domainName]DTO> page = [domainNameVar]Service.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/[domainNameVarHythen]s");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    
    /**
     * GET  /[domainNameVarHythen]s : get all the dutySalaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dutySalaries in body
     */
    @GetMapping("/[domainNameVarHythen]sx")
    @Timed
    public ResponseEntity<List<[domainName]DTO>> getAllDutySalariesx(@RequestParam Map<String,Object> filters) {
        log.debug("REST request to get a page of DutySalaries");
        Page<[domainName]DTO> page = [domainNameVar]Service.findAll(filters);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/[domainNameVarHythen]sx");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    

    /**
     * GET  /[domainNameVarHythen]s/:id : get the "id" [domainNameVar].
     *
     * @param id the id of the [domainNameVar]DTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the [domainNameVar]DTO, or with status 404 (Not Found)
     */
    @GetMapping("/[domainNameVarHythen]s/{id}")
    @Timed
    public ResponseEntity<[domainName]DTO> get[domainName](@PathVariable Long id) {
        log.debug("REST request to get [domainName] : {}", id);
        [domainName]DTO [domainNameVar]DTO = [domainNameVar]Service.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable([domainNameVar]DTO));
    }

    /**
     * DELETE  /[domainNameVarHythen]s/:id : delete the "id" [domainNameVar].
     *
     * @param id the id of the [domainNameVar]DTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/[domainNameVarHythen]s/{id}")
    @Timed
    public ResponseEntity<Void> delete[domainName](@PathVariable Long id) {
        log.debug("REST request to delete [domainName] : {}", id);
        [domainNameVar]Service.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
