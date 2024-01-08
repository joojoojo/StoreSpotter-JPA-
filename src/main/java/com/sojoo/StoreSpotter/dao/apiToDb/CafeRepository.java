package com.sojoo.StoreSpotter.dao.apiToDb;

import com.sojoo.StoreSpotter.dto.apiToDb.Cafe;
import com.sojoo.StoreSpotter.dto.apiToDb.ConvenienceStore;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, String> {
    List<Cafe> findAll();

    @Query(value = "SELECT c.bizes_id, c.bizes_nm, c.rdnm_adr, ST_AsText(c.coordinates), c.region_fk " +
            "FROM cafe c", nativeQuery = true)
    List<Cafe> findAllCafe();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cafe (bizes_id, bizes_nm, rdnm_adr, coordinates, region_fk) " +
            "VALUES (:#{#cafe.bizesId}, :#{#cafe.bizesNm}, " +
            ":#{#cafe.rdnmAdr}, ST_GeomFromText(:coordinates), :#{#cafe.regionFk})", nativeQuery = true)
    void insertCafe(@Param("cafe") Cafe cafe, @Param("coordinates") String coordinates);

}

