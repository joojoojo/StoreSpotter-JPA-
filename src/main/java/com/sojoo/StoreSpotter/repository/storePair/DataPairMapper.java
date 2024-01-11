package com.sojoo.StoreSpotter.repository.storePair;

import com.sojoo.StoreSpotter.entity.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.entity.storePair.PairData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataPairMapper {
    //전체 업종 테이블 데이터 가져오기
    List<StoreInfo> selectIndustryData(@Param("indust_id") String indust_id) throws Exception;

    // 편의점 테이블 데이터 삽입
    void insertPairData(PairData pairData, @Param("indust_id") String indust_id);

    // List<ToPairData> distanceSphere(@Param("convenienceData") String point, Integer region);
    List<PairData> distanceSphere(String name, String point, Integer region, @Param("indust_id") String indust_id);

    // 생성 된 pair 테이블의 데이터 중복제거(st=com and com=st)
    void deleteDuplicatePair(@Param("indust_id") String indust_id);
}
