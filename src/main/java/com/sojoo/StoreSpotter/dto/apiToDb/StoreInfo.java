package com.sojoo.StoreSpotter.dto.apiToDb;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class StoreInfo  implements Serializable {
    @Id
    @Column(name = "bizes_id")
    private String bizesId;

    @Column(name = "bizes_nm")
    private String bizesNm;

    @Column(name = "rdnm_adr")
    private String rdnmAdr;

    @Column(name="coordinates", columnDefinition = "geometry")
    private Point coordinates;
//    private Geometry coordinates;
    @Column(name = "region_fk")
    private Integer regionFk;

//    @Builder
//    public StoreInfo(String bizes_id, String bizes_nm, String rdnm_adr, String coordinates, Integer region_fk){
//        this.bizesId = bizes_id;
//        this.bizesNm = bizes_nm;
//        this.rdnmAdr = rdnm_adr;
//        this.coordinates = setCoordinates(getCoordinates().getX(), getCoordinates().getY());
//        this.regionFk = region_fk;
//    }


    public void setCoordinates(Double lon, Double lat) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(lon, lat);
        System.out.println(geometryFactory.createPoint(new Coordinate(lon, lat)));
        this.coordinates = geometryFactory.createPoint(coordinate);
    }

}
