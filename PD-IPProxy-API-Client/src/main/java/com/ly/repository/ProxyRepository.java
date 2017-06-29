package com.ly.repository;

import com.ly.domains.Proxy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by yongliu on 6/26/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  06/26/2017 14:02
 */
@Repository public interface ProxyRepository extends PagingAndSortingRepository<Proxy, Long> {


  @Query("select p from Proxy p where p.country='国内' and p.types>=:types and p.area like CONCAT('%', :area, '%') and p.id not in (select pa.proxyId from ProxyAudit pa where pa.tokenId=:tokenId)")
  List<Proxy> getIp( @Param("tokenId")String tokenId, @Param("types")Integer types, @Param("area")String area, Pageable p);


  @Query("select p from Proxy p where p.country='国内' and p.area like CONCAT('%', :area, '%') and p.id not in (select pa.proxyId from ProxyAudit pa where pa.tokenId=:tokenId)")
  List<Proxy> getIp( @Param("tokenId")String tokenId, @Param("area")String area,  Pageable p);

  @Query("select p from Proxy p where p.country='国内' and p.id not in (select pa.proxyId from ProxyAudit pa where pa.tokenId=:tokenId)")
  List<Proxy> getIp( @Param("tokenId")String tokenId,  Pageable p);
}
