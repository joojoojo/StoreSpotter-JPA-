package com.sojoo.StoreSpotter.dao.apiToDb;

import com.sojoo.StoreSpotter.dto.apiToDb.ConvenienceStore;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ConvenienceStoreRepository extends JpaRepository<ConvenienceStore, String> {
    @Override
    List<ConvenienceStore> findAll();

    @Query(value = "SELECT c.bizes_id, c.bizes_nm, (c.coordinates), c.rdnm_adr, c.region_fk " +
            "FROM convenience_store c", nativeQuery = true)
    List<ConvenienceStore> findConvenienceStore();


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO convenience_store (bizes_id, bizes_nm, rdnm_adr, coordinates, region_fk) " +
            "VALUES (:#{#convenienceStore.bizesId}, :#{#convenienceStore.bizesNm}, " +
            ":#{#convenienceStore.rdnmAdr}, ST_GeomFromText(:coordinates), :#{#convenienceStore.regionFk})", nativeQuery = true)
    void insertConv(@Param("convenienceStore") ConvenienceStore convenienceStore, @Param("coordinates") String coordinates);

}
