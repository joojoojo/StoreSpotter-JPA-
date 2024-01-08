package com.sojoo.StoreSpotter.dao.storePair;

import com.sojoo.StoreSpotter.dto.storePair.ConveniencePair;
import com.sojoo.StoreSpotter.dto.storePair.PairData;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ConveniencePairRepository extends JpaRepository<ConveniencePair, Integer> {


    @Query(value = "SELECT ST_DISTANCE_SPHERE(ST_GeomFromText(:st_coor), c.coordinates) AS dist," +
            "                       :region_fk AS region_fk," +
            "                       :st_nm AS st_nm," +
            "                       :st_coor AS st_coor," +
            "                       c.bizes_nm AS com_nm," +
            "                       ST_AsWKT(c.coordinates) AS com_coor" +
            "                FROM convenience_store c" +
            "                WHERE c.region_fk = :region_fk" +
            "                AND ST_Within(c.coordinates, ST_Buffer(ST_GeomFromText(:st_coor), 500))" +
            "                AND ST_Distance_Sphere(ST_GeomFromText(:st_coor), c.coordinates) > 10" +
            "                ORDER BY dist" +
            "                LIMIT 1", nativeQuery=true)

    List<ConveniencePair> convenience_distanceSphere(@Param("st_nm") String st_nm, @Param("st_coor") String st_coor, @Param("region_fk") Integer region_fk);

    @Modifying
    @Transactional
    @Query("DELETE FROM ConveniencePair c1 " +
            "WHERE EXISTS (" +
            "    SELECT c2 " +
            "    FROM ConveniencePair c2 " +
            "    WHERE c1.stNm = c2.comNm " +
            "      AND c1.comNm = c2.stNm " +
            "      AND c1.pairId > c2.pairId)")
    void convenience_deleteDuplicatePairs();


}
